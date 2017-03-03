package com.cgstate.boxmobile.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cgstate.boxmobile.R;

/**
 * Created by Administrator on 2017/2/24.
 */

public class PicSelectedViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivShow;
    public ImageButton btnMinus;

    public PicSelectedViewHolder(View itemView) {
        super(itemView);
        ivShow = (ImageView) itemView.findViewById(R.id.iv_pic_show);
        btnMinus = (ImageButton) itemView.findViewById(R.id.btn_minus);

    }
}
