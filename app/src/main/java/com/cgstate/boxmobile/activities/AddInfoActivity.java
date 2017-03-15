package com.cgstate.boxmobile.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.cgstate.boxmobile.R;
import com.cgstate.boxmobile.adapter.SelectPicAdapter;
import com.cgstate.boxmobile.bean.GoodsBean;
import com.cgstate.boxmobile.global.Constant;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class AddInfoActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;


    private EditText etGoodsName;
    private EditText etGoodsWeight;
    private EditText etGoodsColor;
    private EditText etGoodsMemo;
    private int width;

    private int index = -1;


    private RecyclerView mRecyclerView;
    private SelectPicAdapter selectPicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        initViews();

        WindowManager wm = this.getWindowManager();

        width = wm.getDefaultDisplay().getWidth();

        Intent intent = getIntent();
        if (intent != null) {
            index = intent.getIntExtra("index", -1);
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                GoodsBean.DataBean dataBean = (GoodsBean.DataBean) bundle.getSerializable("editInfo");
                if (dataBean != null) {
                    ArrayList<String> oldData = new ArrayList<>();
                    for (String imgUrl : dataBean.img_url) {
                        oldData.add(imgUrl);
                    }
                    selectPicAdapter.setData(oldData);
                }


                etGoodsName = (EditText) findViewById(R.id.et_goods_name);
                etGoodsWeight = (EditText) findViewById(R.id.et_goods_weight);
                etGoodsColor = (EditText) findViewById(R.id.et_goods_color);
                etGoodsMemo = (EditText) findViewById(R.id.et_goods_memo);

                etGoodsName.setText(dataBean.goods_name);
                etGoodsWeight.setText(dataBean.goods_weight);
                etGoodsColor.setText(dataBean.goods_color);
                etGoodsMemo.setText(dataBean.goods_memo);
//                    Picasso.with(mContext)
//                            .load("file://" + filePath)
//                            .resize(width, DensityUtils.dip2px(200, mContext))
//                            .centerInside()
//                            .into(ivGoodsPic);
            }
        }

    }

    /**
     * 初始化View
     */
    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        mToolbar.setTitle("添加图文信息");
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }


        etGoodsName = (EditText) findViewById(R.id.et_goods_name);
        etGoodsWeight = (EditText) findViewById(R.id.et_goods_weight);
        etGoodsColor = (EditText) findViewById(R.id.et_goods_color);
        etGoodsMemo = (EditText) findViewById(R.id.et_goods_memo);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_select);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3,GridLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(gridLayoutManager);

//        mRecyclerView.addItemDecoration(new SpaceItemDecoration(10));


        selectPicAdapter = new SelectPicAdapter(mContext);
        mRecyclerView.setAdapter(selectPicAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addinfo_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getIntent().getBooleanExtra("edit", false)) {//如果是编辑状态
                    if (isListEmpty(selectPicAdapter.getData())) {
                        //如果编辑状态下把文件都清除掉了,那么必须要添加图片咯
                        showMyCustomToast("未选择任何图片,请添加一张");
                        return true;
                    }
                }

                if (isListEmpty(selectPicAdapter.getData())) {//如果未选择任何图片,那么直接退出
                    hideKeyboard();
                    finish();
                } else {//如果选择了图片,进行提示是否退出
                    showUncompleteDialog();
                }
                break;
            case R.id.id_complete:
                if (isListEmpty(selectPicAdapter.getData())) {
                    showMyCustomToast("请添加一张图片");
                } else {
                    doneMyInfo();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean isListEmpty(List list) {
        if (list == null) {
            return true;
        }
        if (list.size() == 0) {
            return true;
        }
        if (list.size() > 0) {
            return false;
        }
        return true;
    }


    private void showUncompleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("退出此次编辑");
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 把信息回调回给MainActivity
     */
    private void doneMyInfo() {


        GoodsBean.DataBean goodsInfo = new GoodsBean.DataBean();

        ArrayList<String> data = selectPicAdapter.getData();


        goodsInfo.img_url = data.toArray(new String[selectPicAdapter.getData().size()]);


        if (checkEditTextEmpty(etGoodsName)) {
            goodsInfo.goods_name = "";
        } else {
            goodsInfo.goods_name = etGoodsName.getText().toString();
        }
        if (checkEditTextEmpty(etGoodsWeight)) {
            goodsInfo.goods_weight = "";
        } else {
            goodsInfo.goods_weight = etGoodsWeight.getText().toString();
        }
        if (checkEditTextEmpty(etGoodsColor)) {
            goodsInfo.goods_color = "";
        } else {
            goodsInfo.goods_color = etGoodsColor.getText().toString();
        }
        if (checkEditTextEmpty(etGoodsMemo)) {
            goodsInfo.goods_memo = "";
        } else {
            goodsInfo.goods_memo = etGoodsMemo.getText().toString();
        }


        Intent intent = getIntent();

        Bundle bundle = new Bundle();

        bundle.putSerializable("goodsinfo", goodsInfo);

        if (index >= 0) {
            intent.putExtra("index", index);
        }

        intent.putExtras(bundle);


        if (intent.getBooleanExtra("edit", false)) {
            setResult(Constant.GOODS_INFO_EDIT_SUCCESS_RESULTCODE, intent);
        } else {
            setResult(Constant.GOODS_INFO_ADD_SUCCESS_RESULTCODE, intent);
        }


        hideKeyboard();

        finish();
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
        switch (v.getId()) {
//            case R.id.iv_goods_pic:
//                pickImage();
//                break;
        }
    }

    private static final int REQUEST_IMAGE = 2;
    private ArrayList<String> mSelectPath = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> stringArrayListExtra = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//                mSelectPath.addAll(stringArrayListExtra);
                fillRVwithPic(stringArrayListExtra);
            }
        }
    }


    private void fillRVwithPic(ArrayList<String> filePathData) {
        selectPicAdapter.addData(filePathData);
        Log.d("AddInfoActivity", "selectPicAdapter.getData().size():" + selectPicAdapter.getData().size());
    }

}
