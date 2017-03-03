package com.cgstate.boxmobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgstate.boxmobile.R;
import com.cgstate.boxmobile.activities.ViewDetailActivity;
import com.cgstate.boxmobile.bean.GoodsBean;
import com.cgstate.boxmobile.global.Constant;
import com.cgstate.boxmobile.utils.DensityUtils;
import com.cgstate.boxmobile.viewholder.OKViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */

public class MyOKDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    public List<GoodsBean.DataBean> getData() {
        return mDatas;
    }

    public void setData(List<GoodsBean.DataBean> goodss) {
        this.mDatas = goodss;
    }

    private List<GoodsBean.DataBean> mDatas;

    public MyOKDataAdapter(GoodsBean goodsBean, Context mContext) {
        if (goodsBean != null) {
            this.mDatas = goodsBean.data;
        }
        this.mContext = mContext;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OKViewHolder(LayoutInflater.from(mContext).inflate(R.layout.data_ok_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        OKViewHolder okViewHolder = (OKViewHolder) holder;
        okViewHolder.tvName.setText(mDatas.get(position).goods_name);
        okViewHolder.tvWeight.setText(mDatas.get(position).goods_weight);
        okViewHolder.tvColor.setText(mDatas.get(position).goods_color);
        okViewHolder.tvMemo.setText(mDatas.get(position).goods_memo);
        Picasso.with(mContext)
//                .load(Constant.BASE_URL + mDatas.get(position).img_url[0] + "&token=" + Constant.TOKEN)
                .load(Constant.BASE_URL + mDatas.get(position).img_url[0])
                .centerCrop()
                .resize(DensityUtils.dip2px(80, mContext), DensityUtils.dip2px(80, mContext))
                .into((okViewHolder).ivPic);


        okViewHolder.llViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewDetailActivity.class);
                Bundle bundle =new Bundle();
                bundle.putSerializable("detail",mDatas.get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


}
