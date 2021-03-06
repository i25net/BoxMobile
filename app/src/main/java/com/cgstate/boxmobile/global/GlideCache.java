package com.cgstate.boxmobile.global;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by Administrator on 2017/3/20.
 */

public class GlideCache implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String glideChaceDir = context.getExternalCacheDir().getAbsolutePath() + "/glide_cache";
            int cacheSize = 15 * 1024 * 1024;
            builder.setDiskCache(new DiskLruCacheFactory(glideChaceDir, cacheSize));
        }
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
//        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
//
////        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
////                .connectTimeout(Constant.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
////                .readTimeout(Constant.READ_TIMEOUT, TimeUnit.MILLISECONDS)
////                .writeTimeout(Constant.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
////                .addInterceptor(new TimeOutInterceptor())
////                .build();
////        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());

    }
}
