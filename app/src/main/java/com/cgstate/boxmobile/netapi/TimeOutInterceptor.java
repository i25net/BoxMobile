package com.cgstate.boxmobile.netapi;

import android.util.Log;

import com.cgstate.boxmobile.MyApplication;
import com.cgstate.boxmobile.bean.LoginBean;
import com.cgstate.boxmobile.global.Constant;
import com.cgstate.boxmobile.utils.PrefUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by Administrator on 2017/3/21.
 */

public class TimeOutInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = Constant.TOKEN;
        Request originalRequest = chain.request()
                .newBuilder()
                .addHeader("token", token)
                .build();
        Response originalResponse = chain.proceed(originalRequest);

        //转化操作.body.string()方法会关闭当前操作,closed异常
        ResponseBody originalResponseBody = originalResponse.body();
        BufferedSource source = originalResponseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = originalResponseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }

        String bodyString = buffer.clone().readString(charset);

        Log.d("TimeOutInterceptor", bodyString.length() + "-------" + bodyString);

        if (bodyString.contains("登陆失效")) {
            originalResponse.body().close();
            Log.d("TimeOutInterceptor", "登陆失效");
            Request loginRequest = getLoginRequest();
            if (loginRequest != null) {
                Response loginResponse = chain.proceed(loginRequest);
                if (loginResponse.isSuccessful()) {
                    String string = loginResponse.body().string();
                    Gson gson = new Gson();
                    LoginBean loginBean = gson.fromJson(string, LoginBean.class);
                    Constant.TOKEN = loginBean.data.token;

                    loginResponse.body().close();

                    Request.Builder builder = originalRequest.newBuilder();
                    Request newRequest = builder.header("token", Constant.TOKEN).build();

                    return chain.proceed(newRequest);
                }
            }
        } else {
            if (originalResponse.isSuccessful()) {
                Log.d("TimeOutInterceptor", "没有登陆失效.不走方法");
                return originalResponse;
            }
        }
        Log.e("TimeOutInterceptor", "如果没过期,那么这里有日志" + " ----" + "如果过期这里还有日志那就不对了");
        return originalResponse;
    }

    private Request getLoginRequest() {
        if (PrefUtils.getString(MyApplication.getContextObject(), "username", "").length() <= 1) {
            return null;
        } else {
            Log.d("TimeOutInterceptor", "如果超时了,进行登陆操作");
            return new Request.Builder()
                    .url(Constant.BASE_URL + Constant.APP_LOGIN)
                    .post(
                            new FormBody.Builder()
                                    .add("account", PrefUtils.getString(MyApplication.getContextObject(), "username", ""))
                                    .add("password", PrefUtils.getString(MyApplication.getContextObject(), "password", ""))
                                    .build()
                    )
                    .build();
        }
    }
}
