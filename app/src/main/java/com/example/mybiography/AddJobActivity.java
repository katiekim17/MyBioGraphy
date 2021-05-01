package com.example.mybiography;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

public class AddJobActivity extends AppCompatActivity {

    TextView cal_DateTitleTextView2, cal_StartDateTextView, cal_ContentsTitleTextView;
    Button saveBtn, delBtn;
    EditText jobTitleEditText, contextEditText;

    Toolbar calToolBar; //<androidx.appcompat.widget.Toolbar사용했으므로 androidx에 있는 toolbar를 사용
    ActionBar actionBar;

    private int mYear = 0, mMonth = 0, mDay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        jobTitleEditText = findViewById(R.id.jobTitleEditText); //일정제목
        contextEditText = findViewById(R.id.multiLineEditText); //내용입력

        cal_DateTitleTextView2 = findViewById(R.id.cal_DateTitleTextView2);
        cal_StartDateTextView = findViewById(R.id.cal_StartDateTextView);
        cal_ContentsTitleTextView = findViewById(R.id.cal_ContentsTitleTextView);

        saveBtn = findViewById(R.id.saveBtn);
        delBtn = findViewById(R.id.delBtn);
//        delBtn.setVisibility(View.INVISIBLE); //버튼 숨기기

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        cal_StartDateTextView.setText(String.format("%d . %d . %d", mYear, mMonth + 1, mDay)); //선택한 날짜표시

        saveBtn.setOnClickListener(new View.OnClickListener() { //저장클릭
            @Override
            public void onClick(View v) {
                String jobTitle = jobTitleEditText.getText().toString(); //일정제목
                String jobStartDate = cal_StartDateTextView.getText().toString(); //시간
                String jobContents = contextEditText.getText().toString(); // 내용
                saveJobContent(new Job(jobTitle,jobStartDate,jobContents));
            }
        });
    }

    public void saveJobContent(Job job){
        Log.d("saveJobContent", "saveJobContent: " + job.jobContents );

        goToCalActivity(job);
    }

    public void goToCalActivity(Job job){
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("fromAddJobActivity1", "hi,fromAddContents");
        intent.putExtra("fromAddJobActivity2", job);
        startActivityForResult(intent, MainActivity.CalIdx); //액티비티 띄우기 //대하는 결과가 충족 되면 자신을 호출한 액티비티로 값을 전달하는 동시에 스택에서 자기 자신을 지우고 종료

    }
}