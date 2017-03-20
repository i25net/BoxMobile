package com.cgstate.boxmobile.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cgstate.boxmobile.R;
import com.cgstate.boxmobile.global.Constant;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2017/3/20.
 */

public class ViewDetailFragment extends BaseFragment {
    private static final String IMAGE_URL = "img_url";
    private String imageUrl;
    private PhotoView photoView;
    private ProgressBar pbLoading;
    private View view;


    public static ViewDetailFragment newInstance(String imgUrl) {
        ViewDetailFragment viewDetailFragment = new ViewDetailFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, imgUrl);
        viewDetailFragment.setArguments(args);
        return viewDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ViewDetailFragment", "onCreate");

        if (getArguments() != null) {
            imageUrl = getArguments().getString(IMAGE_URL);
            Log.d("ViewDetailFragment", imageUrl);
        }
    }


    private void initView(View view) {
        photoView = (PhotoView) view.findViewById(R.id.id_photoview);
        pbLoading = (ProgressBar) view.findViewById(R.id.pb_view_detail_loading);
    }


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_detail, container, false);
        initView(view);
        return view;
    }

    @Override
    public void fetchData() {

        if (imageUrl != null) {

            pbLoading.setVisibility(View.VISIBLE);
            String url = Constant.BASE_URL_NO_END + imageUrl;

            GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder().addHeader("token", Constant.TOKEN).build());

            Glide.with(this)
                    .load(glideUrl)
//                    .crossFade()//淡入淡出300ms
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<GlideUrl, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, GlideUrl model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        Log.d("MyViewPagerAdapter", e.getMessage());
                            pbLoading.setVisibility(View.INVISIBLE);
                            Log.d("onException", "onResourceReady");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, GlideUrl model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            pbLoading.setVisibility(View.INVISIBLE);
                            Log.d("ViewDetailFragment", "onResourceReady");
                            return false;
                        }
                    })
                    .into(photoView);
        }
    }
}
