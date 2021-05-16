package com.example.mybiography;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderPage extends RecyclerView.ViewHolder {

    private TextView jobListPageText;
    Job job;

    ViewHolderPage(View itemView) {
        super(itemView);
        jobListPageText = itemView.findViewById(R.id.jobListPageText);
    }

    public void onBind(Job job){
        this.job = job;
        jobListPageText.setText(job.jobName);
    }
}
