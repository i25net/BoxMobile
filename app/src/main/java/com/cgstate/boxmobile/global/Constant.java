package com.cgstate.boxmobile.global;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/29.
 */

public class Constant {


    public static final String PRIVATE_KEY = "0d4a299b72e646e497efd03b30a8512c";//通信加密秘钥

    public static String TOKEN = "";
    public static int EXPIRED_TIME_SECONDS = -1;
    public static long LAST_RIGHT_TIME = -1;


    public static final int BARCODE_REQUEST_CODE = 10010;
    public static final int BARCODE_SCAN_SUCCESS_RESULTCODE = 200;


    public static final int GOODS_INFO_ADD_REQUEST_CODE = 10086;
    public static final int GOODS_INFO_ADD_SUCCESS_RESULTCODE = 300;


    public static final int GOODS_INFO_EDIT_REQUEST_CODE = 10000;
    public static final int GOODS_INFO_EDIT_SUCCESS_RESULTCODE = 600;


    public static final String BASE_URL = "http://192.168.9.151:8092/";
    public static final String APP_LOGIN = "Account/Login";
    public static final String GET_DEVICE_INFO = "BarCodeInfo/GetBriefDevs";
    public static final String GET_BARCODE_CONTAIN_DATA = "BarCodeInfo/GetBarCodeInfos";
    public static final String UPLOAD_BARCODE_CONTAIN_DATA = "BarCodeInfo/Upload";


    public static final Observable.Transformer OBSERVABLE_TRANSFORMER = new Observable.Transformer() {
        @Override
        public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };


    public final static long CONNECT_TIMEOUT = 10000;//连接超时时间,单位毫秒
    public final static long READ_TIMEOUT = 10000;//读取超时时间,单位毫秒
    public final static long WRITE_TIMEOUT = 1000000;//写的超时时间,单位毫秒

    public final static int UPLOAD_FILES_LIMIT_NUMBERS = 6;
}
