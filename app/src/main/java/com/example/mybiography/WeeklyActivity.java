package com.example.mybiography;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybiography.Recycler.weeklyRecycler.FridayRecyclerAdapter;
import com.example.mybiography.Recycler.weeklyRecycler.MondayRecyclerAdapter;
import com.example.mybiography.Recycler.weeklyRecycler.SaturdayRecyclerAdapter;
import com.example.mybiography.Recycler.weeklyRecycler.SundayRecyclerAdapter;
import com.example.mybiography.Recycler.weeklyRecycler.ThursdayRecyclerAdapter;
import com.example.mybiography.Recycler.weeklyRecycler.TuesdayRecyclerAdapter;
import com.example.mybiography.Recycler.weeklyRecycler.WednesdayRecyclerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeeklyActivity extends AppCompatActivity {

    //TextView weeklyDateTextView;
    RecyclerView weeklyRecyclerView, sundayRecyclerView, mondayRecyclerView, tuesdayRecyclerView;
    RecyclerView wednesRecyclerView, thursdayRecyclerView, fridayRecyclerView, saturdayRecyclerView;

    TextView sundayTextView, mondayTextView, tuesdayTextView, wednesdayTextView;
    TextView thursdayTextView, fridayTextView, saturdayTextView;

    MaterialCalendarView calendarView_weekly;

    Button changeToMonthlyBtn;

    ArrayList<Job> weeklyList = new ArrayList<>();
    ArrayList<Job> newjobListById = new ArrayList<>();
    ArrayList<Job> jobList = new ArrayList<>();

    private int mYear = 0, mMonth = 0, mDay = 0;

    public static final int ChangeToMonthlyIdx = 1011; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    String loginId;
    String key = SysCodes.KeyCodes.JOBLIST.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly);

//        weeklyDateTextView = findViewById(R.id.weeklyDateTextView);

        SharedPreferences pref = getSharedPreferences(SysCodes.KeyCodes.PREF.toString(), Activity.MODE_PRIVATE);
        loginId = pref.getString(SysCodes.KeyCodes.LOGIN_ID.toString(), "저장된 아이디 없음");
        Log.d("CalActivity_loginId: ", loginId);

        //쉐어드프리퍼런스 불러오기 -> jobList에 일단 넣어주기
        SharedPreferences jobListPref = getSharedPreferences(key, Activity.MODE_PRIVATE); //"jobList"
        String jobListStr = jobListPref.getString(key, "No saved data");
        Log.d("WeeklyActivity", "jobListJson? " + jobListStr.toString());
        Gson gson = new Gson();
        if (!jobListStr.equals("No saved data")) {
            Type type = new TypeToken<ArrayList<Job>>() {
            }.getType();
            jobList = gson.fromJson(jobListStr, type);

            for (int i = 0; i < jobList.size(); i++){
                 if(jobList.get(i).loginId.equals(loginId)){
                     Log.d("WeeklyActivity78", "jobList.loginId? " + jobList.get(i).loginId + ", loginId: " + loginId);
                     newjobListById.add(jobList.get(i));
                 }
                Log.d("WeeklyActivity78", "newjobListById? " + jobList.get(i));
            }
            //다시 새로 넣기
            jobList = newjobListById;

        }

        sundayTextView = findViewById(R.id.sundayTextView);
        mondayTextView = findViewById(R.id.mondayTextView);
        tuesdayTextView = findViewById(R.id.tuesdayTextView);
        wednesdayTextView = findViewById(R.id.wednesdayTextView);
        thursdayTextView = findViewById(R.id.thursdayTextView);
        fridayTextView = findViewById(R.id.fridayTextView);
        saturdayTextView = findViewById(R.id.saturdayTextView);

        changeToMonthlyBtn = findViewById(R.id.changeToMonthlyBtn);

        changeToMonthlyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                intent.putExtra(SysCodes.KeyCodes.LOGIN_ID.toString(), loginId);
                startActivityForResult(intent, ChangeToMonthlyIdx); //액티비티 띄우기
            }
        });


        //캘린더 시작시 해당날짜의 입력된 내용 보여주기
        Calendar calendar = Calendar.getInstance(); //오늘날짜
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
//        weeklyDateTextView.setText(String.format("%d/%d/%d", mYear, mMonth + 1, mDay));

        int day = calendar.get(Calendar.DAY_OF_WEEK); //요일구하기
        
        //달력 꾸미기
        calendarDeco();

        setDataListItems();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm a"); //Date and time
        String currentDate = sdf.format(calendar.getTime());

        //Day of Name in full form like,"Saturday", or if you need the first three characters you have to put "EEE" in the date format and your result will be "Sat".
        SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE", Locale.KOREA);
        Date date = calendar.getTime();
        String dayName = sdf_.format(date);

        //선택한 날짜의 요일구하기
        //선택한 날짜의 시작 요일구하기기
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); //일요일
        Date date1 = calendar.getTime();
        sdf = new SimpleDateFormat("dd", Locale.getDefault());
        sdf_ = new SimpleDateFormat("EEE", Locale.KOREA);
        dayName = sdf_.format(date1);
        sundayTextView.setText(sdf.format(date1) + ", " + dayName);

        //해당날짜의 이력구하기
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/M/d", Locale.getDefault());
        String sunDate = sdf2.format(date1);
//        Log.d("SundayTextView126", sunDate + ", " + "요일1: " + dayName);
        if (jobList.size() > 0) { //DB
            ArrayList<Job> newWeeklyJobList = new ArrayList<>(); //일요일 일정에 넣을 새로운 리스트 그룹
            for (int i = 0; i < jobList.size(); i++) {
                String startDate = jobList.get(i).startDate;
//                Log.d("sundayTextView", ": " + startDate + ": " + sunDate);
                if (startDate.equals(sunDate)) { //해당날짜에 맞는 데이터 있는 지 찾기
                    newWeeklyJobList.add(jobList.get(i));
                    for (int j = 0; j < newWeeklyJobList.size(); j++) { //있으면 리스트 뿌려주기
//                        Log.d("sundayTextView132", i + ": " + newWeeklyJobList.get(j).startDate + ", " + newWeeklyJobList.get(j).jobContents);
                    }
                }
//                Log.d("sundayTextView132", "데이터 " + newWeeklyJobList.size() + " 건있음");
            }
            initSundayRecyclerView(newWeeklyJobList);
        }

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        date1 = calendar.getTime();
        dayName = sdf_.format(date1);
//        Log.d("MondayTextView", dayName + "요일: " + sdf.format(date1));
        String monDate = sdf2.format(date1);
        mondayTextView.setText(sdf.format(date1) + ", " + dayName);
        initMondayRecyclerView(setNewWeeklyJob(jobList, monDate));


        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        date1 = calendar.getTime();
        dayName = sdf_.format(date1);
//        Log.d("weeklyDateTextView", "요일1: " + sdf.format(date1));
        tuesdayTextView.setText(sdf.format(date1) + ", " + dayName);
        String tueDate = sdf2.format(date1);
        initTuesdayRecyclerView(setNewWeeklyJob(jobList, tueDate));

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        date1 = calendar.getTime();
        dayName = sdf_.format(date1);
//        Log.d("weeklyDateTextView", "요일1: " + sdf.format(date1));
        wednesdayTextView.setText(sdf.format(date1) + ", " + dayName);
        String wedDate = sdf2.format(date1);
        initWednesdayRecyclerView(setNewWeeklyJob(jobList, wedDate));

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        date1 = calendar.getTime();
        dayName = sdf_.format(date1);
//        Log.d("weeklyDateTextView", "요일1: " + sdf.format(date1));
        thursdayTextView.setText(sdf.format(date1) + ", " + dayName);
        String thurDate = sdf2.format(date1);
        initThursdayRecyclerView(setNewWeeklyJob(jobList, thurDate));

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        date1 = calendar.getTime();
        dayName = sdf_.format(date1);
//        Log.d("weeklyDateTextView", "요일1: " + sdf.format(date1));
        fridayTextView.setText(sdf.format(date1) + ", " + dayName);
        String friDate = sdf2.format(date1);
        initFridayRecyclerView(setNewWeeklyJob(jobList, friDate));


        //last day of week
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        date1 = calendar.getTime();
        dayName = sdf_.format(date1);
//        Log.d("weeklyDateTextView", "요일2: " + sdf.format(date1));
        saturdayTextView.setText(sdf.format(date1) + ", " + dayName);
        String satDate = sdf2.format(date1);
        initSatdayRecyclerView(setNewWeeklyJob(jobList, satDate));

    }

    private ArrayList<Job> setNewWeeklyJob(ArrayList<Job> jobList, String weekDate) {

        ArrayList<Job> newWeeklyJobList = new ArrayList<>();
        if (jobList.size() > 0) { //DB
            for (int i = 0; i < jobList.size(); i++) {
                String startDate = jobList.get(i).startDate;
//                Log.d("weekdayTextView", ": " + startDate + ": " + weekDate);
                if (startDate.equals(weekDate)) {
                    newWeeklyJobList.add(jobList.get(i));
                    for (int j = 0; j < newWeeklyJobList.size(); j++) {
//                        Log.d("weekdayTextView142", i + ": " + newWeeklyJobList.get(j).startDate + ", " + newWeeklyJobList.get(j).jobContents);
                    }
                }
//                Log.d("weekdayTextView145", ": " + newWeeklyJobList.size());
            }
        }
        return newWeeklyJobList;
    }

    private void calendarDeco() {
        // 오늘날짜 표기
        calendarView_weekly = findViewById(R.id.calendarView_Weekly);
        // 오늘꾸미기
        calendarView_weekly.addDecorators(new OneDayDecorator());
    }

    private void setDataListItems() {
        int image1 = R.drawable.star;
        jobList.add(new Job("TEST1123123123123123123123123", "2021/5/1", "testContents1", "INACTIVE"));
        jobList.add(new Job("TEST1", "2021/5/1", "testContents1", "INACTIVE", image1, loginId));
        jobList.add(new Job("TEST2", "2021/5/2", "testContents2", "ACTIVE", loginId));
        jobList.add(new Job("TEST2-2", "2021/5/2", "testContents2-2", "ACTIVE", loginId));
        jobList.add(new Job("TEST2-3", "2021/5/2", "testContents2-3", "ACTIVE", loginId));
        jobList.add(new Job("TEST2-4", "2021/5/2", "testContents2-4", "ACTIVE", loginId));
        jobList.add(new Job("TEST2-5", "2021/5/2", "testContents2-5", "ACTIVE", loginId));
        jobList.add(new Job("TEST2-6", "2021/5/2", "testContents2-6", "ACTIVE", loginId));
        jobList.add(new Job("TEST3", "2021/5/3", "testContents3", "COMPLETED", loginId));
        jobList.add(new Job("TEST4", "2021/5/3", "testContents4-1", "COMPLETED", loginId));
        jobList.add(new Job("TEST4", "2021/5/3", "testContents4-2", "COMPLETED", image1, loginId));
        jobList.add(new Job("TEST4", "2021/5/3", "testContents4-3", "COMPLETED", loginId));
        jobList.add(new Job("TEST4", "2021/5/3", "testContents4-4", "COMPLETED", loginId));
        jobList.add(new Job("TEST4", "2021/5/3", "testContents4-5", "COMPLETED", loginId));
        jobList.add(new Job("TEST4", "2021/5/3", "LocationA", "testContents4", "COMPLETED", loginId));
    }

//    private void initRecyclerView(ArrayList<Job> jobList) {
//        // 리사이클러뷰에 RecyclerAdapter 객체 지정.
//        WeeklyRecyclerAdapter jobAdapter = new WeeklyRecyclerAdapter(jobList);
//        weeklyRecyclerView = findViewById(R.id.weeklyRecyclerView);
//        weeklyRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)); //새로로 나열
//        weeklyRecyclerView.setAdapter(jobAdapter);
//    }

    private void initSundayRecyclerView(ArrayList<Job> jobList) {
        // 리사이클러뷰에 RecyclerAdapter 객체 지정.
        SundayRecyclerAdapter jobAdapter = new SundayRecyclerAdapter(jobList);
        sundayRecyclerView = findViewById(R.id.sundayRecyclerView);
        sundayRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)); //새로로 나열
        sundayRecyclerView.setAdapter(jobAdapter);
    }

    private void initMondayRecyclerView(ArrayList<Job> jobList) {
        // 리사이클러뷰에 RecyclerAdapter 객체 지정.
        MondayRecyclerAdapter jobAdapter = new MondayRecyclerAdapter(jobList);
        mondayRecyclerView = findViewById(R.id.mondayRecyclerView);
        mondayRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)); //새로로 나열
        mondayRecyclerView.setAdapter(jobAdapter);
    }

    private void initTuesdayRecyclerView(ArrayList<Job> jobList) {
        // 리사이클러뷰에 RecyclerAdapter 객체 지정.
        TuesdayRecyclerAdapter jobAdapter = new TuesdayRecyclerAdapter(jobList);
        tuesdayRecyclerView = findViewById(R.id.tuesdayRecyclerView);
        tuesdayRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)); //새로로 나열
        tuesdayRecyclerView.setAdapter(jobAdapter);
    }

    private void initWednesdayRecyclerView(ArrayList<Job> jobList) {
        // 리사이클러뷰에 RecyclerAdapter 객체 지정.
        WednesdayRecyclerAdapter jobAdapter = new WednesdayRecyclerAdapter(jobList);
        wednesRecyclerView = findViewById(R.id.wednesdayRecyclerView);
        wednesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)); //새로로 나열
        wednesRecyclerView.setAdapter(jobAdapter);
    }

    private void initThursdayRecyclerView(ArrayList<Job> jobList) {
        // 리사이클러뷰에 RecyclerAdapter 객체 지정.
        ThursdayRecyclerAdapter jobAdapter = new ThursdayRecyclerAdapter(jobList);
        thursdayRecyclerView = findViewById(R.id.thursdayRecyclerView);
        thursdayRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)); //새로로 나열
        thursdayRecyclerView.setAdapter(jobAdapter);
    }

    private void initFridayRecyclerView(ArrayList<Job> jobList) {
        // 리사이클러뷰에 RecyclerAdapter 객체 지정.
        FridayRecyclerAdapter jobAdapter = new FridayRecyclerAdapter(jobList);
        fridayRecyclerView = findViewById(R.id.fridayRecyclerView);
        fridayRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)); //새로로 나열
        fridayRecyclerView.setAdapter(jobAdapter);
    }

    private void initSatdayRecyclerView(ArrayList<Job> jobList) {
        // 리사이클러뷰에 RecyclerAdapter 객체 지정.
        SaturdayRecyclerAdapter jobAdapter = new SaturdayRecyclerAdapter(jobList);
        saturdayRecyclerView = findViewById(R.id.saturdayRecyclerView);
        saturdayRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)); //새로로 나열
        saturdayRecyclerView.setAdapter(jobAdapter);
    }
}