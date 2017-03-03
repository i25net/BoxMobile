package com.cgstate.boxmobile.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */

public class GoodsBean {


    public boolean IsError;
    public String ErrorCode;
    public String ErrorMessage;
    public String SuccessMessage;
    public List<DataBean> data;

    public static class DataBean implements Serializable {
        /**
         * dev_id : xxx
         * barcode : xxx
         * img_url : xxx
         * goods_name : xxx
         * goods_weight : xxx
         * goods_color : xxx
         * goods_memo : xxxx
         */

        public String dev_id;
        public String barcode;
        public String[] img_url;
        public String goods_name;
        public String goods_weight;
        public String goods_color;
        public String goods_memo;

        @Override
        public String toString() {
            return "{" +
                    "\"goods_name\":\"" + goods_name + "\"," +
                    "\"goods_weight\":\"" + goods_weight + "\"," +
                    "\"goods_color\":\"" + goods_color + "\"," +
                    "\"goods_memo\":\"" + goods_memo + "\"" +
                    '}';
        }
    }
}
