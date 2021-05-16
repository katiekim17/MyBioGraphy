package com.example.mybiography.Recycler.weeklyRecycler;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybiography.Job;
import com.example.mybiography.R;

import java.util.ArrayList;

public class FridayRecyclerAdapter extends RecyclerView.Adapter<FridayRecyclerAdapter.WeeklyViewHolder> {

    private ArrayList<Job> mData_job = new ArrayList<>();
    public static final int JobRecylerActivityIdx = 10056; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    public class WeeklyViewHolder extends RecyclerView.ViewHolder {
        public TextView fridayListTextView, fridayLists_startDateTextView, fridayLists_locationTextView;
        public TextView fridayTextView;

        public WeeklyViewHolder(View itemView, int viewType) {
            super(itemView);
            fridayListTextView = itemView.findViewById(R.id.fridayListTextView);
//            fridayLists_startDateTextView = itemView.findViewById(R.id.fridayLists_startDateTextView);
            fridayLists_locationTextView = itemView.findViewById(R.id.fridayLists_locationTextView);
            fridayTextView = itemView.findViewById(R.id.fridayTextView);
        }
    }

    public FridayRecyclerAdapter(ArrayList<Job> jobList) {

        if(jobList.size() > 0){
//            Log.d("WeeklyRecyclerAdapter", ": " + jobList.get(0).jobContents);
        }

        mData_job = jobList;
    }

    @NonNull
    @Override
    public WeeklyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.fridayrecycler_itemview, null);
        return new WeeklyViewHolder(view, viewType);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull WeeklyViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수
//        Log.d("weeklyDateTextView2", ": " + position);
        Log.d("weeklyDateTextView2", ": " + mData_job.get(position).jobName);
        holder.fridayListTextView.setText(mData_job.get(position).jobName);
        Log.d("weeklyDateTextView3", ": " + String.valueOf(mData_job.get(position).startTime.isEmpty()));
        if(!mData_job.get(position).startTime.isEmpty()){
            holder.fridayListTextView.setText(mData_job.get(position).startTime+", "+mData_job.get(position).jobName);
        }
//        holder.fridayLists_startDateTextView.setText( mData_job.get(position).startDate);
        holder.fridayLists_locationTextView.setText( mData_job.get(position).jobLocation);

    }

    @Override
    public int getItemCount() {
//        Log.d("WeeklyRecycle_size :", String.valueOf(mData_job.size()));
        return mData_job.size();
    }
}
