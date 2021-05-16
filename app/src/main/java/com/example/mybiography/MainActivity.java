package com.example.mybiography;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kakao.sdk.user.UserApiClient;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final int CalIdx = 1002; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    public static final int DiaryIdx = 1003; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    public static final int MemoIdx = 1004; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    public static final int ReportIdx = 1005; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    public static final int GoogleTimerIdx = 1006; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    public static final int DayIdx = 1007; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    public static final int WeekendIdx = 1007; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    public static final int MainIdx = 1008; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    TextView nameDisp ,jobListBannerText;
    String key = SysCodes.KeyCodes.JOBLIST.toString();

    ArrayList<Job> newjobListById = new ArrayList<>();
    ArrayList<Job> pageJobList = new ArrayList<>();
    Job job = new Job();

    private ViewPager2 viewPager2;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    public String loginName;
    public String loginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences(String.valueOf(SysCodes.KeyCodes.PREF), Activity.MODE_PRIVATE);
        loginId = pref.getString(SysCodes.KeyCodes.LOGIN_ID.toString(), null);
        loginName = pref.getString(SysCodes.KeyCodes.USER_NAME.toString(), null);

        Intent intent = getIntent(); /*데이터 get*/
        if (intent != null && intent.hasExtra(SysCodes.KeyCodes.USER_NAME.toString())) {
            loginName = intent.getExtras().getString(SysCodes.KeyCodes.USER_NAME.toString());
        }
        if (intent != null && intent.hasExtra(SysCodes.KeyCodes.LOGIN_ID.toString())) {
            loginId = intent.getExtras().getString(SysCodes.KeyCodes.LOGIN_ID.toString());
        }
        String userName = loginName; //인텐트용
        String userId = loginId; //인텐트용
        Log.d("MainActivty117", "loginId? " + loginId );

        nameDisp = findViewById(R.id.mainNameDisp);
        nameDisp.setText(userName + "님 환영합니다");

        Button calBtn = findViewById(R.id.calBtn);
        calBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                intent.putExtra(SysCodes.KeyCodes.LOGIN_ID.toString(), userId);
                intent.putExtra(SysCodes.KeyCodes.USER_NAME.toString(), userName);
                startActivityForResult(intent, CalIdx); //액티비티 띄우기
            }
        });

        Button weeklyBtn = findViewById(R.id.weeklyBtn);
        weeklyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WeeklyActivity.class);
                intent.putExtra(SysCodes.KeyCodes.USER_NAME.toString(), userName);
                intent.putExtra(SysCodes.KeyCodes.LOGIN_ID.toString(), userId);
                startActivityForResult(intent, WeekendIdx); //액티비티 띄우기
            }
        });

        Button dayBtn = findViewById(R.id.dayBtn);
        dayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddJobActivity.class);
                intent.putExtra(SysCodes.KeyCodes.USER_NAME.toString(), userName);
                intent.putExtra(SysCodes.KeyCodes.LOGIN_ID.toString(), userId);
                //새로 생성하는 것이므로 쉐어드에서 isWriting = true로 바꿔줌.
                SharedPreferences setIsWriting = getSharedPreferences("tempSaveJob", MODE_PRIVATE);
                setIsWriting.edit().putBoolean("isWriting", true).commit();
                startActivityForResult(intent, DayIdx); //액티비티 띄우기
            }
        });

        Button memoBtn = findViewById(R.id.memoBtn);
        memoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
                intent.putExtra(SysCodes.KeyCodes.USER_NAME.toString(), userName);
                intent.putExtra(SysCodes.KeyCodes.LOGIN_ID.toString(), userId);
                startActivityForResult(intent, MemoIdx); //액티비티 띄우기
            }
        });

//        Button googleTimerBtnBtn = findViewById(R.id.googleTimerBtn);
//        googleTimerBtnBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
//                intent.putExtra(SysCodes.KeyCodes.USER_NAME.toString(), userName);
//                intent.putExtra(SysCodes.KeyCodes.LOGIN_ID.toString(), userId);
//                startActivityForResult(intent, GoogleTimerIdx); //액티비티 띄우기
//            }
//        });

//        Button reportBtn = findViewById(R.id.reportBtn);
//        reportBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
//                intent.putExtra("userName", userName);
//                startActivityForResult(intent, ReportIdx); //액티비티 띄우기
//            }
//        });


//        Button diaryBtn = findViewById(R.id.diaryBtn);
//        diaryBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
//                intent.putExtra("userName", userName);
//                startActivityForResult(intent, DiaryIdx); //액티비티 띄우기
//
//            }
//        });

        Button logOutBtn = findViewById(R.id.logOutBtn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("로그아웃 하시겠습니까?");
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Yeah!!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "ByeBye~", Toast.LENGTH_SHORT).show();

                        //logOut

                        UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                if (throwable != null) {
                                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", throwable);
                                }
                                else {
                                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨");
                                }
                                return null;
                            }
                        });

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //앱나가기
//                      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        // 로그인화면으로 나가기
                        startActivityForResult(intent, MainIdx); //액티비티 띄우기

                        //로그아웃하면 다 지워버리기
                        SharedPreferences pref = getSharedPreferences(SysCodes.KeyCodes.PREF.toString(), Activity.MODE_PRIVATE);
                        String loginChecked = pref.getString(SysCodes.KeyCodes.LOGIN_CHECK.toString(), "");
                        Log.v("text", "logOut:" + loginChecked);
                        pref.edit().clear().commit(); //null로 자동로그인 되는것 방지
                        startActivity(intent);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        //인텐트 userId, userName
//        Log.d("MainActivity198", "userId: " + userId + ",loginId: " + loginId);
        jobListBannerText = findViewById(R.id.jobListBannerText);
//        Log.d("MainActivity200", jobListBannerText.getText().toString()); //다가오는일정

        //쉐어드프리퍼런스 불러오기 -> jobList에 일단 넣어주기
        SharedPreferences jobListPref = getSharedPreferences(key, MODE_PRIVATE);
        String jobListStr2 = jobListPref.getString(key, "No saved data");
        Log.d("checkRecover1", "jobListJson1? " + jobListStr2.toString());
        Gson gson = new Gson();

        if (!jobListStr2.equals("No saved data")) {
            Type type = new TypeToken<ArrayList<Job>>() {
            }.getType();
            pageJobList = gson.fromJson(jobListStr2, type);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            String today = sdf.format(date);
//            Log.d("checkRecover203", "today? " + today);

            for (int i = 0; i < pageJobList.size(); i++) {
                //userID인것만 새로담기
                if (pageJobList.get(i).loginId.equals(loginId)) {
                    if (today.equals(pageJobList.get(i).startDate)) {
//                        Log.d("MainActivity78", "jobList.loginId? " + pageJobList.get(i).loginId + ", loginId: " + loginId);
                        newjobListById.add(pageJobList.get(i));
                    }
                }
            }

            //다시 새로 넣기
            pageJobList = newjobListById;

            //다가오는 일정 갯수 삽입
            jobListBannerText.setText("오늘 일정 (" + String.valueOf(pageJobList.size()) + ")");
        }

        // viewPager
        viewPager2 = findViewById(R.id.jobListViewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(pageJobList);
        viewPager2.setAdapter(adapter);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == 9) {
                    currentPage = 0;
                }
                viewPager2.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences pref = getSharedPreferences(SysCodes.KeyCodes.PREF.toString(), Activity.MODE_PRIVATE);
        String loginChecked = pref.getString(SysCodes.KeyCodes.LOGIN_CHECK.toString(), "");
        Log.v("text", "main_onDestroy: " + loginChecked);

        if (!loginChecked.equals("OK")) {
            pref.edit().clear().commit(); //null로 자동로그인 되는것 방지
        }
    }

    private long backKeyPressedTime = 0;
    private Toast toast = null;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
//            toast.makeText(getApplicationContext(), "한번 더 누르시면 종료합니다", Toast.LENGTH_SHORT).show();
            Util.showMsgDialog(MainActivity.this,"한번 더 누르시면 종료합니다");
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            MainActivity.this.moveTaskToBack(true);
            MainActivity.this.finish();
            System.exit(0);
        }
    }

    //Member쉐어드 프리퍼런스 불러오기
    public String getMemberId(String userId) {

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
        Log.d("MainActivity189", "memberId: " + userId);
        return userId;
    }

}