package com.cgstate.boxmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cgstate.boxmobile.R;
import com.cgstate.boxmobile.adapter.MyDataAdapter;
import com.cgstate.boxmobile.bean.BaseBean;
import com.cgstate.boxmobile.bean.DeviceBean;
import com.cgstate.boxmobile.bean.GoodsBean;
import com.cgstate.boxmobile.bean.MyObj;
import com.cgstate.boxmobile.global.Constant;
import com.cgstate.boxmobile.netapi.ApiSubscriber;
import com.cgstate.boxmobile.netapi.MyListenter;
import com.cgstate.boxmobile.netapi.MyRetrofitClient;
import com.cgstate.boxmobile.netapi.ProgressRequestBody;
import com.cgstate.boxmobile.utils.GoToLoginActivity;
import com.cgstate.boxmobile.utils.MD5Encoder;
import com.cgstate.boxmobile.utils.PictureUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 上传图文信息页面
 */

public class UploadGoodsInfoActivity extends BaseActivity implements View.OnClickListener {
    private AppCompatSpinner mSpinner;
    private ImageButton btnInputBarcode;
    private Toolbar mToolbar;
    private EditText etBarCode;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mDeviceList;
    private ArrayAdapter<String> arrayAdapter;
    private MyDataAdapter myDataAdapter;
    private TextView btnUploadGoodsInfo;
    private ProgressBar pbUploading;
    private String mSelectedDeviceId;
    private String barcodeResult;
    private ArrayList<GoodsBean.DataBean> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_goods_info);
        initViews();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (checkEmpty(mSelectedDeviceId)) {
            initData();
        }
    }

    private void initViews() {
        mSpinner = (AppCompatSpinner) findViewById(R.id.spinner_select_device);
        mSpinner.setPrompt("设备列表");
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        btnInputBarcode = (ImageButton) findViewById(R.id.btn_input_barcode);
        btnInputBarcode.setOnClickListener(this);
        etBarCode = (EditText) findViewById(R.id.et_barcode);
        mToolbar.setTitle("上传图文信息");
        setSupportActionBar(mToolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }


        pbUploading = (ProgressBar) findViewById(R.id.pb_uploading);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_data);
        btnUploadGoodsInfo = (TextView) findViewById(R.id.btn_uploadGoodInfo);
        btnUploadGoodsInfo.setOnClickListener(this);


        myDataAdapter = new MyDataAdapter(null, mContext);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mRecyclerView.setAdapter(myDataAdapter);

//        mRecyclerView.setItemAnimator(null);

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
            case R.id.btn_uploadGoodInfo:
                checkExpired();
                break;

        }

    }

    public void checkExpired() {

        if (myDataAdapter.getData() == null) {
            showMyCustomToast("请先添加物品信息");
            return;
        }

        if (checkEditTextEmpty(etBarCode)) {
            showMyCustomToast("请先输入条码信息");
            return;
        }
        uploadGoodsInfo();
    }

    class TempObject {
        public GoodsBean.DataBean dataBean;
        public ArrayList<String> newFileLists;

        public TempObject(GoodsBean.DataBean dataBean, ArrayList<String> newFileLists) {
            this.dataBean = dataBean;
            this.newFileLists = newFileLists;
        }
    }

    /**
     * 返回压缩好的图片集合
     * 键值对,键为原始databean信息,值为压缩后的图片位置集合
     *
     * @param dataBean
     * @return
     */


    private TempObject compressImg(GoodsBean.DataBean dataBean) {


        String destFileUrl = getExternalCacheDir().getAbsolutePath() + "/";

        ArrayList<String> newCompressFileUrlList = new ArrayList<>();


        for (int i = 0; i < dataBean.img_url.length; i++) {

            String srcFileUrl = dataBean.img_url[i];

            String newCompressFileUrl = destFileUrl + MD5Encoder.encode(srcFileUrl) + ".jpg";

            boolean ok = PictureUtil.compressAndSave2(srcFileUrl, newCompressFileUrl, 800);

            if (ok) {
                newCompressFileUrlList.add(newCompressFileUrl);
            }
        }

        for (int i = 0; i < newCompressFileUrlList.size(); i++) {
            Log.d("UploadGoodsInfoActivity", "newCompressFileUrlList:-----" + newCompressFileUrlList.get(i));
        }

        TempObject tempObject = new TempObject(dataBean, newCompressFileUrlList);

        return tempObject;
    }

    private int uploadIndex = 0;

    /**
     * 上传物品信息
     */
    private void uploadGoodsInfo() {
        //开始上传就显示上传ProgressBar
        pbUploading.setVisibility(View.VISIBLE);

        barcodeResult = getEditTextString(etBarCode);

        data = myDataAdapter.getData();
        Subscriber<TempObject> subscriber = new Subscriber<TempObject>() {

            @Override
            public void onError(Throwable e) {
                Log.d("UploadGoodsInfoActivity", "onError" + e.getMessage());
                Log.d("UploadGoodsInfoActivity", "压错错误");
            }

            @Override
            public void onCompleted() {
                Log.d("UploadGoodsInfoActivity", "压缩完毕");

            }

            @Override
            public void onNext(TempObject tempObject) {
                Log.d("UploadGoodsInfoActivity", "开始上传");
                uploadCompressFile(tempObject);
            }
        };

        Observable<TempObject> observable = Observable.create(new Observable.OnSubscribe<TempObject>() {

            @Override
            public void call(Subscriber<? super TempObject> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(compressImg(data.get(uploadIndex)));
                    subscriber.onCompleted();
                }
            }
        });

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    private void uploadCompressFile(TempObject tempObject) {
        GoodsBean.DataBean dataBean = tempObject.dataBean;
        ArrayList<String> newFileLists = tempObject.newFileLists;

        MyObj myObj = new MyObj();

        HashMap<String, RequestBody> photoMap = new HashMap<>();

        for (int k = 0; k < newFileLists.size(); k++) {

            File file = new File(newFileLists.get(k));

//                UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(file, new DefaultProgressListener(mHandler, i));

            int index = uploadIndex;
            int definiteNumber = k;
            int totalUpLoadImagesNumber = newFileLists.size();

            ProgressRequestBody uploadFileRequestBody =
                    new ProgressRequestBody(
                            RequestBody.create(MediaType.parse("multipart/form-data"), file),
                            new MyListenter(
                                    mHandler,
                                    index,
                                    totalUpLoadImagesNumber,
                                    definiteNumber, myObj));
            photoMap.put("photos" + uploadIndex + "\"; filename=\"" + file.getName(), uploadFileRequestBody);
            photoMap.put(file.getName(), uploadFileRequestBody);
        }


        MultipartBody.Part dev_id = MultipartBody.Part.createFormData("dev_id", mSelectedDeviceId);
        MultipartBody.Part barcode = MultipartBody.Part.createFormData("barcode", barcodeResult);
        MultipartBody.Part description = MultipartBody.Part.createFormData("description", dataBean.toString());

        MyRetrofitClient.getInstance().getApiControl()
                .uploadInfoLoop(dev_id, barcode, description, photoMap)
                .compose(Constant.OBSERVABLE_TRANSFORMER)
                .subscribe(new ApiSubscriber<BaseBean>(mContext, false) {

                    @Override
                    public void onStart() {
                        super.onStart();

                    }

                    @Override
                    protected void doSomething(BaseBean bean) {
                        processUpLoadData(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        pbUploading.setVisibility(View.INVISIBLE);
                        showMyCustomToast("上传失败,请重新上传!");
                        return;
                    }
                });
    }


    /**
     * 处理上传数据
     *
     * @param bean
     */
    private void processUpLoadData(BaseBean bean) {

        if (bean.IsError) {//有错误
            if ("1".equals(bean.ErrorCode)) {
                showMyCustomToast(bean.ErrorMessage);
                GoToLoginActivity.goToLoginActivity(mContext);
            } else {
                showMyCustomToast(bean.ErrorMessage);
            }
        } else {//无错误提示
            uploadIndex++;
            if (uploadIndex == myDataAdapter.getData().size()) {
                pbUploading.setVisibility(View.VISIBLE);
                showMyCustomToast("全部图文信息上传成功!");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 300);
            } else {
                uploadGoodsInfo();
            }
        }

    }


    /**
     * 处理进度条
     *
     * @param index 第index个条目
     */

    private void doUpDateProgress(int index, int totalUpLoadImagesNumber, int definiteNumber, int progress) {
//        Log.d("UploadGoodsInfoActivity",
//                "index:" + index
//                        + "---totalUpLoadImagesNumber:" + totalUpLoadImagesNumber
//                        + "---definiteNumber:" + definiteNumber
//                        + "---progress:" + progress
//
//
//        );


        int maxProgress = totalUpLoadImagesNumber * 100;

        myDataAdapter.getData().get(index).maxProgress = maxProgress;

        myDataAdapter.getData().get(index).progress = progress;

        //保留动画
//        myDataAdapter.notifyItemChanged(index);
        myDataAdapter.notifyDataSetChanged();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            doUpDateProgress(msg.what, (int) msg.obj, msg.arg1, msg.arg2);
        }


    };

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
            case Constant.GOODS_INFO_ADD_REQUEST_CODE:
                if (resultCode == Constant.GOODS_INFO_ADD_SUCCESS_RESULTCODE) {
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        GoodsBean.DataBean goodsDataBean = (GoodsBean.DataBean) bundle.getSerializable("goodsinfo");
                        fillRecyclerViewWithData(goodsDataBean, -1);
                    }
                }
                break;

            case Constant.GOODS_INFO_EDIT_REQUEST_CODE:
                if (resultCode == Constant.GOODS_INFO_EDIT_SUCCESS_RESULTCODE) {
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        int index = data.getIntExtra("index", -1);
                        GoodsBean.DataBean goodsDataBean = (GoodsBean.DataBean) bundle.getSerializable("goodsinfo");
                        fillRecyclerViewWithData(goodsDataBean, index);
                    }
                }
                break;

        }
    }

    ArrayList<GoodsBean.DataBean> datas = new ArrayList<>();


    /**
     * 填充recyclerView数据
     *
     * @param goodsDataBean
     * @param index         如果是-1,添加内容
     *                      不是-1,为修改内容,index为修改的内容条目索引
     */
    private void fillRecyclerViewWithData(GoodsBean.DataBean goodsDataBean, int index) {
        Log.d("UploadGoodsInfoActivity", "index:" + index);
        if (index == -1) {
            datas.add(goodsDataBean);
        } else {
            datas.set(index, goodsDataBean);
        }
        myDataAdapter.setData(datas);
        myDataAdapter.notifyDataSetChanged();
    }

    /**
     * 获取设备id
     */

    public void initData() {
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
