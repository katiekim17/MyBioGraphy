package com.example.mybiography;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddmemoActivity extends AppCompatActivity {

    private int mYear = 0, mMonth = 0, mDay = 0;

    TextView memoTextView, memoTitleTextView, memoDateTextView;
    Button memoAddBtn, memoDelBtn;
    EditText editMemoTitle, editMemoText;

    public static final int MemoActivityIdx = 10010; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    Memo memo = new Memo();
    Boolean isUpdate = false;
    Boolean isDelete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmemo);

        memoTextView = findViewById(R.id.memoTextView);
        memoTitleTextView = findViewById(R.id.memoTitleTextView);
        memoDateTextView = findViewById(R.id.memoDateTextView);
        memoAddBtn = findViewById(R.id.memoAddBtn);
        memoDelBtn = findViewById(R.id.memoDelBtn);
        editMemoTitle = findViewById(R.id.editMemoTitle);
        editMemoText = findViewById(R.id.editMemoText);

        //캘린더 시작시 해당날짜의 입력된 내용 보여주기
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        memoDateTextView.setText(String.format("%d / %d / %d", mYear, mMonth + 1, mDay)); //선택한 날짜표시

        //불러오기 (memo인덱스, 로그인아이디)
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("memoId")) {
            int memoId = intent.getExtras().getInt("memoId");
            Log.d("AddmemoActivity54", String.valueOf(memoId));
            memo.id = memoId;

            String loginId = intent.getExtras().getString(SysCodes.KeyCodes.LOGIN_ID.toString());
            Log.d("AddmemoActivity61", loginId);
            memo.writerId = loginId;
        }

        if (intent != null && intent.hasExtra("memoClass")) {
            memo = (Memo) intent.getExtras().get("memoClass");
            isUpdate = intent.getExtras().getBoolean("memoClass_isUpdate"); //true;
            isDelete = false;
            Log.d("AddmemoActivity69", String.valueOf(memo.id));
//            memo = (Memo) intent.getSerializableExtra("memoClass"); //둘중 하나 선택
            editMemoTitle.setText(memo.titleStr); //제목
            editMemoText.setText(memo.editMemoStr); //내용
        }

        //저장하기
        memoAddBtn.setOnClickListener(new View.OnClickListener() { //저장클릭
            @Override
            public void onClick(View v) {
                String titleStr = editMemoTitle.getText().toString();
                String editMemoStr = editMemoText.getText().toString();
//                Log.d("test", "titleStr: " + titleStr.length() + ", isNull?" + titleStr.isEmpty());
//                Log.d("test", "editMemoStr: " + editMemoStr.length() + ", isNull?" + editMemoStr.isEmpty());

                if (titleStr.length() == 0 || titleStr.isEmpty() == true) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddmemoActivity.this);
                    builder.setMessage("제목없이 저장하시겠습니까?");
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(getApplicationContext(), "저장하세요!!", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            memo = saveMemoContents("제목없음", editMemoStr);
                            goToMemoActivity(memo);
                            Toast.makeText(getApplicationContext(), "저장, 메인페이지로 이동!", Toast.LENGTH_LONG).show();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else { //저장
                    memo = saveMemoContents(titleStr, editMemoStr);
                    goToMemoActivity(memo);
                }


            }
        }); //저장하기끝

        //삭제하기
        memoDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddmemoActivity.this);
                builder.setMessage("삭제하시겠습니까?");
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isUpdate = false;
                        isDelete = true;
                        goToMemoActivity(memo);
                        Toast.makeText(getApplicationContext(), "삭제완료, 메모페이지로 이동!", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    } //onCreate end

    public Memo saveMemoContents(String titleStr, String editMemoStr) {

        String writeDate = memoDateTextView.getText().toString();
        memo.titleStr = titleStr;
        memo.editMemoStr = editMemoStr;
        memo.writeDate = writeDate;

        return memo;
    }

    public void goToMemoActivity(Memo memo) {
        Log.d("AddmemoActivity149", String.valueOf(memo.id));
        Log.d("AddmemoActivity150", "아이디: " + memo.writerId);
        Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
        intent.putExtra("memo", memo);
        intent.putExtra(SysCodes.KeyCodes.LOGIN_ID.toString(), memo.writerId);
        intent.putExtra("memo_isUpdate", isUpdate);
        intent.putExtra("memo_isDelete", isDelete);
        startActivityForResult(intent, MemoActivityIdx); //액티비티 띄우기 //대하는 결과가 충족 되면 자신을 호출한 액티비티로 값을 전달하는 동시에 스택에서 자기 자신을 지우고 종료
    }
}