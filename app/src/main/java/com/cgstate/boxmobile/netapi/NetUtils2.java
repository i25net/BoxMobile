package com.cgstate.boxmobile.netapi;


import com.cgstate.boxmobile.utils.TempURLUtils;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/12/1.
 */

public class NetUtils2 {


    private static class LazyHolder {
        private static final NetUtils2 INSTANCE = new NetUtils2();
    }

    public static final NetUtils2 getInstance() {
        return LazyHolder.INSTANCE;
    }



    public static ApiControl getApiControl() {
        return new Retrofit.Builder()
                .baseUrl(TempURLUtils.getInstance().getTempURL())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(
                        OkHttpUtils.getInstance().getClient()
                )
                .build().create(ApiControl.class);

    }


}
