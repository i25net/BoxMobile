package com.cgstate.boxmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.cgstate.boxmobile.R;
import com.cgstate.boxmobile.adapter.MyOKViewAdapter;
import com.cgstate.boxmobile.bean.GoodsBean;

import java.util.ArrayList;

public class ViewDetailActivity extends BaseActivity {


    private Toolbar mToolbar;


    private TextView tvGoodsName;
    private TextView tvGoodsWeight;
    private TextView tvGoodsColor;
    private TextView tvGoodsMemo;
    private RecyclerView mRecyclerView;


    private ArrayList<String> imgUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail);
        initViews();

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                GoodsBean.DataBean data = (GoodsBean.DataBean) bundle.getSerializable("detail");
                imgUrls = new ArrayList<>();
                for (int i = 0; i < data.img_url.length; i++) {
                    imgUrls.add(data.img_url[i]);
                }


                final MyOKViewAdapter myOKViewAdapter = new MyOKViewAdapter(mContext, imgUrls);
                mRecyclerView.setAdapter(myOKViewAdapter);
                setData(data);

                myOKViewAdapter.setOnItemClickListener(new MyOKViewAdapter.ItemClickListener() {
                    @Override
                    public void onItemClickListener(int pos) {
                        Intent intent1 = new Intent(mContext, ViewPhotoActivity.class);
                        intent1.putStringArrayListExtra("imgUrls", imgUrls);
                        intent1.putExtra("pos",pos);
                        startActivity(intent1);

                    }
                });

            }
        }
    }

    /**
     * 设置物品信息
     *
     * @param data
     */
    private void setData(GoodsBean.DataBean data) {
        tvGoodsName.setText(data.goods_name);
        tvGoodsColor.setText(data.goods_color);
        tvGoodsWeight.setText(data.goods_weight);
        tvGoodsMemo.setText(data.goods_memo);
    }


    /**
     * 初始化View
     */
    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        mToolbar.setTitle("浏览图文信息");
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        tvGoodsName = (TextView) findViewById(R.id.tv_goods_name);
        tvGoodsWeight = (TextView) findViewById(R.id.tv_goods_weight);
        tvGoodsColor = (TextView) findViewById(R.id.tv_goods_color);
        tvGoodsMemo = (TextView) findViewById(R.id.tv_goods_memo);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
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
