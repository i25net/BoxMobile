package com.cgstate.boxmobile.utils;

import android.content.Context;
import android.text.TextUtils;

import com.cgstate.boxmobile.MyApplication;
import com.cgstate.boxmobile.bean.LoginBean;
import com.cgstate.boxmobile.global.Constant;
import com.cgstate.boxmobile.netapi.ApiSubscriber;
import com.cgstate.boxmobile.netapi.MyRetrofitClient;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/1.
 */

public class LoginUtils {


    public static void login(final Context mContext) {
        String username = PrefUtils.getString(mContext, "username", "");
        String password = PrefUtils.getString(mContext, "password", "");

        HashMap<String, String> loginMap = new HashMap<>();
        loginMap.put("account", username);
        loginMap.put("password", password);

        MyRetrofitClient.getInstance(mContext)
                .getApiControl()
                .accountLogin(loginMap)
                .compose(Constant.OBSERVABLE_TRANSFORMER)
                .subscribe(new ApiSubscriber<LoginBean>(mContext,false) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        GoToLoginActivity.goToLoginActivity(mContext);
                    }

                    @Override
                    protected void doSomething(LoginBean bean) {
                        processData(bean,mContext);
                    }
                });
    }

    private static void processData(LoginBean loginBean,Context mContext) {
        if (loginBean != null) {
            if (loginBean.IsError) {
                if (!checkEmpty(loginBean.ErrorMessage)) {
                    CustomToast.showToast(mContext, loginBean.ErrorMessage, 500);
//                    GoToLoginActivity.goToLoginActivity(mContext);
                    return;
                }
            } else {
                if (loginBean.data != null) {
                    LoginBean.DataBean data = loginBean.data;
                    PrefUtils.setInt(mContext, "expired_in", data.expired_in);
                    PrefUtils.setString(mContext, "staff_name", data.staff_name);
                    PrefUtils.setString(mContext, "staff_phone", data.staff_phone);
                    PrefUtils.setString(mContext, "store_name", data.store_name);
                    PrefUtils.setString(mContext, "token", data.token);
                    PrefUtils.setBoolean(mContext, "isSavedLoginInfo", true);
                    Constant.TOKEN = data.token;
                    Constant.EXPIRED_TIME_SECONDS = data.expired_in;
                    MyApplication.startService();
                }
            }
        }
    }

    private static boolean checkEmpty(String str) {
        if (str == null) {
            return true;
        }
        return TextUtils.isEmpty(str) && (str.length() == 0);
    }

}
