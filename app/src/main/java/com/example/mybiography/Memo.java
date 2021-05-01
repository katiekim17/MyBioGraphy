package com.example.mybiography;

import java.io.Serializable;
import java.util.ArrayList;

public class Memo implements Serializable {

    //객체클래스를 그냥 보내수는 없으므로, extends AppCompatActivity사용!

    public String titleStr;
    public String editMemoStr;
    public String writeDate;
    public ArrayList<Memo> memoList;

    public Memo() {
    }

    public Memo(String titleStr, String editMemoStr) {
    }

//    public String getTitleStr() {
//        return titleStr;
//    }
//
//    public void setTitleStr(String titleStr) {
//        this.titleStr = titleStr;
//    }
//
//    public String getEditMemoStr() {
//        return editMemoStr;
//    }
//
//    public void setEditMemoStr(String editMemoStr) {
//        this.editMemoStr = editMemoStr;
//    }
//
//    public String getWriteDate() {
//        return writeDate;
//    }
//
//    public void setWriteDate(String writeDate) {
//        this.writeDate = writeDate;
//    }
//
//    public ArrayList<Memo> getMemoList() {
//        return memoList;
//    }
//
//    public void setMemoList(ArrayList<Memo> memoList) {
//        this.memoList = memoList;
//    }

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
