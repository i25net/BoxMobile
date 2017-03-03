package com.cgstate.boxmobile.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/20.
 */

public class LoginBean {


    public DataBean data;
    public boolean IsError;
    public String ErrorCode;
    public String ErrorMessage;
    public String SuccessMessage;

    @Override
    public String toString() {
        return "LoginBean{" +
                "data=" + data +
                ", IsError=" + IsError +
                ", ErrorCode='" + ErrorCode + '\'' +
                ", ErrorMessage='" + ErrorMessage + '\'' +
                ", SuccessMessage='" + SuccessMessage + '\'' +
                '}';
    }

    public static class DataBean implements Serializable {
        /**
         * token : 27f6c8a20bfc47e3939209ff4ca87b09
         * expired_in : 600
         * staff_name : 陆克俭
         * staff_phone : 13732670120
         * store_name : 测试商户
         */

        public String token;
        public int expired_in;
        public String staff_name;
        public String staff_phone;
        public String store_name;

        @Override
        public String toString() {
            return "DataBean{" +
                    "token='" + token + '\'' +
                    ", expired_in=" + expired_in +
                    ", staff_name='" + staff_name + '\'' +
                    ", staff_phone='" + staff_phone + '\'' +
                    ", store_name='" + store_name + '\'' +
                    '}';
        }
    }
}
