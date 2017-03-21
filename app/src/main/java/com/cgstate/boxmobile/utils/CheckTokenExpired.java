package com.cgstate.boxmobile.utils;

import android.content.Context;
import android.util.Log;

import com.cgstate.boxmobile.global.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/1.
 */

public class CheckTokenExpired {


    public static boolean isExpired() {
        boolean isExpired;
        int expiredSeconds = -1;
        long lastLoginTime = -1;

        if (Constant.EXPIRED_TIME_SECONDS > 0) {
            Log.d("CheckTokenExpired", "Constant.EXPIRED_TIME_SECONDS:" + Constant.EXPIRED_TIME_SECONDS);

            expiredSeconds = Constant.EXPIRED_TIME_SECONDS * 1000;
            expiredSeconds = expiredSeconds - 10000;
        }

        if (Constant.LAST_RIGHT_TIME > 0) {
            Log.d("CheckTokenExpired", "Constant.LAST_RIGHT_TIME > 0");
            lastLoginTime = Constant.LAST_RIGHT_TIME;
        }

        long now = System.currentTimeMillis();
        if ((now - lastLoginTime) >= expiredSeconds) {
            Log.d("CheckTokenExpired", "---------超时咯-----------");
            isExpired = true;
        } else {
            isExpired = false;
        }

        Date date = new Date(lastLoginTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy---MM-----dd HH:mm:ss:SSSS");
        Log.d("CheckTokenExpired", "lastLoginTime:" + simpleDateFormat.format(date));

        return isExpired;
    }


    public static void login(Context mContext) {
        LoginUtils.login(mContext);
    }

}
