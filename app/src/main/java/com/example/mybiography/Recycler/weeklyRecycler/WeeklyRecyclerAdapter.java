package com.example.mybiography.Recycler.weeklyRecycler;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybiography.Job;
import com.example.mybiography.R;

import java.util.ArrayList;

public class WeeklyRecyclerAdapter extends RecyclerView.Adapter<WeeklyRecyclerAdapter.WeeklyViewHolder> {

    private ArrayList<Job> mData_job = new ArrayList<>();
    public static final int JobRecylerActivityIdx = 10056; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    public class WeeklyViewHolder extends RecyclerView.ViewHolder {
        public TextView dayListTextView, dayLists_tartDateTextView, dayLists_locationTextView;

        public WeeklyViewHolder(View itemView, int viewType) {
            super(itemView);
            dayListTextView = itemView.findViewById(R.id.dayListTextView);
//            dayLists_tartDateTextView = itemView.findViewById(R.id.dayLists_tartDateTextView);
            dayLists_locationTextView = itemView.findViewById(R.id.dayLists_locationTextView);
        }
    }

    public WeeklyRecyclerAdapter(ArrayList<Job> jobList) {

        if(jobList.size() > 0){
//            Log.d("WeeklyRecyclerAdapter", ": " + jobList.get(0).jobContents);
        }

        mData_job = jobList;
    }

    @NonNull
    @Override
    public WeeklyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.weeklyrecycler_itemview, null);
        return new WeeklyViewHolder(view, viewType);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull WeeklyViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수
//        Log.d("weeklyDateTextView2", ": " + position);
//        Log.d("weeklyDateTextView2", ": " + mData_job.get(position).jobName);
        holder.dayListTextView.setText(mData_job.get(position).jobName);
        holder.dayLists_tartDateTextView.setText( mData_job.get(position).startDate);
        holder.dayLists_locationTextView.setText( mData_job.get(position).jobLocation);

    }

    @Override
    public int getItemCount() {
//        Log.d("WeeklyRecycle_size :", String.valueOf(mData_job.size()));
        return mData_job.size();
    }
}
