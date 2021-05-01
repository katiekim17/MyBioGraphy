package com.example.mybiography;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybiography.Recycler.RecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MemoActivity extends AppCompatActivity {

    public static final int AddMemoIdx = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    ArrayList<String> list;
    FloatingActionButton addBtn;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerAdapter recyclerAdapter;

    Toolbar memoToolBar; //<androidx.appcompat.widget.Toolbar사용했으므로 androidx에 있는 toolbar를 사용
    ActionBar actionBar;

    Button memoAddBtn;
    TextView memoTextView;

    Memo memo = new Memo();
    ArrayList<Memo> memoList = new ArrayList<>();

    private int mYear = 0, mMonth = 0, mDay = 0;
    // ArrayList -> Json으로 변환
    private static final String SETTINGS_PLAYER_JSON = "settings_item_json";

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

        //추가버튼
        memoAddBtn = findViewById(R.id.memoAddBtn);
        memoAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddmemoActivity.class);
                startActivityForResult(intent, AddMemoIdx); //액티비티 띄우기
            }
        });

//        memoList = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON);

        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        Intent addMemoIntent = getIntent();
        if(memo != null){
            memo = (Memo) addMemoIntent.getSerializableExtra("memo");
        }

        if (memo != null) {
            memoList.add(memo);
            memoList.add(memo);
            memoList.add(memo);
            memoList.add(memo);
            Log.d("MemoActivity85",String.valueOf(memoList.size()));
            for (int i = 0; i < memoList.size(); i++){
                Log.d("MemoActivity87",memoList.get(i).editMemoStr);
                Log.d("MemoActivity88",String.valueOf(memoList.size()));
            }
        }

        // 리사이클러뷰에 RecyclerAdapter 객체 지정.
        RecyclerAdapter adapter = new RecyclerAdapter(memoList);
        recyclerView = findViewById(R.id.memoRecyclerView);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        //LinearLayoutManager 수평(Horizontal) 또는 수직(Vertical) 방향, 일렬(Linear)로 아이템 뷰 배치
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)); //새로로 나열
        recyclerView.setAdapter(adapter);

    }

    private ArrayList<Memo> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList urls = new ArrayList();

        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    private void setMemoArrayPref(Context context, String key, ArrayList<Memo> memoList) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();

        for (int i = 0; i < memoList.size(); i++) {
            a.put(memoList.get(i));
        }
        if (!memoList.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "activity_MemoActivity_onResume 호출 됨", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "activity_MemoActivity_onPause 호출 됨", Toast.LENGTH_LONG).show();
        setMemoArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON, memoList);
        Log.d("MemoActivity","putJson" );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "activity_MemoActivity_onStop 호출 됨", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "activity_MemoActivity_onDestroy 호출 됨", Toast.LENGTH_LONG).show();
    }
}