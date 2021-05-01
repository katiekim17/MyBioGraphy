package com.example.mybiography.Recycler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybiography.AddmemoActivity;
import com.example.mybiography.Memo;
import com.example.mybiography.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
 //extends에 나오는 타입은 아무거나지정.

    private ArrayList<Memo> mData = new ArrayList<>();
    public static final int MemoRecylerActivityIdx = 10055; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView memoTextView, memoDateTextView;
        EditText editMemoTitle, editMemoText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 뷰 객체에 대한 참조. (hold strong reference)
            memoTextView = itemView.findViewById(R.id.memoTextView) ;
            memoDateTextView = itemView.findViewById(R.id.memoDateTextView);
            editMemoTitle = itemView.findViewById(R.id.editMemoTitle);
            editMemoText = itemView.findViewById(R.id.editMemoText);

            memoTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(memoTextView.getContext(), AddmemoActivity.class);
                    String memoTitleStr = memoTextView.getText().toString();
                    String editMemoTextStr = null;
                    Log.d("RecyclerAdapter47", String.valueOf(memoTitleStr));
                        editMemoTextStr = editMemoText.getText().toString();
                    intent.putExtra("memoClass", mData);
                    memoTextView.getContext().startActivity(intent); //액티비티 띄우기
                }
            });

        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public RecyclerAdapter(ArrayList<Memo> list){
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.memorecycler_itemview, parent, false);
        RecyclerAdapter.ViewHolder rVh = new RecyclerAdapter.ViewHolder(view);
        return rVh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수
        Log.d("RecyclerAdapter77", String.valueOf(position));
        String titleStr = mData.get(position).titleStr;
        Log.d("RecyclerAdapter79", titleStr);
        String date= mData.get(position).writeDate;
        Log.d("RecyclerAdapter81", date);
        holder.memoTextView.setText(titleStr);
        holder.memoDateTextView.setText(date);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        Log.d("getItemCount", String.valueOf(mData.size()));
        return mData.size();
    }
}