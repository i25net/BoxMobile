package com.cgstate.boxmobile.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */

public class DeviceBean {



    public boolean IsError;
    public String ErrorCode;
    public String ErrorMessage;
    public String SuccessMessage;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 20170001
         */

        public String id;
    }
}
