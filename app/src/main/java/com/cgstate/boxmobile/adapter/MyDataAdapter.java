package com.cgstate.boxmobile.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgstate.boxmobile.R;
import com.cgstate.boxmobile.activities.AddInfoActivity;
import com.cgstate.boxmobile.activities.UploadGoodsInfoActivity;
import com.cgstate.boxmobile.bean.GoodsBean;
import com.cgstate.boxmobile.global.Constant;
import com.cgstate.boxmobile.utils.DensityUtils;
import com.cgstate.boxmobile.viewholder.EmptyViewHolder;
import com.cgstate.boxmobile.viewholder.ShowViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/20.
 */

public class MyDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int show = 1;
    private static final int empty = -1;
    private Context mContext;


    public ArrayList<GoodsBean.DataBean> getData() {
        return mDatas;
    }

    public void setData(ArrayList<GoodsBean.DataBean> goodss) {
        this.mDatas = goodss;
    }

    private ArrayList<GoodsBean.DataBean> mDatas;

    public MyDataAdapter(GoodsBean goodsBean, Context mContext) {
        if (goodsBean != null) {
            this.mDatas = goodsBean.data;
        }
        this.mContext = mContext;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == empty) {
            return new EmptyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.data_empty_layout, parent, false));
        } else {
            return new ShowViewHolder(LayoutInflater.from(mContext).inflate(R.layout.data_show_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EmptyViewHolder) {
            //如果是空白的条目,获取点击事件,打开选择图片的界面
            ((EmptyViewHolder) holder).rlAddData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //回调给上传也展示信息,
                    Intent intent = new Intent(mContext, AddInfoActivity.class);
                    ((UploadGoodsInfoActivity) mContext).startActivityForResult(intent, Constant.GOODS_INFO_ADD_REQUEST_CODE);
                }
            });
        } else if (holder instanceof ShowViewHolder) {
            ((ShowViewHolder) holder).tvName.setText(mDatas.get(position).goods_name);
            ((ShowViewHolder) holder).tvWeight.setText(mDatas.get(position).goods_weight);
            ((ShowViewHolder) holder).tvColor.setText(mDatas.get(position).goods_color);
            ((ShowViewHolder) holder).tvMemo.setText(mDatas.get(position).goods_memo);
            Picasso.with(mContext)
                    .load("file://" + mDatas.get(position).img_url[0])
                    .centerCrop()
                    .resize(DensityUtils.dip2px(80, mContext), DensityUtils.dip2px(80, mContext))
                    .into(((ShowViewHolder) holder).ivPic);

            final int pos = holder.getLayoutPosition();


            ((ShowViewHolder) holder).progressBar.setMax(mDatas.get(pos).maxProgress);
            ((ShowViewHolder) holder).progressBar.setProgress(mDatas.get(pos).progress);


            ((ShowViewHolder) holder).btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("提示");
                    builder.setMessage("确认要删除当前条目?");
                    builder.setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDatas.remove(pos);
                            notifyItemRemoved(pos);
                            if (pos != mDatas.size()) { // 如果移除的是最后一个，忽略
                                notifyItemRangeChanged(pos, mDatas.size() - pos);
                            }
                        }
                    });
                    builder.setNegativeButton("取消操作", null);
                    builder.show();
                }
            });


            ((ShowViewHolder) holder).btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AddInfoActivity.class);
                    intent.putExtra("edit", true);
                    intent.putExtra("index", pos);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("editInfo", mDatas.get(pos));
                    intent.putExtras(bundle);
                    ((UploadGoodsInfoActivity) mContext).startActivityForResult(intent, Constant.GOODS_INFO_EDIT_REQUEST_CODE);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 + 1 : mDatas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        if (mDatas == null) {
            return empty;
        }

        if (position < mDatas.size()) {
            return show;
        } else {
            return empty;
        }

    }


}
