package com.example.mybiography;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybiography.Recycler.ItemTouchHelperCallback;
import com.example.mybiography.Recycler.JobRecyclerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity {

    public static final int CalendarActivityIdx = 1009; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    private static final String TAG = "CalendarActivity";

    TextView tv_userName, dateTextView, dateTextView2, checkTextView, calTextView;
    ImageView job_imageView;
    MaterialCalendarView calendarView;
//    CalendarView calendarView;

    Button changeToWeekendBtn, addJobBtn, delBtn;

    Toolbar calToolBar; //<androidx.appcompat.widget.Toolbar사용했으므로 androidx에 있는 toolbar를 사용
    ActionBar actionBar;

    public String str;

    private int mYear = 0, mMonth = 0, mDay = 0;
    Boolean isFileExist = true;
    String isWriting = "false";

    public static ArrayList<Job> jobList = new ArrayList<>(); //static으로 둬서 재갱신 되지 않도록
    RecyclerView jobRecyclerView;
    ItemTouchHelper helper;
    String loginId, loginName;

    String key = SysCodes.KeyCodes.JOBLIST.toString();
    public String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        SharedPreferences pref = getSharedPreferences(SysCodes.KeyCodes.PREF.toString(), Activity.MODE_PRIVATE);
        loginId = pref.getString(SysCodes.KeyCodes.LOGIN_ID.toString(), "저장된 아이디 없음");
        loginName = pref.getString(SysCodes.KeyCodes.USER_NAME.toString(), "저장된 이름 없음");

        Log.d("CalActivity_loginId: ", loginId + ", loginName: " + loginName);

        //인텐트 userId, userName
        Intent intent = getIntent(); /*데이터 get*/
//        String loginId = "";
        if (intent != null && intent.hasExtra(SysCodes.KeyCodes.LOGIN_ID.toString())) {
            loginId = intent.getExtras().getString(SysCodes.KeyCodes.LOGIN_ID.toString());
        }

        userId = loginId;
        Log.d("CalActivity_loginId87", "userId: " + userId + ", loginId: " + loginId);

        //Member쉐어드 프리퍼런스 불러오기
        userId = getMemberId(userId);

        if (intent != null && intent.hasExtra(SysCodes.KeyCodes.USER_NAME.toString())) {
            loginName = intent.getExtras().getString(SysCodes.KeyCodes.USER_NAME.toString());
        }
//        Log.d("CalActivity_loginId2: ", loginId + ", userId: " + userId +", loginName: " + loginName);

        String intentUserId = loginId; //인텐트에 넣을 변수. 다른곳에 쓰면 안됨.

        calTextView = findViewById(R.id.calTextView);
        calTextView.setText(loginName + "님의 월간일정");

        //툴바
        makeToolBar(loginId);

        dateTextView = findViewById(R.id.dateTextView); //선택한 날짜표시
        dateTextView2 = findViewById(R.id.dateTextView2); //선택한 날짜표시 DB연결용

        //쉐어드프리퍼런스 불러오기 -> jobList에 일단 넣어주기
        jobList = getJobListAll();

        //생성, 수정, 삭제관련
        if (intent != null && intent.hasExtra("fromAddJobActivity")) {
            Job job = (Job) intent.getExtras().get("fromAddJobActivity"); //new Job check

            Log.d("fromAddJobActivity1", String.valueOf(jobList.size()));
            Log.d("fromAddJobActivity2", String.valueOf(job.writeDate));

            // 새로운 일정이 있는지 확인
            isUpdateDelCheck(job);
        }

        //캘린더 시작시 해당날짜의 입력된 내용 보여주기
        Calendar calendar = Calendar.getInstance(); //오늘날짜
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        //캘린더데코 //오늘날짜
        calendarDeco();

        //캘린더점찍기
        int size = 1;
        HashMap<CalendarDay, Integer> datesMap = new HashMap<>();
        Log.d("fromAddJobActivity2", String.valueOf(datesMap));
        for (int i = 0; i < jobList.size(); i++) {
            if (jobList.get(i).loginId.equals(loginId)) {
                String[] time = jobList.get(i).startDate.split("/");
                int year1 = Integer.parseInt(time[0]);
                int month1 = Integer.parseInt(time[1]);
                int day1 = Integer.parseInt(time[2]);

               //기존데이터값이 있을때
                if (datesMap.size() > 0) {
                    //중복일때
                    if (datesMap.containsKey(CalendarDay.from(year1, month1, day1))){ //키를 포함하고 있으면
                        int val = datesMap.get(CalendarDay.from(year1, month1, day1)); //키값가져오기
                        datesMap.remove(CalendarDay.from(year1, month1, day1)); //제거
                        datesMap.put(CalendarDay.from(year1, month1, day1), ++val); //다시 넣기
                    } else{//처음일때
                        datesMap.put(CalendarDay.from(year1, month1, day1), 1);
                    }
                } else { //처음일때
                    datesMap.put(CalendarDay.from(year1, month1, day1), 1);
                }
            }
        }
        int[] threeColors = new int[0];
        for (Map.Entry<CalendarDay,Integer> entry : datesMap.entrySet()){
            Log.d("keyset: ","[key]:" + entry.getKey() + ", [value]:" + entry.getValue());
            if(entry.getValue()==1){
                threeColors = new int[]{Color.rgb(255, 0, 0)}; //빨강
            } else if(entry.getValue() ==2){
                threeColors = new int[]{ Color.rgb(0, 255, 0), Color.rgb(255, 0, 0)}; //[화면상] 빨강->초록
            } else if(entry.getValue() >=3){
                threeColors = new int[]{Color.rgb(0, 0, 255), Color.rgb(0, 255, 0), Color.rgb(255, 0, 0)};//[화면상] 빨강->초록 -> 파랑
            }
            CalendarDay dates1 = entry.getKey();
            calendarView.addDecorators(new EventDecorator(threeColors, dates1));
        }

        //오늘날짜일때
        try {
            checkDay(mYear, mMonth + 1, mDay);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //다른날짜선택
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull @NotNull MaterialCalendarView widget, @NonNull @NotNull CalendarDay date, boolean selected) {
                try {
                    int year = date.getYear();
                    int month = date.getMonth();
                    int day = date.getDay();
                    Log.d(TAG, "1 " + year + " / " + month + " / " + day);
                    checkDay(year, month, day);

//                    calendarView.clearSelection();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        //일정추가
        addJobBtn = findViewById(R.id.addJobBtn);
        addJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddJobActivity.class);
                intent.putExtra("loginId", intentUserId);
                intent.putExtra("jobStartDate", dateTextView2.getText().toString());
                Log.d("calActivity139", " writeDate: " + dateTextView2.getText().toString());
                //새로 생성하는 것이므로 쉐어드에서 isWriting = true로 바꿔줌.
                SharedPreferences setIsWriting = getSharedPreferences("tempSaveJob", MODE_PRIVATE);
                setIsWriting.edit().putBoolean("isWriting", true).commit();
                //이동
                startActivityForResult(intent, CalendarActivityIdx); //액티비티 띄우기
            }
        });

    }

    private ArrayList<Job> getJobListAll() {
        SharedPreferences jobListPref = getSharedPreferences(key, Activity.MODE_PRIVATE);
        String jobListStr = jobListPref.getString(key, "No saved data");
        Log.d("checkRecover1", "jobListJson? " + jobListStr.toString());
        Gson gson = new Gson();
        if (!jobListStr.equals("No saved data")) {
            Type type = new TypeToken<ArrayList<Job>>() {
            }.getType();
            jobList = gson.fromJson(jobListStr, type);
        }
        return jobList;
    }

    private void calendarDeco() {
        // 오늘날짜 표기
        calendarView = findViewById(R.id.calendarView);
        // 오늘꾸미기
        calendarView.addDecorators(new OneDayDecorator());
    }


    //Member쉐어드 프리퍼런스 불러오기
    public String getMemberId(String userId) {

        Log.d("CalActivity249", "memberId: " + userId);
        String memberKey = SysCodes.KeyCodes.MEMBER.toString();

        SharedPreferences memberPref = getSharedPreferences(memberKey, Activity.MODE_PRIVATE); //다른 액티비티에서 사용 가능
        String prefMemberList = memberPref.getString(memberKey, null);
//        Log.d("MemoActivity162", "checkIdShared: " + prefMemberList);
        Gson gson = new Gson();
        ArrayList<Member> memList = new ArrayList<>();
        if (prefMemberList != null) {
            Type type = new TypeToken<ArrayList<Member>>() {
            }.getType();
            memList = gson.fromJson(prefMemberList, type);
//            Log.d("MemoActivity162", "checkIdShared_memList.size: " + memList.size());
            for (int i = 0; i < memList.size(); i++) {
                if (memList.get(i).loginId.equals(userId)) {
                    userId = memList.get(i).loginId;
//                    Log.d("MemoActivity162", "checkIdShared_memList: " + memList.get(i).loginId);
                }
            }
        }
        Log.d("CalActivity189", "memberId: " + userId);
        return userId;
    }

    private void isUpdateDelCheck(Job job) {
        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        Intent checkAddJobIntent = getIntent();
        Boolean isUpdated = checkAddJobIntent.getExtras().getBoolean("job_isUpdate");
        Boolean isDelete = checkAddJobIntent.getExtras().getBoolean("job_isDelete");
        Log.d("isUpdateDelCheck1", "isUpdated: " + isUpdated + ", isDelete: " + isDelete);
        //수정
        if (isUpdated == true) {
            for (int i = 0; i < jobList.size(); i++) {
                Log.d("isUpdateDelCheck2", "1: " + jobList.get(i).loginId);
                Log.d("isUpdateDelCheck2", "2: " + job.loginId);
//                Log.d("isUpdateDelCheck2", "3: " + String.valueOf(jobList.get(i).loginId.equals(job.loginId)));
                //id이랑 작성날짜 같으면..
                if (jobList.get(i).loginId.equals(job.loginId))
                    if (jobList.get(i).writeDate.equals(job.writeDate)) {
//                        Log.d("isUpdateDelCheck3", "3: " + jobList.get(i).writeDate.equals(job.writeDate));
                        jobList.set(i, job);
                    }
            }
            //삭제
        } else if (isDelete == true) {
            for (int i = 0; i < jobList.size(); i++) {
                if (jobList.get(i).jobName.equals(job.jobName) && jobList.get(i).startDate.equals(job.startDate)) {
                    Toast.makeText(getApplicationContext(), "삭제완료", Toast.LENGTH_SHORT).show();
                    jobList.remove(i);
                }
            }
            //새로 생성
        } else {
            Log.d("isUpdateDelCheck4", "4: " + loginId + ", " + job.loginId);
            job.loginId = loginId;
            jobList.add(job);
        }

        //쉐어드프리퍼런스에 저장
        String json = "";
        Gson gson = new Gson();
        json = gson.toJson(jobList);
        Log.d("CalActivity199", "JobListJson? " + json);
        SharedPreferences jobListPrefs = getSharedPreferences(key, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = jobListPrefs.edit();
        prefEditor.putString(key, json);
        prefEditor.commit();

    }

    private void initRecyclerView(ArrayList<Job> jobList) {
        // 리사이클러뷰에 RecyclerAdapter 객체 지정.
        JobRecyclerAdapter jobAdapter = new JobRecyclerAdapter(jobList);
        jobRecyclerView = findViewById(R.id.jobRecyclerView);
        //RecyclerView의 레이아웃 방식을 지정
        jobRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)); //새로로 나열
        jobRecyclerView.setAdapter(jobAdapter);

        //ItemTouchHelpler생성
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(jobAdapter));
        //RecyclerView에 ItemTouchHelper 붙이기
        helper.attachToRecyclerView(jobRecyclerView);
    }

    // 오른쪽 밀어서 삭제버튼이 위아래 스크롤 할때 없어지는거 방지
    private void setUpRecyclerView() {
//        jobRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration(){
//            @Override
//            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull State state) {
//                ItemTouchHelperCallback.onDraw(c);
////                super.onDraw(c, parent, state);
//            }
//        });
    }


    public void makeToolBar(String loginId) {
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
                Intent intent = new Intent(getApplicationContext(), WeeklyActivity.class);
                intent.putExtra(SysCodes.KeyCodes.LOGIN_ID.toString(), loginId);
                startActivityForResult(intent, CalendarActivityIdx); //액티비티 띄우기
            }
        });
    }

    private long Back_Key_Before_Time = 0;
    Boolean isBack = false;

    public void checkDay(int year, int month, int dayOfMonth) throws FileNotFoundException {
        //선택한 날짜표시
        SimpleDateFormat sdf_ = new SimpleDateFormat("EEE", Locale.KOREA);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, dayOfMonth);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK); //요일구하기
//        Log.d("CalTextView367", String.valueOf(cal.getTime()));
        dateTextView.setText( dayOfMonth + ", "+ sdf_.format(cal.getTime()));
//        dateTextView.setText(String.format("%d/%d/%d", year, month, dayOfMonth));

        //선택한 날짜에 데이터있는 지 확인
        String checkDate = String.format("%d/%d/%d", year, month, dayOfMonth);
        dateTextView2.setText(checkDate);
        Log.d("checkDay", "checkDate: " + checkDate + ", size : " + String.valueOf(jobList.size()));
        ArrayList<Job> newJobList = new ArrayList<>();
        if (jobList.size() > 0) {
            for (int i = 0; i < jobList.size(); i++) {
//                Log.d("startDate: ", jobList.get(i).startDate);
                //날짜가 있으면 새로운 리스트에 담아서 뿌려주기
                if (checkDate.equals(jobList.get(i).startDate) && jobList.get(i).loginId.equals(userId)) {
//                    Log.d("isCheckedDayis Today?", "yes, " + i + ", startDate : " + jobList.get(i).startDate);
                    newJobList.add(jobList.get(i));
                } else {
//                    Log.d("isCheckedDayis Today?", "No" + i);
                }
            }

            //리사이클러뷰에 뿌려주기
            initRecyclerView(newJobList);
        } else {
            initRecyclerView(newJobList);
        }

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "메인페이지로 이동!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
        startActivityForResult(intent, CalendarActivityIdx); //액티비티 띄우기
    }
}