package com.cgstate.boxmobile.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.cgstate.boxmobile.MyApplication;

/**
 * Created by Administrator on 2017/3/15.
 */

public class ScreenUtils {
    public static int getScreenWidth() {
        WindowManager windowManager = (WindowManager) MyApplication.getContextObject().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        return dm.widthPixels;
    }
}
