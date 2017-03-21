package com.cgstate.boxmobile.netapi;


import com.cgstate.boxmobile.global.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/12/13.
 */

public class OkHttpUtils {

    private static class LazyHolder {
        private static final OkHttpUtils INSTANCE = new OkHttpUtils();
    }

    public static final OkHttpUtils getInstance() {
        return OkHttpUtils.LazyHolder.INSTANCE;
    }


    private static class GetOkHttpInstance {
        private static final OkHttpClient INSTANCE =
                new OkHttpClient.Builder()
                        .connectTimeout(Constant.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                        .readTimeout(Constant.READ_TIMEOUT, TimeUnit.MILLISECONDS)
                        .writeTimeout(Constant.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                        .addInterceptor(new BaseInterceptor())
                        .addInterceptor(new TimeOutInterceptor())
                        .build();
    }

    public static final OkHttpClient getClient() {
        return GetOkHttpInstance.INSTANCE;
    }

}
