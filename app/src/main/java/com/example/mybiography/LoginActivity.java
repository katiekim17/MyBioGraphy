package com.example.mybiography;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.util.Utility;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.AccessTokenInfo;
import com.kakao.sdk.user.model.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {

    public static final int loginIdx = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    private static final String TAG = "LoginActivity";

    EditText et_email, et_password;
    AlertDialog dialog;
    public String userName;
    public String loginCheck, loginId;
    CheckBox checkBox;
    TextView findPwBtn, sigUpBtn;
    ImageView profileImage;
    Button loginBtn;

    private View kakaoLoginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

//        startLoading();

        String getKeyHash = Utility.INSTANCE.getKeyHash(LoginActivity.this);
//        Log.d("kakaoLoginBtn", "getKeyHash : " + getKeyHash); //+cjyoqqr/O5X3ikPgJRJSYXstXA=


        //xml에서 값 불러오기
        et_email = findViewById(R.id.inputEmail);
        et_password = findViewById(R.id.inputPassword);
        checkBox = findViewById(R.id.checkBox);

        sigUpBtn = findViewById(R.id.sigUpBtn);
        findPwBtn = findViewById(R.id.findPwBtn);

        loginBtn = findViewById(R.id.loginBtn);
        kakaoLoginBtn = findViewById(R.id.kakao_loginBtn);
        profileImage = findViewById(R.id.profile);

        SharedPreferences pref = getSharedPreferences(SysCodes.KeyCodes.PREF.toString(), Activity.MODE_PRIVATE); //다른 액티비티에서 사용 가능
        loginCheck = pref.getString(SysCodes.KeyCodes.LOGIN_CHECK.toString(), null);
        userName = pref.getString(SysCodes.KeyCodes.USER_NAME.toString(), null);
        loginId = pref.getString(SysCodes.KeyCodes.LOGIN_ID.toString(), null);

        Log.d("LoginActivity38", "loginId: " + loginId);

        if (loginCheck != null) { //자동로그인으로 로그인페이지생략
            Toast.makeText(LoginActivity.this, userName + "님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();

//            saveLoginId(loginId);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(SysCodes.KeyCodes.USER_NAME.toString(), userName);
            intent.putExtra(SysCodes.KeyCodes.LOGIN_ID.toString(), loginId);
            startActivityForResult(intent, loginIdx); //액티비티 띄우기
        } else if (loginCheck == null) { //로그인페이지보여주기

            loginBtn.setOnClickListener(new View.OnClickListener() { //로그인버튼 클릭
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

                        ArrayList<Member> memList = new ArrayList<>();
                        //쉐어드프리퍼런스 불러오기
                        SharedPreferences memberPref = getSharedPreferences(SysCodes.KeyCodes.MEMBER.toString(), MODE_PRIVATE); //다른 액티비티에서 사용 가능
                        String prefMemberList = memberPref.getString(SysCodes.KeyCodes.MEMBER.toString(), null);
                        Log.d("LoginActivity91", "checkIdShared: " + prefMemberList);
                        Gson gson = new Gson();
                        if (prefMemberList != null) {
                            Type type = new TypeToken<ArrayList<Member>>() {
                            }.getType();
                            memList = gson.fromJson(prefMemberList, type);
                            Log.d("LoginActivity96", "prefMemberList: " + Arrays.asList(memList));
                        }

                        String checkId1 = et_email.getText().toString();
                        String checkPw1 = et_password.getText().toString();
                        Boolean isCheckIdDone = false;
                        Boolean isCheckPwDone = false;
                        for (int i = 0; i < memList.size(); i++) {
                            if (memList.get(i).loginId.equals(checkId1)) {
                                isCheckIdDone = true;
                                Log.d("LoginActivity96", "memList.oginId: " + memList.get(i).loginId + ", checkId1: " + checkId1);
                            }
                            if (memList.get(i).loginPw.equals(checkPw1)) {
                                isCheckPwDone = true;
                                Log.d("LoginActivity96", "memList.oginId: " + memList.get(i).loginPw + ", checkPw1: " + checkPw1);
                            }
                        }

                        Log.d("LoginActivity111", "isCheckIdDone :" + isCheckIdDone + ", isCheckPwDone: " + isCheckPwDone);
                        if (isCheckIdDone == true && isCheckPwDone == true) {
                            loginId = et_email.getText().toString();
                            userName = et_email.getText().toString();
                            //메인엑티비티로 이동
                            goToMainActivity();

                        } else {
                            String str = "아이디 혹은 비밀번호가 일치하지 않습니다";
                            showCheckIdDialog(str);
                        }
                    }
                }
            });

            kakaoLoginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickedKaKaoBtn();
                }
            });

            findPwBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), FindpwActivity.class);
                    startActivityForResult(intent, loginIdx); //액티비티 띄우기
                }
            });

            sigUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivityForResult(intent, loginIdx); //액티비티 띄우기
                }
            });
        }
    }

    private void goToMainActivity() {

        Log.d(TAG, "1userName? " + userName + ",loginId? " + loginId);
        //로그인성공
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        if (!loginId.isEmpty() && !userName.isEmpty()) {
            intent.putExtra(SysCodes.KeyCodes.LOGIN_ID.toString(), loginId);
            intent.putExtra(SysCodes.KeyCodes.USER_NAME.toString(), userName);
            Log.d(TAG, "2userName? " + userName + ",loginId? " + loginId);
        }
        saveLoginId(userName);

        Util.showMsgDialog(LoginActivity.this, "환영합니다");

        startActivityForResult(intent, loginIdx); //액티비티 띄우기

    }

    Function2<AccessTokenInfo, Throwable, Unit> callback = new Function2<AccessTokenInfo, Throwable, Unit>() {
        @Override
        public Unit invoke(AccessTokenInfo accessTokenInfo, Throwable throwable) {
            if (throwable != null) {
                Log.e(TAG, "토큰 정보 보기 실패", throwable);
            } else if (accessTokenInfo != null) {
                Log.i(TAG, "토큰 정보 보기 성공" +
                        "\n회원번호: "+ accessTokenInfo.getId() +
                        "\n만료시간: " + accessTokenInfo.getExpiresIn() +"초");

            }
            return null;
        }
    };

    private void clickedKaKaoBtn() {
        Log.d(TAG, "clickedKaKaoBtn");
        //토큰 존재 여부 확인하기
        if (AuthApiClient.getInstance().hasToken()) {
            Log.d(TAG, "토큰 존재 여부 확인");
            UserApiClient.getInstance().accessTokenInfo(callback);
            updateKakaoLoingUi();
        } else {
            Log.d(TAG, "토큰 없음");
            Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
                @Override
                public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                    Log.d("kakaoLoginBtn192", "oAuthToken : " + oAuthToken + ", throwable" + throwable);
                    if (oAuthToken != null) {
                        Util.showMsgDialog(LoginActivity.this, "로그인성공");
                        Log.d("kakaoLoginBtn195", "로그인성공");
                    }
                    if (throwable != null) {
                        Util.showMsgDialog(LoginActivity.this, "로그인실패");
                    }
                    updateKakaoLoingUi();
                    return null;
                }
            };

            //사용자 기기에 카카오톡이 설치되어 있음을 확인
            if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
                //카카오톡으로 로그인
                Log.d("kakaoLoginBtn", "카카오톡으로 로그인");
                UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, callback);
            } else { //계정없으면 웹 카카오계정으로 로그인
                Log.d("kakaoLoginBtn", "계정없으면 웹 카카오를 통해 로그인처리");
                UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, callback);
            }
        }

    }

    private void updateKakaoLoingUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                Log.i(TAG, "updateKakaoLoingUi239");
                if (user != null) {
                    Member member = new Member();
                    member.loginId = user.getKakaoAccount().getEmail();
                    member.userName = user.getKakaoAccount().getProfile().getNickname();
                    Log.d(TAG, "getHasSignedUp? " +user.getHasSignedUp());

                    Log.d("LoginActivty243", "loginId? " + member.loginId + ", userName? " + member.userName);
                    Util.saveMember(LoginActivity.this, member);
                    loginId = member.loginId;
                    userName = member.userName;
                    Log.d("LoginActivty245", "저장, loginId:" + loginId + ", userName :" + userName);
                    goToMainActivity();
//                    Glide.with(profileImage).load(user.getKakaoAccount().getProfile().getThumbnailImageUrl()).circleCrop().into(profileImage);
                } else {
                    Log.i(TAG, "updateKakaoLoingUi getMessage : " + throwable.getMessage());
                    Log.i(TAG, "updateKakaoLoingUi getCause" + throwable.getCause());
//                    profileImage.setImageBitmap(null);
                }
                return null;
            }
        });
    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish(); //현재 액티비티 종료
            }
        }, 2000);  // 화면에 Logo 2초간 보이기
    }

    private void showCheckIdDialog(String msgStr) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        dialog = builder.setMessage(msgStr)
                .setPositiveButton("확인", null)
                .create();
        dialog.show();
    }

    public void saveLoginId(String userName) {
        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
        SharedPreferences pref = getSharedPreferences(SysCodes.KeyCodes.PREF.toString(), Activity.MODE_PRIVATE); //다른 액티비티에서 사용 가능
        SharedPreferences.Editor editor = pref.edit();
        Log.d("LoginActivity288", "saveLoginId userName: " + userName + ", loginID : " + loginId);
        editor.putString(SysCodes.KeyCodes.USER_NAME.toString(), userName); // key, value를 이용하여 저장하는 형태
        editor.putString(SysCodes.KeyCodes.LOGIN_ID.toString(), loginId); // key, value를 이용하여 저장하는 형태

        if (checkBox.isChecked()) { //자동로그인체크되어있으면
            editor.putString(SysCodes.KeyCodes.LOGIN_CHECK.toString(), "OK"); // 로그인체크!
            Toast.makeText(LoginActivity.this, "로그인상태저장되었습니다.", Toast.LENGTH_SHORT).show();
        }
        editor.commit(); ////최종 커밋. 커밋을 해야 저장
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, "activity_login_onResume 호출 됨", Toast.LENGTH_LONG).show();
        //xml에서 값 불러오기
//        userName = et_email.getText().toString();
//        Log.v("text", "onResume:" + userName);
//        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
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
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences pref = getSharedPreferences(SysCodes.KeyCodes.PREF.toString(), Activity.MODE_PRIVATE);
        String loginChecked = pref.getString(SysCodes.KeyCodes.LOGIN_CHECK.toString(), "");
        Log.v(TAG, "onDestroy:" + loginChecked);
        if (!loginChecked.equals("OK")) {
            pref.edit().clear().commit(); //null로 자동로그인 되는것 방지
        }
    }

}