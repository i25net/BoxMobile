package com.cgstate.boxmobile.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cgstate.boxmobile.R;

/**
 * Created by Administrator on 2017/2/24.
 */

public class PicSelectedViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivShow;
    public ImageButton btnMinus;
    public RelativeLayout rlContainer;

    public PicSelectedViewHolder(View itemView) {
        super(itemView);
        rlContainer = (RelativeLayout) itemView.findViewById(R.id.rl_add_container);


//        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) rlContainer.getLayoutParams();
//        layoutParams.width = ScreenUtils.getScreenWidth()/3;
//        rlContainer.setLayoutParams(layoutParams);


        ivShow = (ImageView) itemView.findViewById(R.id.iv_pic_show);
        btnMinus = (ImageButton) itemView.findViewById(R.id.btn_minus);

    }
}
