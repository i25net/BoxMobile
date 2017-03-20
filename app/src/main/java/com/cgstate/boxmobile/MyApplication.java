package com.cgstate.boxmobile;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.cgstate.boxmobile.services.UpDateTokenService;

/**
 * Created by Administrator on 2017/2/10.
 */

public class MyApplication extends Application {
    private static Context mContext;
    private static Intent service;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        service = new Intent(mContext, UpDateTokenService.class);
//        OkHttpClient picassoClient = MyRetrofitClient.getInstance().getOkHttpClient();
//        final Picasso picasso = new Picasso.Builder(mContext).downloader(new OkHttp3Downloader(picassoClient)).listener(new Picasso.Listener() {
//            @Override
//            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//                Log.d("MyApplication", "picasso:" + exception.getMessage());
//            }
//        }).build();
//
//        //Debug模式
//        picasso.setIndicatorsEnabled(true);
//
//        Picasso.setSingletonInstance(picasso);
    }

    public static Context getContextObject() {
        return mContext;
    }


    public static void startService() {
        mContext.startService(service);
    }

    public static void stopService() {
        mContext.stopService(service);
    }
}
