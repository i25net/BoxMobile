package com.cgstate.boxmobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgstate.boxmobile.R;
import com.cgstate.boxmobile.global.Constant;
import com.cgstate.boxmobile.utils.DensityUtils;
import com.cgstate.boxmobile.viewholder.OKDetailViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/1.
 */

public class MyOKViewAdapter extends RecyclerView.Adapter {
    private ArrayList<String> imgUrls;
    private Context mContext;

    private ItemClickListener itemClickListener;


    public interface ItemClickListener {
        void onItemClickListener(int pos);
    }


    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MyOKViewAdapter(Context mContext, ArrayList<String> imgUrls) {
        this.imgUrls = imgUrls;
        this.mContext = mContext;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OKDetailViewHolder(LayoutInflater.from(mContext).inflate(R.layout.data_ok_detail_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OKDetailViewHolder) {
            final OKDetailViewHolder okDetailViewHolder = (OKDetailViewHolder) holder;
            Picasso.with(mContext)
//                .load(Constant.BASE_URL + mDatas.get(position).img_url[0] + "&token=" + Constant.TOKEN)
                    .load(Constant.BASE_URL + imgUrls.get(position))
                    .centerCrop()
                    .resize(DensityUtils.dip2px(80, mContext), DensityUtils.dip2px(80, mContext))
                    .into(okDetailViewHolder.ivDetailShow);

            okDetailViewHolder.rlShowBigPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = okDetailViewHolder.getLayoutPosition();
                    itemClickListener.onItemClickListener(pos);
                }
            });


        }

    }

    @Override
    public int getItemCount() {
        return imgUrls.size();
    }
}
