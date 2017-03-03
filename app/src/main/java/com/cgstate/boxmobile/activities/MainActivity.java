package com.cgstate.boxmobile.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.cgstate.boxmobile.MyApplication;
import com.cgstate.boxmobile.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private Button button;
    private Button button2;
    private Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initService();

    }

    private void initService() {
        MyApplication.startService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.stopService();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        mToolbar.setTitle("主页");
        setSupportActionBar(mToolbar);


        button = (Button) findViewById(R.id.btn_start_upload);
        button2 = (Button) findViewById(R.id.btn_start_download);
        btnExit = (Button) findViewById(R.id.btn_exit);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_upload:
                openAnotherActivity(UploadGoodsInfoActivity.class);
                break;
            case R.id.btn_start_download:
                openAnotherActivity(DownLoadGoodsInfoActivity.class);
                break;
            case R.id.btn_exit:
                MyApplication.stopService();
                System.exit(0);
                break;
        }
    }
}
