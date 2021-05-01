package com.example.mybiography;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybiography.Recycler.JobRecyclerAdapter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    public static final int ChangeToWeekendIdx = 1009; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    public static final int ChangeToAddJobIdx = 10010; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    TextView tv_userName, dateTextView, checkTextView;

    CalendarView calendarView;

    Button delBtn, changeToWeekendBtn;

    Toolbar calToolBar; //<androidx.appcompat.widget.Toolbar사용했으므로 androidx에 있는 toolbar를 사용
    ActionBar actionBar;

    String fileName; //저장할 파일 이름

    public String str;

    private int mYear = 0, mMonth = 0, mDay = 0;
    Boolean isFileExist = true;
    String isWriting = "false";

    ArrayList<Job> jobList = new ArrayList<>();
    RecyclerView jobRecyclerView;
    //    SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Intent intent = getIntent();
        String userName = intent.getExtras().getString("userName");

        tv_userName = findViewById(R.id.userNameDisp_cal);
        tv_userName.setText(userName + "님의 월간일정");

        dateTextView = findViewById(R.id.dateTextView); //선택한 날짜표시
        calendarView = findViewById(R.id.calendarView);

        //툴바
        makeToolBar();

        //캘린더 시작시 해당날짜의 입력된 내용 보여주기
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        try {
            checkDay(mYear, mMonth, mDay);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        setDataListItems();
        initRecyclerView();

        //다른날짜선택
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                try {
                    checkDay(year, month, dayOfMonth);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setDataListItems() {
        jobList.add(new Job("TEST1123123123123123123123123", "20210501", "testContents1", "INACTIVE"));
        jobList.add(new Job("TEST2", "20210501", "testContents2","ACTIVE"));
        jobList.add(new Job("TEST3", "20210501", "testContents3", "COMPLETED"));
        jobList.add(new Job("TEST4", "20210501", "testContents4","COMPLETED"));
    }

    private void initRecyclerView() {
        // 리사이클러뷰에 RecyclerAdapter 객체 지정.
        JobRecyclerAdapter jobAdapter = new JobRecyclerAdapter(jobList);
        jobRecyclerView = findViewById(R.id.jobRecyclerView);
        jobRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)); //새로로 나열
        jobRecyclerView.setAdapter(jobAdapter);
    }

    public void makeToolBar() {
        //ToolBar create
        calToolBar = findViewById(R.id.cal_toolbar);
        setSupportActionBar(calToolBar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목삭제
        actionBar.setDisplayHomeAsUpEnabled(false); //자동으로 뒤로가기 버튼을 만들어줍니다.

        //추가버튼
        changeToWeekendBtn = findViewById(R.id.changeToWeekendBtn);
        changeToWeekendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WeekendActivity.class);
                startActivityForResult(intent, ChangeToWeekendIdx); //액티비티 띄우기
            }
        });
    }

    private long Back_Key_Before_Time = 0;
    Boolean isBack = false;
    AlertDialog alertDialog;

    @Override
    public void onBackPressed() {
        Log.d("test", "isBackFFF: " + isBack);
//        isWriting = pref.getString("isWriting", null);
//        if (isWriting.equals("true")) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("작성하시던 내용은 초기화 됩니다");

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "저장하세요!!", Toast.LENGTH_LONG).show();
                isBack = false;
            }
        });
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("test", "isBack3: " + isBack);
//                Toast.makeText(getApplicationContext(), "다시 한번 더 눌러주세요!", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "메인페이지로 이동!", Toast.LENGTH_LONG).show();
                isBack = true;
                onBackPressed();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
        Log.d("test", "isBack2 : " + isBack);
        if (isBack) {
            super.onBackPressed();
            if (alertDialog != null) {
                alertDialog.dismiss();
                alertDialog = null;
            }
        }
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void checkDay(int year, int month, int dayOfMonth) throws FileNotFoundException {
        dateTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth)); //선택한 날짜표시
//        fileName = "" + year + "-" + (month + 1) + "" + "-" + dayOfMonth + ".txt";//저장할 파일 이름설정

//        FileInputStream fis = null;//FileStream fis 변수

        try {
//            fis = openFileInput(fileName);
//
//            byte[] fileData = new byte[fis.available()];
//            fis.read(fileData);
//            fis.close();

//            str = new String(fileData, "UTF-8");
            isFileExist = true;
        } catch (Exception e) { //파일이 존재하지않을때
            e.printStackTrace();
//            saveBtn.setVisibility(View.INVISIBLE);
//            delBtn.setVisibility(View.INVISIBLE);
            isFileExist = false;
        }

        if (isFileExist == true) { //파일이 있으면
//            checkTextView.setText(str); //날짜에 적은거 있으면 보여주기
//            Log.d("test", "checkTextView : " + mYear + mMonth + mDay);
//            checkTextView.setVisibility(View.VISIBLE); //적은거 있으면 보이기
        } else { //파일이 없으면
//            checkTextView.setVisibility(View.VISIBLE);
//            checkTextView.setText("아직 입력한 내용이 없습니다.");
//            contextEditText.setText("");
        }

//        if (contextEditText.getVisibility() == View.VISIBLE) {
//            contextEditText.setVisibility(View.INVISIBLE);// 수정화면숨기기
////            saveBtn.setVisibility(View.INVISIBLE);
////            delBtn.setVisibility(View.INVISIBLE);
//        }

//        checkTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isWriting = "true"; //작성중플래그
//
//                Intent intent = new Intent(getApplicationContext(), AddJobActivity.class);
//                startActivityForResult(intent, ChangeToAddJobIdx); //액티비티 띄우기
//
//                //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
////                SharedPreferences.Editor editor = pref.edit();
////                editor.putString("isWriting", isWriting); // key, value를 이용하여 저장하는 형태
//
//                //해당글클릭하면 수정/삭제할수있게
////                checkTextView.setVisibility(View.INVISIBLE);//텍스트뷰 숨기기
////                if (contextEditText.getText().length() > 0 && isFileExist == true) { //파일이 존재, 내용있을때
////                if (isFileExist == true) { //파일이 존재, 내용있을때
////                    contextEditText.setText(str);
////                    contextEditText.setVisibility(View.VISIBLE);
////                    contextEditText.setEnabled(true);
////                    saveBtn.setText("수정");
////                    saveBtn.setVisibility(View.VISIBLE); //저장버튼 보이기
////                    delBtn.setVisibility(View.VISIBLE);  // 삭제버튼 보이기
////                } else {
////                    contextEditText.setVisibility(View.VISIBLE);
////                    contextEditText.setEnabled(true); //에디터 활성화
////                    saveBtn.setText("저장");
////                    saveBtn.setVisibility(View.VISIBLE);
////                }
//            }
//        });
    }

    //저장 https://recipes4dev.tistory.com/113
    //텍스트(text) 파일을 읽고 쓸 때는 FileInputStream 또는 FileOutputStream을 사용하지 않고,
    // 텍스트(text) 파일 입출력을 위해 별도로 제공되는 클래스를 사용 -> 어려움....
//    @SuppressLint("WrongConstant")
//    private void saveContent(String fileName) {
//        FileOutputStream fos = null;
//        try {
//            fos = openFileOutput(fileName, MODE_NO_LOCALIZED_COLLATORS); //파일이름저장
//            String content = contextEditText.getText().toString(); //입력한 값 불러와서 스트링에 담기
//            fos.write(content.getBytes());
//            fos.close();
//
//            Toast.makeText(CalendarActivity.this, "저장", Toast.LENGTH_SHORT).show();
////            contextEditText.setText("");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(CalendarActivity.this, "저장오류", Toast.LENGTH_SHORT).show();
//        }
//    }
}