package com.cgstate.boxmobile.netapi;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cgstate.boxmobile.R;
import com.cgstate.boxmobile.global.Constant;
import com.cgstate.boxmobile.utils.CustomToast;
import com.cgstate.boxmobile.utils.DensityUtils;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/12/2.
 */

public abstract class ApiSubscriber<T> extends Subscriber<T> {
    private Context mContext;
    private AlertDialog alertDialog;
    private boolean showDialog = true;


    public ApiSubscriber(Context mContext) {
        this.mContext = mContext;
    }

    public ApiSubscriber(Context mContext, boolean showDialog) {
        this.mContext = mContext;
        this.showDialog = showDialog;
    }


    @Override
    public void onStart() {

        Log.d("ApiSubscriber", "开始访问网络了");

        if (showDialog) {
            alertDialog = new AlertDialog.Builder(mContext).create();
            View view = View.inflate(mContext, R.layout.loading, null);
            alertDialog.setView(view);
            alertDialog.show();
            Window window = alertDialog.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = DensityUtils.dip2px(300, mContext);
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(attributes);
            alertDialog.setCancelable(false);
        }


    }

    @Override
    public void onCompleted() {
        Log.d("ApiSubscriber", "网络访问结束---将当前时间更新到LAST_RIGHT_TIME中");
        Constant.LAST_RIGHT_TIME = System.currentTimeMillis();
        if (showDialog) {
            alertDialog.dismiss();
        }

    }

    @Override
    public void onError(Throwable e) {
        CustomToast.showToast(mContext, "网络异常:" + e.getMessage(), 500);
        Log.d("ApiSubscriber", "网络异常:" + e.getMessage());
        if (showDialog) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onNext(T t) {
        Log.d("ApiSubscriber", "网络访问正常");
        if (showDialog) {
            alertDialog.dismiss();
        }
        doSomething(t);
    }

    protected abstract void doSomething(T bean);
}
