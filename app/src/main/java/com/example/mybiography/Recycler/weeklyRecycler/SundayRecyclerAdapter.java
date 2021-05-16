package com.example.mybiography.Recycler.weeklyRecycler;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybiography.Job;
import com.example.mybiography.R;

import java.util.ArrayList;

public class SundayRecyclerAdapter extends RecyclerView.Adapter<SundayRecyclerAdapter.SundayViewHolder> {

    private ArrayList<Job> mData_job = new ArrayList<>();
    public static final int JobRecylerActivityIdx = 10057; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    public class SundayViewHolder extends RecyclerView.ViewHolder {
        public TextView sundayListTextView, sundayLists_startDateTextView, sundayLists_locationTextView;
        public TextView sundayTextView;

        public SundayViewHolder(View itemView, int viewType) {
            super(itemView);
            sundayListTextView = itemView.findViewById(R.id.sundayListTextView);
//            sundayLists_startDateTextView = itemView.findViewById(R.id.sundayLists_startDateTextView);
            sundayLists_locationTextView = itemView.findViewById(R.id.sundayLists_locationTextView);
        }
    }

    public SundayRecyclerAdapter(ArrayList<Job> jobList) {
        mData_job = jobList;
    }

    @NonNull
    @Override
    public SundayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.sundayrecycler_itemview, null);
        return new SundayViewHolder(view, viewType);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull SundayViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수
//        Log.d("weeklyDateTextView2", ": " + position);
//        Log.d("weeklyDateTextView2", ": " + mData_job.get(position).jobName);
        holder.sundayListTextView.setText(mData_job.get(position).jobName);
        if(!mData_job.get(position).startTime.isEmpty()){
            holder.sundayListTextView.setText(mData_job.get(position).startTime+", "+mData_job.get(position).jobName);
        }
//        holder.sundayLists_startDateTextView.setText( mData_job.get(position).startDate);
        holder.sundayLists_locationTextView.setText( mData_job.get(position).jobLocation);
    }

    @Override
    public int getItemCount() {
//        Log.d("WeeklyRecycle_size :", String.valueOf(mData_job.size()));
        return mData_job.size();
    }
}
