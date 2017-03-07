package com.cgstate.boxmobile.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cgstate.boxmobile.R;

/**
 * Created by Administrator on 2017/3/1.
 */

public class OKDetailViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivDetailShow;
    public RelativeLayout rlShowBigPic;
    public ProgressBar pbLoading;

    public OKDetailViewHolder(View itemView) {
        super(itemView);
        ivDetailShow = (ImageView) itemView.findViewById(R.id.iv_detail_show);
        rlShowBigPic = (RelativeLayout) itemView.findViewById(R.id.rl_show_big_pic);
        pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
    }
}
