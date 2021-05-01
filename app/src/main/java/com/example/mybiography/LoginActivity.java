package com.example.mybiography;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    public static final int loginIdx = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    EditText et_email, et_password;
    AlertDialog dialog;
    String userName;
    String loginCheck, loginName;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Toast.makeText(this, "ogin_onCreate 호출 됨", Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_login);

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        loginCheck = pref.getString("loginCheck", null);
        loginName = pref.getString("loginName", null);

        Button btn = findViewById(R.id.loginBtn);

        if (pref == null) {
            String name = pref.getString("loginName", "없음");  // name이 없으면  "" 를 달라
            Toast.makeText(LoginActivity.this, "복구된 이름 " + name, Toast.LENGTH_LONG).show();
        }

        if (loginCheck != null) { //자동로그인으로 로그인페이지생략
            Toast.makeText(LoginActivity.this, loginName + "님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("loginName", loginName);
            startActivityForResult(intent, loginIdx); //액티비티 띄우기
        } else if (loginCheck == null) { //로그인페이지보여주기

            //xml에서 값 불러오기
            et_email = findViewById(R.id.inputEmail);
            et_password = findViewById(R.id.inputPassword);
            checkBox = findViewById(R.id.checkBox);

            btn.setOnClickListener(new View.OnClickListener() { //로그인버튼 클릭
                @Override
                public void onClick(View v) {
                    //아이디빈칸체크
                    if (et_email.length() == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        dialog = builder.setMessage("아이디는 빈칸 일 수 없습니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    }
                    //아이디빈칸 아니면 로그인가능
                    if (et_email.length() > 0) {
                        userName = et_email.getText().toString();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("loginName", userName);

                        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                        SharedPreferences.Editor editor = pref.edit();
                        String userName = et_email.getText().toString(); // 사용자가 입력한 저장할 데이터
                        editor.putString("loginName", userName); // key, value를 이용하여 저장하는 형태

                        if (checkBox.isChecked()) { //자동로그인체크되어있으면
                            editor.putString("loginCheck", "OK"); // 로그인체크!
                            Toast.makeText(LoginActivity.this, "로그인상태저장되었습니다.", Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(LoginActivity.this, "환영합니다", Toast.LENGTH_LONG).show();
                        editor.commit();

                        startActivityForResult(intent, loginIdx); //액티비티 띄우기
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, "activity_login_onResume 호출 됨", Toast.LENGTH_LONG).show();
        //xml에서 값 불러오기
//        userName = et_email.getText().toString();
        Log.v("text", "onResume:" + userName);
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
////        if(userName == null || userName == ""){
//            Log.v("text", "onResume1:" + userName);
//            pref.edit().remove("loginName").commit();
//        }
//        if (userName != null) {
//            Log.v("text", "onResume2:" + userName);
//            if (pref != null) {
//                String name = pref.getString("loginName", "없음");  // name이 없으면  "" 를 달라
//                if (!name.equals("없음")) {
//                    Toast.makeText(LoginActivity.this, "복구된 이름: " + name, Toast.LENGTH_LONG).show();
//                }
//            }
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Toast.makeText(this, "activity_login_onStop 호출 됨",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        String loginChecked = pref.getString("loginCheck", "");
        Log.v("text", "onDestroy:" + loginChecked);
        if (!loginChecked.equals("OK")) {
            pref.edit().clear().commit(); //null로 자동로그인 되는것 방지
        }
        Toast.makeText(this, "activity_login_onDestroy 호출 됨" + loginChecked, Toast.LENGTH_LONG).show();
//        pref.edit().remove("loginCheck").commit();
    }

}