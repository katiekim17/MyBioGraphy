package com.example.mybiography;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public interface SysCodes {
    public static enum KeyCodes {
        PREF,
        MEMBER,
        MEMOLIST,
        LOGIN_ID,
        USER_NAME,
        LOGIN_CHECK,
        JOBLIST;

        @NonNull
        @NotNull
        @Override
        public String toString() {
//            return "userAuth:" + super.toString();
            return super.toString();
        }
    }
}
