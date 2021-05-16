package com.example.mybiography.Recycler;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybiography.AddJobActivity;
import com.example.mybiography.Job;
import com.example.mybiography.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;

public class JobRecyclerAdapter extends RecyclerView.Adapter<JobRecyclerAdapter.TimeLineViewHolder> implements ItemTouchHelperListener {

    private ArrayList<Job> mData_job = new ArrayList<>();
    public static final int JobRecylerActivityIdx = 10056; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        return false;
    }

    @Override
    public void onItemSwipe(int position) {
        Log.d("JobRecyclerAdapter33", "onItemSwipe, position: " + String.valueOf(position));

        mData_job.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
        mData_job.remove(position);
        notifyItemRemoved(position);
    }


    public class TimeLineViewHolder extends RecyclerView.ViewHolder {
        public TimelineView mTimelineView; //땡땡이
        public TextView timeline_textView, timeline_startDateTextView, timeline_locationTextView;
        public ImageView job_imageView;
        public CardView timeline_cardView;
        private Job job = new Job();

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            timeline_cardView = itemView.findViewById(R.id.timeline_cardView);

            timeline_textView = itemView.findViewById(R.id.timeline_textView);
//            timeline_startDateTextView = itemView.findViewById(R.id.timeline_startDateTextView);
            timeline_locationTextView = itemView.findViewById(R.id.timeline_locationTextView);
            job_imageView = itemView.findViewById(R.id.job_imageView);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.timeline);
            mTimelineView.initLine(viewType);

            timeline_cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(timeline_cardView.getContext(), AddJobActivity.class);
                    Log.d("RecyclerAdapter47", "clicked");
                    intent.putExtra("jobClass", job);
                    intent.putExtra("jobClass_isUpdate", true);
                    timeline_cardView.getContext().startActivity(intent); //액티비티 띄우기
                }
            });

        }
    }

    public JobRecyclerAdapter(ArrayList<Job> jobList) {
        mData_job = jobList;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("JobRecyclerAdapter76", ": " + position);
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
        if (status.equals("INACTIVE")) {
//            setMarker(holder, R.drawable.ic_marker_inactive, R.color.material_grey_500);
        }
        String jobTitle = mData_job.get(position).jobName;
        holder.timeline_textView.setText(mData_job.get(position).startTime+", "+jobTitle);
//        holder.timeline_startDateTextView.setText(mData_job.get(position).startDate);
        holder.timeline_locationTextView.setText(mData_job.get(position).jobLocation);
        holder.job_imageView.setImageResource(mData_job.get(position).image);
        holder.job = mData_job.get(position);

    }

//    private void setMarker(TimeLineViewHolder holder, int ic_marker_inactive, int material_grey_500) {
//        holder.mTimelineView.setMarker();VectorDrawableUtils.getDrawable(holder.itemView.context, drawableResId, ContextCompat.getColor(holder.itemView.context, colorFilter))
//    }

    @Override
    public int getItemCount() {
        Log.d("getItemCount", String.valueOf(mData_job.size()));
        return mData_job.size();
    }

    public void removeItem(int position) {
        mData_job.remove(position);
        notifyDataSetChanged(); //refresh
    }

}
