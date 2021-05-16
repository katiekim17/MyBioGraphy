package com.example.mybiography;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class SignupActivity extends AppCompatActivity {

    public static final int SignupIdx = 10030; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    EditText et_makeId;
    EditText et_makeNickName, et_makePw, et_makePw2;
    Button makeAccountBtn, checkIdBtn;

    public Boolean idChecked = true;
    String makeIdStr, makeNickNameStr, makePwStr, makePw2Str;

    String key = SysCodes.KeyCodes.MEMBER.toString();
    ArrayList<Member> memList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_makeId = findViewById(R.id.makeId);
        et_makeNickName = findViewById(R.id.makeNickName);
        et_makePw = findViewById(R.id.makePw);
        et_makePw2 = findViewById(R.id.makePw2);
        checkIdBtn = findViewById(R.id.checkIdBtn);

        //쉐어드프리퍼런스 불러오기
        SharedPreferences memberPref = getSharedPreferences(key, MODE_PRIVATE); //다른 액티비티에서 사용 가능
        String prefMemberList = memberPref.getString(key, null);
        Log.d("signUpActivity49", "checkIdShared: " + prefMemberList);
        Gson gson = new Gson();
        if (prefMemberList != null) {
            Type type = new TypeToken<ArrayList<Member>>() {
            }.getType();
            memList = gson.fromJson(prefMemberList, type);
            Log.d("signUpActivity54", "prefMemberList: " + Arrays.asList(memList));
        }

        // Id비교처리
        checkIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIdStr = et_makeId.getText().toString();
                makeNickNameStr = et_makeNickName.getText().toString();
                makePwStr = et_makePw.getText().toString();
                makePw2Str = et_makePw2.getText().toString();

                Log.d("signUpActivity68", "prefMemberList: " + makeIdStr.length());

                String msgStr = "";
                idChecked = false;
                if (makeIdStr.length() == 0) {
                    msgStr = "아이디는 빈칸 일 수 없습니다.";
                    showCheckIdDialog(msgStr);
                } else if (makeIdStr.length() > 0) {
                    Log.d("signUpActivity75", "memberList_size: " + memList.size());
                    Boolean isSame = false;
                    String checkId = "";
                    for (int i = 0; i < memList.size(); i++) {
                        if(memList.get(i).loginId.equals(makeIdStr)){
                            checkId = memList.get(i).loginId;
                            isSame = true;
                        }
                    }
                    Log.d("signUpActivity84", "isSame: " + isSame + ", makeIdStr " + makeIdStr);
                    if (isSame == true && checkId.equals(makeIdStr)) {
                        msgStr = "이미 등록된 아이디입니다";
                        showCheckIdDialog(msgStr);
                        idChecked = false;
                    } else if(memList.size() == 0 || isSame == false) {
                        Log.d("signUpActivity82", "memberList_size: " + memList.size());
                        msgStr = "등록가능한 아이디입니다.";
                        showCheckIdDialog(msgStr);
                        idChecked = true;
                    }
                }
            }
        });

        Log.d("signUpActivity96", "idChecked: " + idChecked);
        //비밀번호 확인
        makeAccountBtn = findViewById(R.id.makeAccountBtn);
        makeAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIdStr = et_makeId.getText().toString();
                makeNickNameStr = et_makeNickName.getText().toString();
                makePwStr = et_makePw.getText().toString();
                makePw2Str = et_makePw2.getText().toString();

                Boolean isGoOn = true;
//                idChecked = true;
                Log.d("signUpActivity107", "isGoOn: " + isGoOn + ", idChecked: " + idChecked);
                //비밀번호 확인
                if (!makePwStr.equals(makePw2Str)) {
                    String str = "비밀번호가 일치하지 않습니다";
                    showCheckIdDialog(str);
                    isGoOn = false;
                }
                Log.d("signUpActivity114", "isGoOn: " + isGoOn + ", idChecked: " + idChecked);
                if (idChecked == false && makeIdStr.isEmpty()  ||  (!makeIdStr.isEmpty() && idChecked == false )) {
                    String str = "아이디체크를 해주세요";
                    showCheckIdDialog(str);
                    isGoOn = false;
                } else if (makeIdStr.isEmpty() || makeNickNameStr.isEmpty() || makePwStr.isEmpty() || makePw2Str.isEmpty()) {
                    String str = "모두 기입해주세요";
                    showCheckIdDialog(str);
                } else if (isGoOn == true && idChecked == true && !makeIdStr.isEmpty() && !makeNickNameStr.isEmpty() && !makePwStr.isEmpty() && !makePw2Str.isEmpty()) {
                    //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                    SharedPreferences memberPref = getSharedPreferences(key, MODE_PRIVATE); //다른 액티비티에서 사용 가능
                    SharedPreferences.Editor editor = memberPref.edit();
                    Log.d("LoginActivity95", "userName: " + makeIdStr + ", " + makeNickNameStr + ", " + makePwStr + ", " + makePw2Str);

                    Member member = new Member();

                    member.loginId = makeIdStr;
                    member.userName = makeNickNameStr;
                    member.loginPw = makePwStr;

                    if (memList.size() > 0) {
                        memList.add(memList.size(), member);
                    } else memList.add(0, member);

                    //쉐어드프리퍼런스에 저장1
                    String json = "";
                    Gson gson = new Gson();
                    json = gson.toJson(memList);
                    Log.d("MemberListActivity101", "MemberListJson? " + json);
                    SharedPreferences.Editor prefEditor = memberPref.edit();
                    prefEditor.putString(key, json);
                    prefEditor.commit();//최종 커밋. 커밋을 해야 저장

                    //쉐어드프리퍼런스에 저장2
                    SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE); //다른 액티비티에서 사용 가능
                    SharedPreferences.Editor editor2 = pref.edit();
                    Log.d("LoginActivity95", "userName: "+member.userName + ", id : " + member.userName);
                    editor2.putString(SysCodes.KeyCodes.USER_NAME.toString(),  member.userName); // key, value를 이용하여 저장하는 형태
                    editor2.putString(SysCodes.KeyCodes.LOGIN_ID.toString(),  member.loginId); // key, value를 이용하여 저장하는 형태
                    editor2.commit(); ////최종 커밋. 커밋을 해야 저장


                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    intent.putExtra("loginName", member.loginName);
                    Toast.makeText(SignupActivity.this, "가입성공. 메인메뉴로 이동", Toast.LENGTH_SHORT).show();
                    startActivityForResult(intent, SignupIdx); //액티비티 띄우기
                }
            }
        });


    }

    private void showCheckIdDialog(String msgStr) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);

        dialog = builder.setMessage(msgStr)
                .setPositiveButton("확인", null)
                .create();
        dialog.show();
        idChecked = false;
    }


}