package com.cgstate.boxmobile.netapi;

import com.cgstate.boxmobile.global.Constant;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/1.
 */

public class BaseInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = Constant.TOKEN;
        Request originalRequest = chain.request()
                .newBuilder()
                .addHeader("token", token)
                .build();
        return chain.proceed(originalRequest);
    }
}
