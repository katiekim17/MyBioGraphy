package com.example.mybiography.KakaoData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegionInfo {

    @SerializedName("region")
    @Expose
    private List<Object> region;
    @SerializedName("keyword")
    @Expose
    private String keyword;
    @SerializedName("selected_region")
    @Expose
    private String selected_region;

    public List<Object> getRegion() {
        return region;
    }

    public void setRegion(List<Object> region) {
        this.region = region;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSelected_region() {
        return selected_region;
    }

    public void setSelected_region(String selected_region) {
        this.selected_region = selected_region;
    }
}
