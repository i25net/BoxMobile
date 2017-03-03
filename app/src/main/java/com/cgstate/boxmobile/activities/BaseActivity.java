package com.cgstate.boxmobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.cgstate.boxmobile.utils.ActivityCollector;
import com.cgstate.boxmobile.utils.CustomToast;


public class BaseActivity extends AppCompatActivity {
    public Context mContext;
    public String TAG = this.getClass().getSimpleName();
    public static final int FINISH_THIS_ACTIVITY = 10010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mContext = this;
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public void showMyCustomToast(String msg) {
        CustomToast.showToast(mContext, msg, 500);
    }

    public boolean checkEmpty(String str) {
        if (str == null) {
            return true;
        }
        return TextUtils.isEmpty(str) && (str.length() == 0);
    }


    public String getEditTextString(EditText editText) {
        if (checkEditTextEmpty(editText)) {
            return "";
        } else {
            return editText.getText().toString();
        }
    }

    public boolean checkEditTextEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString()) && (editText.getText().toString().length() == 0);
    }


    public void setTextViewContent(TextView textView, String content) {
        textView.setText(content);
    }


    public void openAnotherActivity(Class anotherActivityClass) {
        openAnotherActivity(anotherActivityClass, -1);
    }

    public void openAnotherActivity(Class anotherActivityClass, int tag) {
        Intent intent = new Intent(mContext, anotherActivityClass);
        startActivity(intent);
        if (tag > 0) {
            finish();
        }
    }

}

