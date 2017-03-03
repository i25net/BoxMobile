package com.cgstate.boxmobile.netapi;


import com.cgstate.boxmobile.MyApplication;
import com.cgstate.boxmobile.utils.PrefUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/12/1.
 */

public class NetUtils {


    private static class LazyHolder {
        private static final NetUtils INSTANCE = new NetUtils();
    }

    public static final NetUtils getInstance() {
        return LazyHolder.INSTANCE;
    }


    private static class GetApiControlInstance {
        private static final ApiControl API_CONTROL =
                new Retrofit.Builder()
                        .baseUrl("http://" + PrefUtils.getString(MyApplication.getContextObject(), "server_address", "www.baidu.com") + "/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .client(
                                OkHttpUtils.getInstance().getClient()
                        )
                        .build().create(ApiControl.class);
    }


    public static ApiControl getApiControl() {
//        if (apiControl == null) {
//            return new Retrofit.Builder()
//                    .baseUrl(Constant.BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    .client(
//                            new OkHttpClient.Builder()
//                                    .addInterceptor(new BaseInterceptor())
//                                    .build()
//                    )
//                    .build().create(ApiControl.class);
//        } else {
//            return apiControl;
//        }

        return GetApiControlInstance.API_CONTROL;
    }


    public static ApiControl getApiControlOffen() {
        return new Retrofit.Builder()
                .baseUrl("http://" + PrefUtils.getString(MyApplication.getContextObject(), "server_address", "www.baidu.com") + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(
                        new OkHttpClient.Builder()
                                .addInterceptor(new BaseInterceptor())
                                .build()
                )
                .build().create(ApiControl.class);
    }

}
