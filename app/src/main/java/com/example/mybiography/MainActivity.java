package com.example.mybiography;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final int CalIdx = 1002; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    public static final int DiaryIdx = 1003; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    public static final int MemoIdx = 1004; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    public static final int ReportIdx = 1005; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    public static final int GoogleTimerIdx = 1006; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    public static final int DayIdx = 1007; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    public static final int WeekendIdx = 1007; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    TextView nameDisp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent(); /*데이터 get*/
        String userName = intent.getExtras().getString("loginName");

        nameDisp = findViewById(R.id.mainNameDisp);
        nameDisp.setText(userName + "님 환영합니다");

        Button calBtn = findViewById(R.id.calBtn);
        calBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                intent.putExtra("userName", userName);
                startActivityForResult(intent, CalIdx); //액티비티 띄우기
            }
        });

        Button weekBtn = findViewById(R.id.weekBtn);
        weekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
                intent.putExtra("userName", userName);
                startActivityForResult(intent, WeekendIdx); //액티비티 띄우기
            }
        });

        Button dayBtn = findViewById(R.id.dayBtn);
        dayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddJobActivity.class);
                intent.putExtra("userName", userName);
                startActivityForResult(intent, DayIdx); //액티비티 띄우기
            }
        });

        Button googleTimerBtnBtn = findViewById(R.id.googleTimerBtn);
        googleTimerBtnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
                intent.putExtra("userName", userName);
                startActivityForResult(intent, GoogleTimerIdx); //액티비티 띄우기
            }
        });

        Button reportBtn = findViewById(R.id.reportBtn);
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
                intent.putExtra("userName", userName);
                startActivityForResult(intent, ReportIdx); //액티비티 띄우기
            }
        });

        Button memoBtn = findViewById(R.id.memoBtn);
        memoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
                intent.putExtra("userName", userName);
                startActivityForResult(intent, MemoIdx); //액티비티 띄우기
            }
        });


        Button diaryBtn = findViewById(R.id.diaryBtn);
        diaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
                intent.putExtra("userName", userName);
                startActivityForResult(intent, DiaryIdx); //액티비티 띄우기

            }
        });

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
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //앱나가기
//                      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                        String loginChecked = pref.getString("loginCheck", "");
                        Log.v("text", "logOut:" + loginChecked);
                        pref.edit().clear().commit(); //null로 자동로그인 되는것 방지
                        startActivity(intent);
                    }
                });

                AlertDialog alertDialog = builder.create(); alertDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, "onResume 호출 됨", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Toast.makeText(this, "onPause 호출 됨", Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Main_onDestroy 호출 됨", Toast.LENGTH_LONG).show();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        String loginChecked = pref.getString("loginCheck", "");
        Log.v("text", "main_onDestroy: " + loginChecked);

        if (!loginChecked.equals("OK")) {
            pref.edit().clear().commit(); //null로 자동로그인 되는것 방지
        }
    }

}