package com.cgstate.boxmobile.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cgstate.boxmobile.R;

public class ShowViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName;
    public TextView tvWeight;
    public TextView tvColor;
    public TextView tvMemo;
    public ImageView ivPic;
    public ImageButton btnDelete;
    public ImageButton btnEdit;
    public ProgressBar progressBar;

    public ShowViewHolder(View itemView) {
        super(itemView);
        ivPic = (ImageView) itemView.findViewById(R.id.iv_data_show);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvWeight = (TextView) itemView.findViewById(R.id.tv_weight);
        tvColor = (TextView) itemView.findViewById(R.id.tv_color);
        tvMemo = (TextView) itemView.findViewById(R.id.tv_memo);
        btnDelete = (ImageButton) itemView.findViewById(R.id.btn_delete);
        btnEdit = (ImageButton) itemView.findViewById(R.id.btn_edit);
        progressBar = (ProgressBar) itemView.findViewById(R.id.id_progress);
    }
}