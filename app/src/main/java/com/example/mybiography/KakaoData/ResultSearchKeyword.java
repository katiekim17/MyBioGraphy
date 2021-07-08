package com.example.mybiography.KakaoData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultSearchKeyword {

    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("documents")
    @Expose
    private List<Documents> documents;

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Meta getMeta(){
        return meta;
    }

    public List<Documents> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Documents> documents) {
        this.documents = documents;
    }

    @Override
    public String toString(){
        return
                "KakaoResultSearchKeyword{" +
                        "documents = '" + documents + '\'' +
                        ",meta = '" + meta + '\'' +
                        "}";
    }
}
