package com.cgstate.boxmobile.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.cgstate.boxmobile.MyApplication;
import com.cgstate.boxmobile.global.Constant;
import com.cgstate.boxmobile.utils.CheckTokenExpired;

public class UpDateTokenService extends Service {

    private Context mContext;

    private int checkTimeDelay = Constant.CHECK_TIME_DELAY;

    public UpDateTokenService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Log.d("UpDateTokenService", "----------onCreate");
        checkToken();

    }

    private void checkToken() {
        mHandler.postDelayed(checkTokenRunnable, checkTimeDelay);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("UpDateTokenService", "-------------------onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("UpDateTokenService", "--------------------------onDestroy");
        mHandler.removeCallbacksAndMessages(null);
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (CheckTokenExpired.isExpired()) {
                MyApplication.stopService();
                CheckTokenExpired.login(mContext);
                return;
            }
            mHandler.postDelayed(checkTokenRunnable, checkTimeDelay);
        }
    };


    private Runnable checkTokenRunnable = new Runnable() {
        @Override
        public void run() {
            Message message = Message.obtain();
            message.what=1;
            mHandler.sendMessage(message);
        }
    };
}
