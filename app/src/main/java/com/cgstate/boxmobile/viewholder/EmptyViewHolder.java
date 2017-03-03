package com.cgstate.boxmobile.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.cgstate.boxmobile.R;

public class EmptyViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout rlAddData;

    public EmptyViewHolder(View itemView) {
        super(itemView);
        rlAddData = (RelativeLayout) itemView.findViewById(R.id.rl_add_data);
    }
}