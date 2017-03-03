package com.cgstate.boxmobile.utils;

/**
 * Created by Administrator on 2016/12/10.
 */

public class TempURLUtils {
    public static  String tempURL;

    private static class LazyHolder {
        private static final TempURLUtils INSTANCE = new TempURLUtils();
    }

    public static final TempURLUtils getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void setTempURL(String url) {
        this.tempURL = url;
    }

    public String getTempURL() {
        return tempURL;
    }
}
