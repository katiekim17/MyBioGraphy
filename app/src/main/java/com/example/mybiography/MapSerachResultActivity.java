package com.example.mybiography;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybiography.KakaoData.ResultSearchKeyword;
import com.example.mybiography.Recycler.MapKeywordListRecyclerAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapSerachResultActivity extends AppCompatActivity {

    private final String TAG = "MapSerachResultActivity";
    private final String BASE_URL = "https://dapi.kakao.com/"; //TODO 마지막 slash 주의!
    private final String API_KEY = "KakaoAK 4c84eaebf3871119b12421d22dc3cece"; // REST API 키

    EditText search;
    ImageView searchBtn;
    RecyclerView searchRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_serach_result);

        search = findViewById(R.id.search);
        searchBtn = findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search.length() == 0) {
                    Util.showMsgDialog(MapSerachResultActivity.this, "검색어를 입력하세요.");
                } else {
                    String searchVal = search.getText().toString();
                    searachKeyWord(searchVal);
                }

            }
        });
    }

    private void searachKeyWord(String searchVal) {
        try {
            Log.d("KAKAO_searchVal", searchVal);
            int page = 1;

            Retrofit retrofit = new Retrofit.Builder() // Retrofit 구성
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            KakaoAPI kakaoAPI = retrofit.create(KakaoAPI.class); // 통신 인터페이스를 객체로 생성
            Log.d("KAKAO_searchVal", kakaoAPI.getSearchKeyword(API_KEY,searchVal).request().toString());

            kakaoAPI.getSearchKeyword(API_KEY, searchVal).enqueue(new Callback<ResultSearchKeyword>() {
                @Override
                public void onResponse(Call<ResultSearchKeyword> call, Response<ResultSearchKeyword> response) {
                    if(response.isSuccessful()){
                        ResultSearchKeyword data = response.body();
                        Log.d("KAKAO2", String.valueOf(data));
                        dispSearchResult(data);
                    }
                }

                @Override
                public void onFailure(Call<ResultSearchKeyword> call, Throwable t) {
                    Log.d("KAKAO3",  "통신 실패:"+t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

        private void dispSearchResult(ResultSearchKeyword data) {
            Log.d("KAKAO3",  "getTotal_count:"+data.getMeta().getTotal_count());
            if(!data.getDocuments().isEmpty()){
                ArrayList<MapKeywordResult> resultList = new ArrayList<>();
                for (int i = 0; i < data.getDocuments().size(); i++){
                    MapKeywordResult mapResult = new MapKeywordResult();
                    mapResult.setPlace_name(data.getDocuments().get(i).getPlace_name());
                    mapResult.setRoad_address_name(data.getDocuments().get(i).getRoad_address_name());
                    mapResult.setX(data.getDocuments().get(i).getX());
                    mapResult.setY(data.getDocuments().get(i).getY());
                    resultList.add(mapResult);
//                    Log.d("KAKAO3",  "1:"+resultList.get(i).getRoad_address_name());
                }
                searchResultRecyclerView(resultList);
            }

    }
    private void searchResultRecyclerView(ArrayList<MapKeywordResult> list) {
        // 리사이클러뷰에 RecyclerAdapter 객체 지정.
        searchRecyclerView = findViewById(R.id.searchResultRecyclerView);

        MapKeywordListRecyclerAdapter adapter = new MapKeywordListRecyclerAdapter(list);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)); //새로로 나열
        searchRecyclerView.setAdapter(adapter);
    }
}