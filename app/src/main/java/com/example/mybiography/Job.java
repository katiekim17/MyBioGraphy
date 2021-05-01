package com.example.mybiography;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Job implements Serializable {


    public String jobName;
    public String startDate;
    public Date endDate;
    public String jonLocation;
    public String jobContents;
    public int priority;
    public int groupColor;
    public String status;
    public ArrayList<Memo> memoList;

    public Job(String jobName, String startDate, String jobContents, String status) {
        this.jobName = jobName;
        this.startDate = startDate;
        this.jobContents = jobContents;
        this.status = status;
    }

    public Job(String jobName, String startDate, String jobContents) {
        this.jobName = jobName;
        this.startDate = startDate;
        this.jobContents = jobContents;
    }
}
