package com.cgstate.boxmobile.utils;


import com.cgstate.boxmobile.global.Constant;

/**
 * Created by Administrator on 2016/6/8.
 */
public class GenerateRandom {

    public static String getTimestamp() {
        return System.currentTimeMillis() / 1000 + "";
    }

    public static String getNonce() {
        return new RandomGUID(true).valueAfterMD5;
    }

    public static String getSignature(String nonce, String timestamp) {
        return MD5Encoder.encode(nonce + timestamp + Constant.PRIVATE_KEY);
    }

}
