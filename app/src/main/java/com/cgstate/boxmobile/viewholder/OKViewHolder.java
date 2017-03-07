package com.cgstate.boxmobile.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cgstate.boxmobile.R;

public class OKViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName;
    public TextView tvWeight;
    public TextView tvColor;
    public TextView tvMemo;
    public ImageView ivPic;
    public LinearLayout llViewDetail;
    public ProgressBar pbLoading;

    public OKViewHolder(View itemView) {
        super(itemView);
        ivPic = (ImageView) itemView.findViewById(R.id.iv_data_show);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvWeight = (TextView) itemView.findViewById(R.id.tv_weight);
        tvColor = (TextView) itemView.findViewById(R.id.tv_color);
        tvMemo = (TextView) itemView.findViewById(R.id.tv_memo);
        llViewDetail = (LinearLayout) itemView.findViewById(R.id.ll_view_detail);
        pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
    }
}