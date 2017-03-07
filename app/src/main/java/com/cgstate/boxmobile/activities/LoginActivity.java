package com.cgstate.boxmobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.cgstate.boxmobile.MyApplication;
import com.cgstate.boxmobile.R;
import com.cgstate.boxmobile.bean.LoginBean;
import com.cgstate.boxmobile.global.Constant;
import com.cgstate.boxmobile.netapi.ApiSubscriber;
import com.cgstate.boxmobile.netapi.MyRetrofitClient;
import com.cgstate.boxmobile.utils.PrefUtils;

import java.util.HashMap;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextInputLayout usernameWrapper;
    private TextInputLayout passwordWrapper;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        initViews();
    }

    private void initViews() {
        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
//        usernameWrapper.setHint("请输入用户名");
//        passwordWrapper.setHint("请输入密码");

        etUsername = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);


        etUsername.setText(PrefUtils.getString(mContext, "username", ""));
        etPassword.setText(PrefUtils.getString(mContext, "password", ""));



    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isSavedLoginInfo = PrefUtils.getBoolean(mContext, "isSavedLoginInfo", false);
        if(isSavedLoginInfo){
            gotoLogin(PrefUtils.getString(mContext, "username", ""),PrefUtils.getString(mContext, "password", ""));
        }
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (!validateUsername(username)) {
            usernameWrapper.setError("用户名不能为空");
            passwordWrapper.setErrorEnabled(false);
            return;
        } else if (!validatePassword(password)) {
            passwordWrapper.setError("密码不能为空");
            usernameWrapper.setErrorEnabled(false);
            return;
        } else {
            usernameWrapper.setErrorEnabled(false);
            passwordWrapper.setErrorEnabled(false);
            gotoLogin(username, password);
        }
    }

    private boolean validatePassword(String password) {
        return password.length() > 0;
    }

    private boolean validateUsername(String username) {
        return username.length() > 0;
    }


    /**
     * 登陆咯
     *
     * @param username
     * @param password
     */
    private void gotoLogin(String username, String password) {
        HashMap<String, String> loginMap = new HashMap<>();
        loginMap.put("account", username);
        loginMap.put("password", password);

        MyRetrofitClient.getInstance(mContext)
                .getApiControl()
                .accountLogin(loginMap)
                .compose(Constant.OBSERVABLE_TRANSFORMER)
                .subscribe(new ApiSubscriber<LoginBean>(mContext) {

                    @Override
                    protected void doSomething(LoginBean bean) {
                        processData(bean);
                    }
                });

    }

    /**
     * 处理登陆信息
     *
     * @param loginBean
     */
    private void processData(LoginBean loginBean) {
        if (loginBean != null) {
            if (loginBean.IsError) {
                if (!checkEmpty(loginBean.ErrorMessage)) {
                    showMyCustomToast(loginBean.ErrorMessage);
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


                    //记录用户名密码
                    PrefUtils.setString(mContext, "username", getEditTextString(etUsername));
                    PrefUtils.setString(mContext, "password", getEditTextString(etPassword));


                    if (getIntent() != null) {
                        if (getIntent().getBooleanExtra("openlogin", false)) {
                            MyApplication.startService();
                            finish();
                            return;
                        }
                    }
//                    openAnotherActivity(MainActivity.class, FINISH_THIS_ACTIVITY);
//                    openAnotherActivity(HomeActivity.class, FINISH_THIS_ACTIVITY);

                    Intent intent = new Intent(mContext, HomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("login", data);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();

                }
            }
        }
    }
}
