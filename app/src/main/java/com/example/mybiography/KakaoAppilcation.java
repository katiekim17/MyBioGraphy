package com.example.mybiography;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class KakaoAppilcation extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Kakao Sdk 초기화
        KakaoSdk.init(this, "b0a08e3c340f4f4c2aae32b4c2cfb438");
    }

}
