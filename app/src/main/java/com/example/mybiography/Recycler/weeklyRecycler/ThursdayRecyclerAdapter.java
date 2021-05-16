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

public class ThursdayRecyclerAdapter extends RecyclerView.Adapter<ThursdayRecyclerAdapter.WeeklyViewHolder> {

    private ArrayList<Job> mData_job = new ArrayList<>();
    public static final int JobRecylerActivityIdx = 10056; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    public class WeeklyViewHolder extends RecyclerView.ViewHolder {
        public TextView thurdayListTextView, thurdayLists_startDateTextView, thurdayLists_locationTextView;
        public TextView thursdayTextView;

        public WeeklyViewHolder(View itemView, int viewType) {
            super(itemView);
            thurdayListTextView = itemView.findViewById(R.id.thurdayListTextView);
//            thurdayLists_startDateTextView = itemView.findViewById(R.id.thurdayLists_startDateTextView);
            thurdayLists_locationTextView = itemView.findViewById(R.id.thurdayLists_locationTextView);
            thursdayTextView = itemView.findViewById(R.id.thursdayTextView);
        }
    }

    public ThursdayRecyclerAdapter(ArrayList<Job> jobList) {

        if(jobList.size() > 0){
//            Log.d("WeeklyRecyclerAdapter", ": " + jobList.get(0).jobContents);
        }

        mData_job = jobList;
    }

    @NonNull
    @Override
    public WeeklyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.thursdayrecycler_itemview, null);
        return new WeeklyViewHolder(view, viewType);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull WeeklyViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수
//        Log.d("weeklyDateTextView2", ": " + position);
        Log.d("weeklyDateTextView2", ": " + mData_job.get(position).jobName);
        holder.thurdayListTextView.setText(mData_job.get(position).jobName);
        if(!mData_job.get(position).startTime.isEmpty()) {
            holder.thurdayListTextView.setText(mData_job.get(position).startTime + ", " + mData_job.get(position).jobName);
        }
//        holder.thurdayLists_startDateTextView.setText( mData_job.get(position).startDate);
        holder.thurdayLists_locationTextView.setText( mData_job.get(position).jobLocation);

    }

    @Override
    public int getItemCount() {
//        Log.d("WeeklyRecycle_size :", String.valueOf(mData_job.size()));
        return mData_job.size();
    }
}
