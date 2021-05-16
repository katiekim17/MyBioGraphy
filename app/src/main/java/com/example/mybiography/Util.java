package com.example.mybiography;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class Util {

    private static final String TAG = "Util";

    public static String getCalendar() {
        Calendar calendar = Calendar.getInstance();
        String DATE_FROMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FROMAT);
        String dateStr = dateFormat.format(calendar.getTime());

        Log.d("JOB Class", "getCalendar2: " + dateStr);
        return dateStr;
    }

    public static void showMsgDialog(Context context, String msgStr) {
        Toast.makeText(context, msgStr, Toast.LENGTH_SHORT).show();
    }

    public static void loadSharedDB(Context context, String key, ArrayList<Member> memList) {
        //쉐어드프리퍼런스 불러오기
        SharedPreferences memberPref = context.getSharedPreferences(key, MODE_PRIVATE); //다른 액티비티에서 사용 가능
        String prefMemberList = memberPref.getString(key, null);
        Log.d("signUpActivity49", "checkIdShared: " + prefMemberList);
        Gson gson = new Gson();
        if (prefMemberList != null) {
            Type type = new TypeToken<ArrayList<Member>>() {
            }.getType();
            memList = gson.fromJson(prefMemberList, type);
            Log.d("signUpActivity54", "prefMemberList: " + Arrays.asList(memList));
        }
    }

//    public static void updateKakaoLoingUi() {
//        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
//            @Override
//            public Unit invoke(User user, Throwable throwable) {
//                Log.i(TAG, "updateKakaoLoingUi239");
//                if (user != null) {
//                    Member member = new Member();
//                    member.loginId = user.getKakaoAccount().getEmail();
//                    member.loginName = user.getKakaoAccount().getProfile().getNickname();
//
//                    Log.d("LoginActivty243", "loginId? " + member.loginId + ", userName? " + member.loginName);
//                    Util.saveMember(LoginActivity.this, member);
//                    loginId = member.loginId;
//                    userName = member.loginName;
//                    Log.d("LoginActivty245", "저장, loginId:" + loginId + ", userName :" + userName);
//                    goToMainActivity();
////                    Glide.with(profileImage).load(user.getKakaoAccount().getProfile().getThumbnailImageUrl()).circleCrop().into(profileImage);
//                } else {
//                    Log.i(TAG, "updateKakaoLoingUi getMessage : " + throwable.getMessage());
//                    Log.i(TAG, "updateKakaoLoingUi getCause" + throwable.getCause());
////                    profileImage.setImageBitmap(null);
//                }
//                return null;
//            }
//        });
//    }

    public static void saveMember(Context context, Member member) {

        if (member.loginId.isEmpty() || member.loginId == "") {
            Log.d("Util_54", "저장실패_member: " + member.toString());
        } else {
            Log.d("Util_55", "saveMember");
            ArrayList<Member> memList = new ArrayList<>();
            memList.add(member);
            //쉐어드프리퍼런스 불러오기
            String key = "member";
            SharedPreferences memberPref = context.getSharedPreferences(key, MODE_PRIVATE); //다른 액티비티에서 사용 가능
            //쉐어드프리퍼런스에 저장1
            String json = "";
            Gson gson = new Gson();
            json = gson.toJson(memList);
            Log.d("Util_59", "MemberListJson? " + json);
            SharedPreferences.Editor prefEditor = memberPref.edit();
            prefEditor.putString(key, json);
            prefEditor.commit();//최종 커밋. 커밋을 해야 저장

            //쉐어드프리퍼런스에 저장2
            SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE); //다른 액티비티에서 사용 가능
            SharedPreferences.Editor editor2 = pref.edit();
            Log.d("Util_67", "userName: " + member.userName + ", id : " + member.userName);
            editor2.putString("loginName", member.userName); // key, value를 이용하여 저장하는 형태
            editor2.putString("loginId", member.loginId); // key, value를 이용하여 저장하는 형태
            editor2.commit(); ////최종 커밋. 커밋을 해야 저장
        }
    }

}


