package com.example.mybiography;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybiography.Recycler.MemoRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MemoActivity extends AppCompatActivity {

    //    public static final int AddMemoIdx = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    public static final int MemoIdx = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    FloatingActionButton addBtn;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    Toolbar memoToolBar; //<androidx.appcompat.widget.Toolbar사용했으므로 androidx에 있는 toolbar를 사용
    ActionBar actionBar;

    Button memoAddBtn;
    TextView memoTextView;

    public static ArrayList<Memo> memoList = new ArrayList<>();
    ArrayList<Memo> newMemoList = new ArrayList<>();

    private int mYear = 0, mMonth = 0, mDay = 0;
    // ArrayList -> Json으로 변환
    private static final String SETTINGS_PLAYER_JSON = "settings_item_json";
//    String key = "memoList";
    String memberKey = SysCodes.KeyCodes.MEMBER.toString();
    String key = SysCodes.KeyCodes.MEMOLIST.toString();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        //ToolBar create
        memoToolBar = findViewById(R.id.weekend_toolbar);
        setSupportActionBar(memoToolBar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목삭제
        actionBar.setDisplayHomeAsUpEnabled(false); //자동으로 뒤로가기 버튼을 만들어줍니다.

        //인텐트 userId
        Intent intent = getIntent(); /*데이터 get*/
        String loginId = "";
        if (intent != null && intent.hasExtra(SysCodes.KeyCodes.LOGIN_ID.toString())) {
            loginId = intent.getExtras().getString(SysCodes.KeyCodes.LOGIN_ID.toString());
            Log.d("MemoActivity85", "loginId: " + loginId);
        }

        String userId = loginId;
        Log.d("MemoActivity74", "userId: " + userId + ",loginId: " + loginId);

        //Member쉐어드 프리퍼런스 불러오기
        userId = getMemberId(userId);
        Log.d("MemoActivity78", "userId: " + userId + ",userId: " + userId);

        String intentUserId = loginId; //인텐트에 넣을 변수. 다른곳에 쓰면 안됨.

        //추가버튼
        memoAddBtn = findViewById(R.id.memoAddBtn);
        memoAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddmemoActivity.class);
                Log.d("MemoActivity69", String.valueOf(memoList.size()));
                if (memoList.size() == 0) {
                    intent.putExtra("memoId", 1);
                    intent.putExtra(SysCodes.KeyCodes.LOGIN_ID.toString(), intentUserId);
                } else {
                    intent.putExtra("memoId", memoList.size() + 1);
                    intent.putExtra(SysCodes.KeyCodes.LOGIN_ID.toString(), intentUserId);
                }
                startActivityForResult(intent, MemoIdx); //액티비티 띄우기
            }
        });


        //Memo쉐어드 프리퍼런스 불러오기
        SharedPreferences memoPrefs = getSharedPreferences(key, Activity.MODE_PRIVATE); //"memoList";
        String memoStr = memoPrefs.getString(SysCodes.KeyCodes.MEMOLIST.toString(), "No saved data");
        Log.d("checkRecover105", "tempJson2? " + memoStr.toString());
        Gson gson = new Gson();
        if (!memoStr.equals("No saved data")) {
            Type type = new TypeToken<ArrayList<Memo>>() {
            }.getType();
            memoList = gson.fromJson(memoStr, type);

            Log.d("checkRecover112", "memo.writerId? " + userId);

            //메인->메모로 들어왔을때 검색해서 새로 담아줌
//            for (int i = 0; i < memoList.size(); i++) {
//                if (memoList.get(i).writerId.equals(userId)) {
//                    newMemoList.add(memoList.get(i));
//                }
//            }
        }


        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        Memo memo = new Memo();
        Intent addMemoIntent = getIntent();
        if (addMemoIntent != null && addMemoIntent.hasExtra("memo")) {
            Log.d("checkRecover117", "addMemoIntent 리사이클러뷰에 표시할 데이터 리스트 생성.");

            memo = (Memo) addMemoIntent.getSerializableExtra("memo");
            Boolean isUpdated = addMemoIntent.getExtras().getBoolean("memo_isUpdate");
            Boolean isDelete = addMemoIntent.getExtras().getBoolean("memo_isDelete");

            //수정
            if (isUpdated == true) {
                for (int i = 0; i < memoList.size(); i++) {
                    if (memoList.get(i).id == memo.id && memoList.get(i).writerId.equals(memo.writerId)) {
                        memoList.set(i, memo);
                    }
                }
                //삭제
            } else if (isDelete == true) {
                for (int i = 0; i < memoList.size(); i++) {
                    if (memoList.get(i).id == memo.id && memoList.get(i).writerId.equals(memo.writerId)) {
                        memoList.remove(i);
                    }
                }
            } else {
                Log.d("checkRecover137209", "memo.writerId? " + memo.writerId);
                memoList.add(memo);
            }
        }

        //쉐어드프리퍼런스에 저장
        String json = "";
        json = gson.toJson(memoList);
        Log.d("MemoActivity156", "쉐어드프리퍼런스에 저장 MemoJson? " + json);
        SharedPreferences.Editor prefEditor = memoPrefs.edit();
        prefEditor.putString(key, json);
        prefEditor.commit();

        Log.d("checkRecover162", "memo.writerId? " + memo.writerId);
        Log.d("checkRecover163", "userId? " +userId);
        if (userId != null) {
            for (int i = 0; i < memoList.size(); i++) {
                if (memoList.get(i).writerId.equals(userId)) {
                    newMemoList.add(memoList.get(i));
                    for (int k = 0; k < newMemoList.size(); k++){
                        Log.d("checkRecover167", "memoList? " + newMemoList.get(k).id + ", title : " + newMemoList.get(k).titleStr);
                    }
                }
            }
        }

        // 리사이클러뷰에 RecyclerAdapter 객체 지정.
        MemoRecyclerAdapter adapter = new MemoRecyclerAdapter(newMemoList);
        recyclerView = findViewById(R.id.memoRecyclerView);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        //LinearLayoutManager 수평(Horizontal) 또는 수직(Vertical) 방향, 일렬(Linear)로 아이템 뷰 배치
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)); //새로로 나열
        recyclerView.setAdapter(adapter);

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
        Log.d("MemoActivity176", "memberId: " + userId);
        return userId;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Log.d("MemoActivity160_back", "onBackPressed");
        startActivityForResult(intent, MemoIdx); //액티비티 띄우기
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}