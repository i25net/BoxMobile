package com.cgstate.boxmobile.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cgstate.boxmobile.R;

/**
 * Created by Administrator on 2017/2/24.
 */

public class AddPicViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout btnAddPic;
    public ImageView ivInsert;

    public AddPicViewHolder(View itemView) {
        super(itemView);
        btnAddPic = (RelativeLayout) itemView.findViewById(R.id.btn_add_pic);
        ivInsert = (ImageView) itemView.findViewById(R.id.iv_insert);
    }
}
