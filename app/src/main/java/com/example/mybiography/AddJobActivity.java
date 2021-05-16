package com.example.mybiography;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import java.util.Calendar;

public class AddJobActivity extends AppCompatActivity {

    TextView cal_DateTitleTextView2, cal_StartDateTextView, cal_ContentsTitleTextView, timeDialogBtn;
    Button saveBtn, delBtn;
    EditText jobTitleEditText, contextEditText;

    Toolbar calToolBar; //<androidx.appcompat.widget.Toolbar사용했으므로 androidx에 있는 toolbar를 사용
    ActionBar actionBar;

    private int mYear = 0, mMonth = 0, mDay = 0;

    public static final int GoToCalendarIdx = 1011; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    Job job = new Job();
    Boolean isUpdate = false;
    Boolean isDelete = false;
    Boolean isWriting = true; // 창이 열릴때는 항상 수정모드여야 함.
    String loginId;

    Context context;
    String key = "tempSaveJob";
    public Boolean dialogValue = false;

    int alarmHour= 0, alarmMinute =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        jobTitleEditText = findViewById(R.id.jobTitleEditText); //일정제목
        contextEditText = findViewById(R.id.multiLineEditText); //내용입력

        cal_DateTitleTextView2 = findViewById(R.id.cal_DateTitleTextView2);
        cal_StartDateTextView = findViewById(R.id.cal_StartDateTextView);
        cal_ContentsTitleTextView = findViewById(R.id.cal_ContentsTitleTextView);

        timeDialogBtn = findViewById(R.id.timeDialogBtn); //시간다이얼불러오기

        saveBtn = findViewById(R.id.saveBtn);
        delBtn = findViewById(R.id.delBtn);
        if (delBtn.getVisibility() == View.VISIBLE) {
            delBtn.setVisibility(View.INVISIBLE);
        }

        SharedPreferences pref = getSharedPreferences(SysCodes.KeyCodes.PREF.toString(), Activity.MODE_PRIVATE);
        loginId = pref.getString(SysCodes.KeyCodes.LOGIN_ID.toString(), null);
        job.loginId = loginId;

        //쉐어드에서 작성중인 파일 있는지 확인
        isWriting = getWritingState();
        SharedPreferences tempSavePref = getSharedPreferences(key, Activity.MODE_PRIVATE);
        String isTempEmpty = tempSavePref.getString(key, "");
        Log.d("AddJobActivity74", "isTempEmpty? " + isTempEmpty + ", isWriting? " + isWriting);

        if (isWriting == true && !isTempEmpty.isEmpty()) { //작성중이였으면 물어보기
            showDialog();
        }

        //불러오기
        Intent intent = getIntent();
        
        //유저아디 불러오기
        if (intent != null && intent.hasExtra(SysCodes.KeyCodes.LOGIN_ID.toString())) {
            String loginId = intent.getExtras().getString(SysCodes.KeyCodes.LOGIN_ID.toString());
            job.loginId = loginId;
//            Log.d("AddJobActivity87", loginId + ", job id: " + job.loginId);
        }

        //수정모드이면
        if (intent != null && intent.hasExtra("jobClass")) {
            job = (Job) intent.getExtras().get("jobClass");
            isUpdate = intent.getExtras().getBoolean("jobClass_isUpdate"); //true;
            isDelete = false;
            jobTitleEditText.setText(job.jobName); //제목
            cal_StartDateTextView.setText(job.startDate); //일정시작날짜
            contextEditText.setText(job.jobContents); //내용
            delBtn.setVisibility(View.VISIBLE); //버튼 숨기기
        }

        if (isUpdate == false) {
            Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);

            //날짜확인
            String startDate= intent.getExtras().getString("jobStartDate");

            cal_StartDateTextView.setText(startDate); //선택한 날짜표시
            job.writeDate = Util.getCalendar();
        }


        timeDialogBtn.setOnClickListener(new View.OnClickListener() { //다이얼불러오기
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddJobActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            hourOfDay = view.getHour();
                            minute = view.getMinute();
                        }
                        Log.d("TAG",String.valueOf(hourOfDay));
                        Log.d("TAG",String.valueOf(minute));
                        String ampm = "AM";
                        if(hourOfDay >= 12){
                            ampm = "PM";
                        }
                        timeDialogBtn.setText(hourOfDay + ":" + minute + " "+ ampm);
                    }
                }, alarmHour,alarmMinute, false);
                timePickerDialog.show();
                Log.d("TAG",String.valueOf(alarmHour));
            }

        });

        saveBtn.setOnClickListener(new View.OnClickListener() { //저장클릭
            @Override
            public void onClick(View v) {
                // 입력한 내용 값 받기
                job.jobName = jobTitleEditText.getText().toString(); //일정제목
                job.startDate = cal_StartDateTextView.getText().toString(); //날짜
                job.startTime = timeDialogBtn.getText().toString(); //시간
                job.jobContents = contextEditText.getText().toString(); // 내용

                saveJobContent(job);
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddJobActivity103", "delBtn Clicked");
                isDelete = true;
                isUpdate = false;
                // 작성중인 내용 삭제
                SharedPreferences tempSavePref = getSharedPreferences(key, MODE_PRIVATE);
                tempSavePref.edit().remove(key).commit(); //key : tempSaveJob 만 삭제
                String json = tempSavePref.getString(key, ""); //key : tempSaveJob
//                tempSavePref.edit().putBoolean("isWriting", false).commit();
                goToCalActivity(job);

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        isWriting = getWritingState();
        Log.d("AddJobActivity153", "onPause2, isWriting: " + String.valueOf(isWriting) + ", isUpdate: " + String.valueOf(isUpdate) + ", isDelete: " + String.valueOf(isDelete));
        if (isWriting == true && (isUpdate == false || isDelete == false)) {
            // 입력한 내용 값 받기
            job.jobName = jobTitleEditText.getText().toString(); //일정제목
            job.startDate = cal_StartDateTextView.getText().toString(); //시간
            job.jobContents = contextEditText.getText().toString(); // 내용
            setJobArrayPref(context, key, job);
            Toast.makeText(this, "임시 자동저장", Toast.LENGTH_SHORT).show();
        }

    }

    public void checkRecover(Boolean dialogValue) {
        SharedPreferences tempSavePref = getSharedPreferences(key, MODE_PRIVATE);
        Log.d("checkRecover1", "dialogValue? " + dialogValue);

        Gson gson = new Gson();
        String json = tempSavePref.getString(key, ""); //key : tempSaveJob
        tempSavePref.edit().putBoolean("isWriting", false);

        Log.d("checkRecover2", "tempJson? " + json + ", isWriting : " + getWritingState());

        if (dialogValue == true) { //작성중이였으면 물어보기
            Log.d("checkRecover4", "isRecover? " + dialogValue);
            if (dialogValue == true) {
                job = gson.fromJson(json, Job.class);
                Log.d("checkRecover5", "tempJson2? " + job.toString());
                jobTitleEditText.setText(job.jobName); //제목
                cal_StartDateTextView.setText(job.startDate); //일정시작날짜
                timeDialogBtn.setText(job.startTime); //일정시작날짜
                contextEditText.setText(job.jobContents); //내용
                delBtn.setVisibility(View.VISIBLE); //버튼 숨기기
            }
        } else {
            // 작성중인 내용 삭제
            tempSavePref.edit().remove(key).commit(); //key : tempSaveJob 만 삭제
        }
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddJobActivity.this);
        builder.setMessage("작성중이던 내용이 있습니다. 계속 하시겠습니까?");
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogValue = false;
                checkRecover(dialogValue);
            }
        });
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogValue = true;
                checkRecover(dialogValue);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Save Object to Preference :
    public void setJobArrayPref(Context context, String key, Job job) {
        Log.d("AddJobActivity193", "json? " + job.toString());
        Log.d("AddJobActivity193", "isWriting? " + isWriting);

        SharedPreferences prefs = getSharedPreferences(key, Activity.MODE_PRIVATE); //다른 액티비티에서 사용 가능
//        SharedPreferences prefs = getPreferences(MODE_PRIVATE); //한 액티비티안에서만 가능 getPreferences , AddJobActivity.xml에 저장됨
        SharedPreferences.Editor prefEditor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(job);
//        Log.d("AddJobActivity147", "json? " + json);
        prefEditor.putString(key, json);
        prefEditor.putBoolean("isWriting", isWriting);
        prefEditor.commit();
    }

    public boolean getWritingState() {
        SharedPreferences tempSavePref = getSharedPreferences(key, MODE_PRIVATE);
        isWriting = tempSavePref.getBoolean("isWriting", true);
        Log.d("getWritingState()227", "getIsWriting? " + isWriting);
        return isWriting;
    }

    public void saveJobContent(Job job) {
        Log.d("saveJobContent", "saveJobContent: " + job.toString());
        // 작성중인 내용 삭제
        SharedPreferences tempSavePref = getSharedPreferences(key, MODE_PRIVATE);
        tempSavePref.edit().remove(key).commit(); //key : tempSaveJob 만 삭제
        String json = tempSavePref.getString(key, ""); //key : tempSaveJob
//                tempSavePref.edit().putBoolean("isWriting", false).commit();
        goToCalActivity(job);
    }

    public void goToCalActivity(Job job) {
        //다른 액티비티로 넘어가기전 작성중인 플래그 false로 변경
        SharedPreferences tempSavePref = getSharedPreferences(key, MODE_PRIVATE);
        tempSavePref.edit().putBoolean("isWriting", false).commit();
        Log.d("goToCalActivity", "244_ job.writerId: " + job.loginId);
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra(SysCodes.KeyCodes.LOGIN_ID.toString(), job.loginId);
        intent.putExtra("job_isUpdate", isUpdate);
        intent.putExtra("job_isDelete", isDelete);
        intent.putExtra("fromAddJobActivity", job);
        Toast.makeText(getApplicationContext(), "달력페이지로 이동!", Toast.LENGTH_SHORT).show();
        startActivityForResult(intent, GoToCalendarIdx); //액티비티 띄우기 //대하는 결과가 충족 되면 자신을 호출한 액티비티로 값을 전달하는 동시에 스택에서 자기 자신을 지우고 종료
    }

    AlertDialog alertDialog;

    @Override
    public void onBackPressed() {
        isWriting = getWritingState();
        Log.d("onBackPressed", "onBackPressed, " + isWriting);

        if (isWriting == true) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("작성하시던 내용은 초기화 됩니다");

            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "저장하세요!!", Toast.LENGTH_LONG).show();
                }
            });
            builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("isBack3", "onBackPressed");

                    // 작성중인 내용 삭제
                    SharedPreferences tempSavePref1 = getSharedPreferences(key, MODE_PRIVATE);
                    tempSavePref1.edit().remove(key).commit(); //key : tempSaveJob 만 삭제
                    tempSavePref1.edit().putBoolean("isWriting", false).commit();
                    String json = tempSavePref1.getString(key,"없음");
                    Log.d("isBack4", "onBackPressed : " + json);

                    //이동
                    Toast.makeText(getApplicationContext(), "달력페이지로 이동!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                    startActivityForResult(intent, GoToCalendarIdx); //액티비티 띄우기
                    alertDialog.dismiss();
                    alertDialog = null;
                }
            });

            alertDialog = builder.create();
            alertDialog.show();
        } else {
            Toast.makeText(getApplicationContext(), "달력페이지로 이동!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddJobActivity.this, CalendarActivity.class);
            startActivityForResult(intent, GoToCalendarIdx); //액티비티 띄우기
        }
    }
}