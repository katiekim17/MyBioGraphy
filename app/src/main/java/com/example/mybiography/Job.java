package com.example.mybiography;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Job implements Serializable {


    public String jobName = "";
    public String writeDate;
    public String updateDate;
    public String startDate;
    public String startTime;
    public Date endDate;
    public String jobLocation = "";
    public Double latitude;
    public Double longitude;
    public String jobContents = "";
    public int priority = 1;
    public int groupColor;
    public String status = "INACTIVE";
    public int image;
    public String loginId;
    public ArrayList<Memo> memoList;

    public Job() {
    }

    public Job(String jobName, String startDate, String jobLocation, String jobContents, String status, int image, String loginId) {
        this.jobName = jobName;
        this.startDate = startDate;
        this.jobLocation = jobLocation;
        this.jobContents = jobContents;
        this.status = status;
        this.image = image;
        this.updateDate = Util.getCalendar();
    }

    public Job(String jobName, String startDate, String jobContents, String status, int image, String loginId) {
        this.jobName = jobName;
        this.startDate = startDate;
        this.jobLocation = jobLocation;
        this.jobContents = jobContents;
        this.status = status;
        this.image = image;
        this.updateDate = Util.getCalendar();
    }

    public Job(String jobName, String startDate, String jobLocation, String jobContents, String status, String loginId) {
        this.jobName = jobName;
        this.startDate = startDate;
        this.jobLocation = jobLocation;
        this.jobContents = jobContents;
        this.status = status;
        this.updateDate = Util.getCalendar();
    }

    public Job(String jobName, String startDate, String jobContents, String status, String loginId) {
        this.jobName = jobName;
        this.startDate = startDate;
        this.jobContents = jobContents;
        this.status = status;
        this.updateDate = Util.getCalendar();
    }

    public Job(String jobName, String startDate, String jobContents, String loginId) {
        this.jobName = jobName;
        this.startDate = startDate;
        this.jobLocation = jobLocation;
        this.jobContents = "INACTIVE";
        this.image = image;
        this.updateDate = Util.getCalendar();
    }

    @Override
    public String toString() {
        return "Job{" +
                "jobName='" + jobName + '\'' +
                ", writeDate='" + writeDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate=" + endDate +
                ", jobLocation='" + jobLocation + '\'' +
                ", jobContents='" + jobContents + '\'' +
                ", priority=" + priority +
                ", groupColor=" + groupColor +
                ", status='" + status + '\'' +
                ", image=" + image +
                ", loginId='" + loginId + '\'' +
                ", memoList=" + memoList +
                '}';
    }
}
