package com.example.mybiography.Recycler;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybiography.Job;
import com.example.mybiography.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;

public class JobRecyclerAdapter extends RecyclerView.Adapter<JobRecyclerAdapter.TimeLineViewHolder> {

    private ArrayList<Job> mData_job = new ArrayList<>();
    public static final int JobRecylerActivityIdx = 10056; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    public class TimeLineViewHolder extends RecyclerView.ViewHolder {
        public TimelineView mTimelineView; //땡땡이
        public TextView timeline_textView, timeline_startDateTextView, timeline_locationTextView;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            timeline_textView = itemView.findViewById(R.id.timeline_textView);
            timeline_startDateTextView = itemView.findViewById(R.id.timeline_startDateTextView);
            timeline_locationTextView = itemView.findViewById(R.id.timeline_locationTextView);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.timeline);
            mTimelineView.initLine(viewType);
        }
    }

    public JobRecyclerAdapter(ArrayList<Job> jobList) {

        if(jobList != null){
            Log.d("JobRecyclerAdapter", ": " + jobList.get(0).jobContents);
        }

        mData_job = jobList;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("JobRecyclerAdapter", ": " + position);
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_timeline, null);
        return new TimeLineViewHolder(view, viewType);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수
        String status = mData_job.get(position).status;
        if(status.equals("INACTIVE")){
//            setMarker(holder, R.drawable.ic_marker_inactive, R.color.material_grey_500);
        }
        Log.d("onBindViewHolder1", String.valueOf(position));
        String jobTitle = mData_job.get(position).jobName;
        Log.d("onBindViewHolder2", jobTitle);
        holder.timeline_textView.setText(jobTitle);
        holder.timeline_startDateTextView.setText( mData_job.get(position).startDate);

    }

//    private void setMarker(TimeLineViewHolder holder, int ic_marker_inactive, int material_grey_500) {
//        holder.mTimelineView.setMarker();VectorDrawableUtils.getDrawable(holder.itemView.context, drawableResId, ContextCompat.getColor(holder.itemView.context, colorFilter))
//    }

    @Override
    public int getItemCount() {
        Log.d("getItemCount", String.valueOf(mData_job.size()));
        return mData_job.size();
    }


}
