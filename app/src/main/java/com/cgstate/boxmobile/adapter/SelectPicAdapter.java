package com.cgstate.boxmobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.cgstate.boxmobile.R;
import com.cgstate.boxmobile.activities.AddInfoActivity;
import com.cgstate.boxmobile.global.Constant;
import com.cgstate.boxmobile.utils.DensityUtils;
import com.cgstate.boxmobile.viewholder.AddPicViewHolder;
import com.cgstate.boxmobile.viewholder.PicSelectedViewHolder;

import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by Administrator on 2017/2/24.
 */

public class SelectPicAdapter extends RecyclerView.Adapter {

    private ArrayList<String> mFileList = new ArrayList<>();


    private static final int UP_FILES_NUMBERS= Constant.UPLOAD_FILES_LIMIT_NUMBERS;

    private Context mContext;
    private static final int REQUEST_IMAGE = 2;

    private static final int show = 1;
    private static final int empty = -1;

    public SelectPicAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<String> getData() {
        return mFileList;
    }

    public void addData(ArrayList<String> addData) {
        mFileList.addAll(addData);
        notifyDataSetChanged();
    }

    public void setData(ArrayList<String> datas) {
        this.mFileList = datas;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == empty) {
            return new AddPicViewHolder(LayoutInflater.from(mContext).inflate(R.layout.pic_add_layout, parent, false));
        } else {
            return new PicSelectedViewHolder(LayoutInflater.from(mContext).inflate(R.layout.pic_selected_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof AddPicViewHolder) {
            ((AddPicViewHolder) holder).btnAddPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number;
                    if (mFileList == null) {
                        number = 0;
                    } else {
                        number = mFileList.size();
                    }

                    MultiImageSelector.create()
                            .showCamera(true) // 是否显示相机. 默认为显示
                            .count(UP_FILES_NUMBERS - number) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                            .start((AddInfoActivity) mContext, REQUEST_IMAGE);
                }
            });
        } else if (holder instanceof PicSelectedViewHolder) {
            final int pos = holder.getLayoutPosition();
            ((PicSelectedViewHolder) holder).btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFileList.remove(pos);
                    notifyItemRemoved(pos);
                    if (pos != mFileList.size()) { // 如果移除的是最后一个，忽略
                        notifyItemRangeChanged(pos, mFileList.size() - pos);
                    }
                }
            });

            String filePos = mFileList.get(position);
            int width = DensityUtils.dip2px(100, mContext);
            Glide.with(mContext)
                    .load(filePos)
                    .centerCrop()
                    .override(width, width)
                    .into(((PicSelectedViewHolder) holder).ivShow);


//            Picasso.with(mContext)
//                    .load("file://" + mFileList.get(position))
//                    .centerCrop()
//                    .resize(DensityUtils.dip2px(100, mContext), DensityUtils.dip2px(100, mContext))
//                    .into(((PicSelectedViewHolder) holder).ivShow);
        }


    }

    @Override
    public int getItemCount() {
        if (mFileList.size() == 0) {
            return 1;
        }
        if (mFileList.size() == UP_FILES_NUMBERS) {
            return UP_FILES_NUMBERS;
        }
        return mFileList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
//        Log.d("SelectPicAdapter", "mFileList.size():AAAAAAAA===" + mFileList.size());
        if (position < mFileList.size()) {
//            Log.d("SelectPicAdapter", "position:----show------" + position);
            return show;
        } else {
//            Log.d("SelectPicAdapter", "position:----empty----" + position);
            return empty;
        }
    }
}
