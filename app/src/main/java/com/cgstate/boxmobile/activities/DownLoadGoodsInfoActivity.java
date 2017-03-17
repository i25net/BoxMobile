package com.cgstate.boxmobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cgstate.boxmobile.R;
import com.cgstate.boxmobile.adapter.MyOKDataAdapter;
import com.cgstate.boxmobile.bean.DeviceBean;
import com.cgstate.boxmobile.bean.GoodsBean;
import com.cgstate.boxmobile.global.Constant;
import com.cgstate.boxmobile.netapi.ApiSubscriber;
import com.cgstate.boxmobile.netapi.MyRetrofitClient;
import com.cgstate.boxmobile.utils.GoToLoginActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class DownLoadGoodsInfoActivity extends BaseActivity implements View.OnClickListener {
    private AppCompatSpinner mSpinner;
    private ImageButton btnInputBarcode;
    private Toolbar mToolbar;
    private EditText etBarCode;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mDeviceList;
    private ArrayAdapter<String> arrayAdapter;
    private String mSelectedDeviceId;

    private TextView btnDownLoadGoodsInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_goods_info);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initViews() {
        mSpinner = (AppCompatSpinner) findViewById(R.id.spinner_select_device);
        mSpinner.setPrompt("设备列表");
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        btnInputBarcode = (ImageButton) findViewById(R.id.btn_input_barcode);
        btnInputBarcode.setOnClickListener(this);
        etBarCode = (EditText) findViewById(R.id.et_barcode);
        mToolbar.setTitle("查看图文信息");
        setSupportActionBar(mToolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_data_show);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));


        btnDownLoadGoodsInfo = (TextView) findViewById(R.id.btn_downLoadGoodInfo);
        btnDownLoadGoodsInfo.setOnClickListener(this);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mDeviceList != null) {
                    mSelectedDeviceId = mDeviceList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_input_barcode:
                openInputBarCodeActivity();
                break;
            case R.id.btn_downLoadGoodInfo:
                downLoadGoodsInfo();
                break;
        }

    }

    /**
     * 下载图文信息
     */
    private void downLoadGoodsInfo() {
        if (checkEditTextEmpty(etBarCode)) {
            showMyCustomToast("请先输入条码信息");
            return;
        }
        getBarCodeInfoMethod();
    }

    private void getBarCodeInfoMethod() {
        HashMap<String, String> params = new HashMap<>();
        params.put("barcode", getEditTextString(etBarCode));
        MyRetrofitClient.getInstance().getApiControl()
                .getBarCodeInfoContainDatas(params)
                .compose(Constant.OBSERVABLE_TRANSFORMER)
                .subscribe(new ApiSubscriber<GoodsBean>(mContext) {
                    @Override
                    protected void doSomething(GoodsBean goodsBean) {
                        if(goodsBean.data.size()>0){
                            processGoodsBean(goodsBean);
                        }else {
                            showMyCustomToast("该条形码下无数据!");
                        }
                    }
                });
    }


    /**
     * 处理图文信息
     *
     * @param goodsBean
     */
    private void processGoodsBean(GoodsBean goodsBean) {
        if (goodsBean.IsError) {
            if ("1".equals(goodsBean.ErrorCode)) {
                showMyCustomToast(goodsBean.ErrorMessage);
                GoToLoginActivity.goToLoginActivity(mContext);
                return;
            } else {
                showMyCustomToast(goodsBean.ErrorMessage);
            }
        } else {
            fillRecyclerViewWithData(goodsBean);
        }
    }

    /**
     * 用数据填充RecyclerView
     *
     * @param goodsBean
     */
    private void fillRecyclerViewWithData(GoodsBean goodsBean) {
        MyOKDataAdapter myOKDataAdapter = new MyOKDataAdapter(goodsBean, mContext);
        mRecyclerView.setAdapter(myOKDataAdapter);
        hideKeyboard();
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 打开扫描条形码的界面
     */

    private void openInputBarCodeActivity() {
        Intent intent = new Intent(mContext, CodeScanActivity.class);
        startActivityForResult(intent, Constant.BARCODE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.BARCODE_REQUEST_CODE:
                if (resultCode == Constant.BARCODE_SCAN_SUCCESS_RESULTCODE) {
                    if (data != null) {
                        String barcodeResult = data.getStringExtra("barcode");
                        etBarCode.setText(barcodeResult);
                    }
                }
                break;
        }
    }

    /**
     * 获取设备id
     */

    private void initData() {
        MyRetrofitClient.getInstance().getApiControl().getDeviceInfo()
                .compose(Constant.OBSERVABLE_TRANSFORMER)
                .subscribe(new ApiSubscriber<DeviceBean>(mContext) {
                    @Override
                    protected void doSomething(DeviceBean deviceBean) {
                        processData(deviceBean);
                    }
                });
    }

    private void processData(DeviceBean deviceBean) {
        if (deviceBean != null) {
            if (deviceBean.IsError) {
                if ("1".equals(deviceBean.ErrorCode)) {
                    GoToLoginActivity.goToLoginActivity(mContext);
                }
                if (!checkEmpty(deviceBean.ErrorMessage)) {
                    showMyCustomToast(deviceBean.ErrorMessage);
                    return;
                }
            } else {
                if (deviceBean.data != null) {
                    mDeviceList = new ArrayList<>();
                    for (DeviceBean.DataBean data : deviceBean.data) {
                        mDeviceList.add(data.id);
                    }
//测试添加数据
//                    for (int i = 0; i < 100; i++) {
//                        mDeviceList.add(new RandomGUID() + "" + new RandomGUID());
//                    }

                    arrayAdapter = new ArrayAdapter<>(mContext, R.layout.spinner_pop, mDeviceList);
                    mSpinner.setAdapter(arrayAdapter);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
