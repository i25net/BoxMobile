package com.cgstate.boxmobile.utils;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/12/1.
 */

public class CustomToast {
    private static Toast mToast;

    public static void showToast(Context mContext, String text, int duration) {

        if (mToast != null) {
            mToast.setText(text);
            mToast.setDuration(duration);
            mToast.show();
        } else {
            mToast = Toast.makeText(mContext, text, duration);
            LinearLayout linearLayout = (LinearLayout) mToast.getView();
            TextView messageTextView = (TextView) linearLayout.getChildAt(0);
            messageTextView.setTextSize(20);
            mToast.show();
        }
    }
}
