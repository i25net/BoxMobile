package com.cgstate.boxmobile.netapi;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Administrator on 2017/2/8.
 */

public class DefaultProgressListener implements ProgressUploadListener {
    private Handler mHandler;
    //多文件上传时，index作为上传的位置的标志
    private int mIndex;

    public DefaultProgressListener(Handler mHandler, int mIndex) {
        this.mHandler = mHandler;
        this.mIndex = mIndex;
    }

    @Override
    public void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish) {
//        System.out.println("mIndex" + mIndex + "----the current " + hasWrittenLen + "----" + totalLen + "-----" + (hasWrittenLen * 100 / totalLen));

        int percent = (int) (hasWrittenLen * 100 / totalLen);
        if (percent > 100) percent = 100;
        if (percent < 0) percent = 0;


        Message msg = Message.obtain();
        msg.what = percent;
        msg.arg1 = mIndex;
        mHandler.sendMessage(msg);
    }


}
