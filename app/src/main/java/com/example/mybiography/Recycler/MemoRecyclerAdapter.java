package com.example.mybiography.Recycler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybiography.AddmemoActivity;
import com.example.mybiography.Memo;
import com.example.mybiography.R;

import java.util.ArrayList;

public class MemoRecyclerAdapter extends RecyclerView.Adapter<MemoRecyclerAdapter.ViewHolder> {
    //extends에 나오는 타입은 아무거나지정.

    private ArrayList<Memo> mData = new ArrayList<>();
    private Context mContext;
    public static final int MemoRecylerActivityIdx = 10055; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView memoTextView, memoDateTextView;
        Memo memo = new Memo();
        int delPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 뷰 객체에 대한 참조. (hold strong reference)
            memoTextView = itemView.findViewById(R.id.memoTextView);
            memoDateTextView = itemView.findViewById(R.id.memoDateTextView);

            memoTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String memoTitleStr = memoTextView.getText().toString();
                    Intent intent = new Intent(memoTextView.getContext(), AddmemoActivity.class);
                    intent.putExtra("memoClass", memo);
                    intent.putExtra("memoClass_isUpdate", true);
                    memoTextView.getContext().startActivity(intent); //액티비티 띄우기
                }
            });

            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "편집");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1001: //편집
                        Intent intent = new Intent(memoTextView.getContext(), AddmemoActivity.class);
                        intent.putExtra("memoClass", memo);
                        intent.putExtra("memoClass_isUpdate", true);
                        memoTextView.getContext().startActivity(intent); //액티비티 띄우기
                        break;
                    case 1002: //삭제
                        mData.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), mData.size());
                        break;
                }
                return true;
            }
        };
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public MemoRecyclerAdapter(ArrayList<Memo> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.memorecycler_itemview, parent, false);
        MemoRecyclerAdapter.ViewHolder rVh = new MemoRecyclerAdapter.ViewHolder(view);
        return rVh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수
        holder.memo = mData.get(position);
        holder.delPosition = mData.get(position).id;
        holder.memoTextView.setText(mData.get(position).titleStr);
        holder.memoDateTextView.setText(mData.get(position).writeDate);
        Log.d("RecyclerAdapter82", String.valueOf(holder.delPosition));
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

}