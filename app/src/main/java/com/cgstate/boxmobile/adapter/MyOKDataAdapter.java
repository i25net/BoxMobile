package com.cgstate.boxmobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cgstate.boxmobile.R;
import com.cgstate.boxmobile.activities.ViewDetailActivity;
import com.cgstate.boxmobile.bean.GoodsBean;
import com.cgstate.boxmobile.global.Constant;
import com.cgstate.boxmobile.global.OkHttpUrlLoader;
import com.cgstate.boxmobile.netapi.MyRetrofitClient;
import com.cgstate.boxmobile.utils.DensityUtils;
import com.cgstate.boxmobile.viewholder.OKViewHolder;

import java.io.InputStream;
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
        final OKViewHolder okViewHolder = (OKViewHolder) holder;
        okViewHolder.tvName.setText(mDatas.get(position).goods_name);
        okViewHolder.tvWeight.setText(mDatas.get(position).goods_weight);
        okViewHolder.tvColor.setText(mDatas.get(position).goods_color);
        okViewHolder.tvMemo.setText(mDatas.get(position).goods_memo);

        String url = Constant.BASE_URL_NO_END + mDatas.get(position).img_url[0];
//        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder().addHeader("token", Constant.TOKEN).build());

        int width = DensityUtils.dip2px(80, mContext);
        Glide.get(mContext)
                .register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(MyRetrofitClient.getInstance().getOkHttpClient()));


        Glide.with(mContext)
                .load(url)
                .centerCrop()
                .override(width, width)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        okViewHolder.pbLoading.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        okViewHolder.pbLoading.setVisibility(View.GONE);
                        return false;
                    }
                })
//                .listener(new RequestListener<GlideUrl, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, GlideUrl model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        okViewHolder.pbLoading.setVisibility(View.GONE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, GlideUrl model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        okViewHolder.pbLoading.setVisibility(View.GONE);
//                        return false;
//                    }
//                })
                .into(okViewHolder.ivPic);

//        Picasso.with(mContext)
//                .load(Constant.BASE_URL_NO_END + mDatas.get(position).img_url[0])
//                .centerCrop()
//                .resize(DensityUtils.dip2px(80, mContext), DensityUtils.dip2px(80, mContext))
//                .into((okViewHolder).ivPic, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        okViewHolder.pbLoading.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onError() {
//                        okViewHolder.pbLoading.setVisibility(View.GONE);
//                    }
//                });


        okViewHolder.llViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("detail", mDatas.get(position));
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
