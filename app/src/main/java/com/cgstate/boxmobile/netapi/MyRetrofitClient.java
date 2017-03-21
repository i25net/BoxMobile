package com.cgstate.boxmobile.netapi;

import android.text.TextUtils;

import com.cgstate.boxmobile.global.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/2/10.
 */
public class MyRetrofitClient {


    private ApiControl apiControl;

    private OkHttpClient mOkHttpClient;


    public static String baseUrl = Constant.BASE_URL;

    private static MyRetrofitClient sNewInstance;

    private static class SingletonHolder {
        private static MyRetrofitClient INSTANCE = new MyRetrofitClient();
    }

    public static MyRetrofitClient getInstance() {

        return SingletonHolder.INSTANCE;
    }

    public static MyRetrofitClient getInstance(String url) {
        sNewInstance = new MyRetrofitClient(url);
        return sNewInstance;
    }

    private MyRetrofitClient() {
        this(null);
    }


    private MyRetrofitClient(String url) {

        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }

        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(Constant.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(Constant.READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(Constant.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new TimeOutInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();
        apiControl = retrofit.create(ApiControl.class);
    }

    public ApiControl getApiControl() {
        return apiControl;
    }
}
