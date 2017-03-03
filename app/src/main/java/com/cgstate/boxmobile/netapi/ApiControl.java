package com.cgstate.boxmobile.netapi;


import com.cgstate.boxmobile.bean.BaseBean;
import com.cgstate.boxmobile.bean.DeviceBean;
import com.cgstate.boxmobile.bean.GoodsBean;
import com.cgstate.boxmobile.bean.LoginBean;
import com.cgstate.boxmobile.global.Constant;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/1.
 */

public interface ApiControl {


    /**
     * 登陆
     * account    //账号
     * password   //密码
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constant.APP_LOGIN)
    Observable<LoginBean> accountLogin(@FieldMap() HashMap<String, String> params);

    /**
     * 获取设备简略信息列表api
     *
     * @return
     */
    @POST(Constant.GET_DEVICE_INFO)
    Observable<DeviceBean> getDeviceInfo();


    /**
     * 获取图文信息
     * barcode       //条码  string
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constant.GET_BARCODE_CONTAIN_DATA)
    Observable<GoodsBean> getBarCodeInfoContainDatas(@FieldMap() HashMap<String, String> params);


    /**
     * 上传图文信息,一次一个,loop循环使用
     *
     * @return
     */
    @Multipart
    @POST(Constant.UPLOAD_BARCODE_CONTAIN_DATA)
    Observable<BaseBean> uploadInfoLoop(
            @Part MultipartBody.Part dev_id,
            @Part MultipartBody.Part barcode,
            @Part MultipartBody.Part description,
            @PartMap HashMap<String, RequestBody> photo);

}
