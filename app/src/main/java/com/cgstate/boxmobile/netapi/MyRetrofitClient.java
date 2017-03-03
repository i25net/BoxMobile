package com.cgstate.boxmobile.netapi;

import android.content.Context;
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

    private static Context mContext;


    private static MyRetrofitClient sNewInstance;

    private static class SingletonHolder {
        private static MyRetrofitClient INSTANCE = new MyRetrofitClient(mContext);
    }

    public static MyRetrofitClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }

    public static MyRetrofitClient getInstance(Context context, String url) {
        if (context != null) {
            mContext = context;
        }
        sNewInstance = new MyRetrofitClient(context, url);
        return sNewInstance;
    }

    private MyRetrofitClient(Context context) {
        this(context, null);
    }


    private MyRetrofitClient(Context context, String url) {

        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(Constant.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(Constant.READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(Constant.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new BaseInterceptor())
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();
        apiControl = retrofit.create(ApiControl.class);
    }


    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public ApiControl getApiControl() {
        return apiControl;
    }
}
