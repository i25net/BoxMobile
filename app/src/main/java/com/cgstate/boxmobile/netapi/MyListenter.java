package com.cgstate.boxmobile.netapi;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cgstate.boxmobile.bean.MyObj;

/**
 * Created by Administrator on 2017/2/28.
 */

public class MyListenter implements ProgressRequestBody.Listener {
    private int index;
    private int totalUpLoadImagesNumber;
    private int definiteNumber;
    private Handler mHandler;
    private MyObj myObj;

    public MyListenter(Handler mHandler, int index, int totalUpLoadImagesNumber, int definiteNumber, MyObj myObj) {
        this.myObj = myObj;
        this.mHandler = mHandler;
        this.index = index;
        this.totalUpLoadImagesNumber = totalUpLoadImagesNumber;
        this.definiteNumber = definiteNumber;
    }


    @Override
    public void onProgress(long hasWrittenLen, long total, int progress, boolean hasFinished) {
//        Log.d("MyListenter", index + "---hasWrittenLen:" + hasWrittenLen);
//        Log.d("MyListenter", index + "---total:" + total);
//        Log.d("MyListenter", index + "---progress:" + progress);
//        Log.d("MyListenter", index + "---hasFinished:" + hasFinished);


        Log.d("MyListenter", "index:"+index+"----myObj:" + myObj+"---"+Thread.currentThread().getName()+"----"+Thread.currentThread().getId());

        if (progress > 100) progress = 100;
        if (progress < 0) progress = 0;

        if (progress == 100) {
            int count = myObj.getCount();
            count++;
            myObj.setCount(count);
            progress = 0;

        }


        Message msg = Message.obtain();


        msg.what = index;//what用于索引第几个大条目
        msg.obj = totalUpLoadImagesNumber;//该条目下有多少张图片
        msg.arg1 = definiteNumber;//arg1用于标识第几张图片

        if (myObj.getCount() == 0) {
            msg.arg2 = progress;//arg2 图片的上传总进度
        } else if (myObj.getCount() < totalUpLoadImagesNumber) {
            msg.arg2 = myObj.getCount() * 100 + progress;
        } else {
            msg.arg2 = totalUpLoadImagesNumber * 100;
        }
        Log.d("MyListenter", "index:" + index + "---count:" + myObj.getCount());

        mHandler.sendMessage(msg);

    }
}
