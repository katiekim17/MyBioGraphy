package com.example.mybiography;

import java.io.Serializable;
import java.util.ArrayList;

public class Memo implements Serializable {

    //객체클래스를 그냥 보내수는 없으므로, extends AppCompatActivity사용!

    public int id;
    public String writerId;
    public String titleStr; //제목
    public String editMemoStr; //내용
    public String writeDate;
    public ArrayList<Memo> memoList;

    public Memo() {
    }

    public Memo(int id, String titleStr, String editMemoStr, String writeDate) {
        this.id = id;
        this.titleStr = titleStr;
        this.editMemoStr = editMemoStr;
        this.writeDate = writeDate;
    }


    @Override
    public String toString() {

        return "Memo{" +
                "titleStr='" + titleStr + '\'' +
                ", editMemoStr='" + editMemoStr + '\'' +
                ", writeDate='" + writeDate + '\'' +
                ", memoList=" + memoList +
                '}';
    }
}
