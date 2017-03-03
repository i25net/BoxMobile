package com.cgstate.boxmobile.utils;

import android.content.Context;
import android.content.Intent;

import com.cgstate.boxmobile.activities.LoginActivity;

/**
 * Created by Administrator on 2017/3/1.
 */

public class GoToLoginActivity {
    public static void goToLoginActivity(Context mContext) {
        Intent intent = new Intent();
        intent.putExtra("openlogin", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(mContext, LoginActivity.class);
        mContext.startActivity(intent);
    }
}
