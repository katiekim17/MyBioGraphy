package com.example.mybiography;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DiaryActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int CAMERA_REQUEST_CODE = 2;

    private int mYear = 0, mMonth = 0, mDay = 0;
    TextView diaryToday;
    ImageView imgView;
    Button cameraBtn, saveBtn, delBtn;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        diaryToday = findViewById(R.id.diary_today);
        imgView = findViewById(R.id.imageView);
        cameraBtn = findViewById(R.id.cameraBtn);
        saveBtn = findViewById(R.id.saveBtn);

        Calendar calendar = new GregorianCalendar();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        Log.v("text", "test : " + diaryToday.getText());
        if (diaryToday.getText().length() == 0) {
            diaryToday.setText(String.format("%d / %d / %d", mYear, mMonth + 1, mDay));
        }
        //SD카드로 저장설정
        File sdCard = Environment.getExternalStorageDirectory();
        file = new File(sdCard, "capture.pjg");

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //카메라 인텐트 불러오기
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//            Uri uri = FileProvider.getUriForFile(getBaseContext(), "com.onedelay.chap7.fileprovider", file);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("text", "onActivityResult : " + requestCode);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            //사진의 해상도가 너무 높으므로 비트맵으로 로딩
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8; // 1/8 로 크기를 줄임
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),options);
            imgView.setImageBitmap(bitmap);

        }

    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        Log.v("text", "onActivityResult : " + requestCode);
//        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
//            //사진의 해상도가 너무 높으므로 비트맵으로 로딩
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 8; // 1/8 로 크기를 줄임
//            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),options);
//            imgView.setImageBitmap(bitmap);
//
//        }
//    }
}