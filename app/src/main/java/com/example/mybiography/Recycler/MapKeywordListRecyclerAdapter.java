package com.example.mybiography.Recycler;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybiography.AddJobActivity;
import com.example.mybiography.MapKeywordResult;
import com.example.mybiography.R;

import java.util.ArrayList;

public class MapKeywordListRecyclerAdapter extends RecyclerView.Adapter<MapKeywordListRecyclerAdapter.ViewHolder> {
    //extends에 나오는 타입은 아무거나지정.

    private ArrayList<MapKeywordResult> mData;
    public static final int MapKeyWordRecylerActivityIdx = 10059; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView place_nameTextView, place_road_addrTextView;
        LinearLayout searchResultlayout;
        MapKeywordResult result = new MapKeywordResult();

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            // 뷰 객체에 대한 참조. (hold strong reference)
            searchResultlayout = itemView.findViewById(R.id.searchResultlayout);
            place_nameTextView = itemView.findViewById(R.id.place_name);
            place_road_addrTextView = itemView.findViewById(R.id.place_road_addr);

//            Log.d("KeyWordAdapter41", String.valueOf(mData.get(0).getPlace_name()));

            searchResultlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(place_nameTextView.getContext(), AddJobActivity.class);
                    intent.putExtra("keyResultClass", result);
                    intent.putExtra("fromSearchResult", true);
                    place_nameTextView.getContext().startActivity(intent); //액티비티 띄우기
                }
            });


        }

    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public MapKeywordListRecyclerAdapter(ArrayList<MapKeywordResult> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.keyword_itemview, null);
        return new ViewHolder(view, viewType);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수
        holder.result = mData.get(position);
        holder.place_nameTextView.setText(mData.get(position).getPlace_name());
        holder.place_road_addrTextView.setText(mData.get(position).getRoad_address_name());
//        Log.d("KeyWordRecycler82", String.valueOf(holder.result.getPlace_name()));
//        Log.d("KeyWordRecycler83", String.valueOf(mData.get(position).getRoad_address_name()));
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
//        Log.d("KeyWordAdapterSize", String.valueOf(mData.size()));
        return mData.size();
    }


}