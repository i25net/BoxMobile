package com.cgstate.boxmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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
import com.cgstate.boxmobile.adapter.PhotoViewPagerAdapter;
import com.cgstate.boxmobile.fragments.ViewDetailFragment;
import com.cgstate.boxmobile.global.Constant;
import com.cgstate.boxmobile.view.ViewPagerFixed;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewPhotoActivity extends BaseActivity {

    private Toolbar mToolbar;

    private ArrayList<String> imgUrls;
    private ViewPagerFixed mViewPager;
    private ProgressBar pbLoading;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        initViews();

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            imgUrls = intent.getStringArrayListExtra("imgUrls");
        }

        if (imgUrls != null && imgUrls.size() > 0) {
            pos = intent.getIntExtra("pos", -1);

            mToolbar.setTitle("查看大图\t( " + (pos + 1) + "/" + imgUrls.size() + " )");

//            final MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();

            ArrayList<ViewDetailFragment> fragments = new ArrayList<>();
            for (int i = 0; i < imgUrls.size(); i++) {
                ViewDetailFragment viewDetailFragment = ViewDetailFragment.newInstance(imgUrls.get(i));
                fragments.add(viewDetailFragment);
            }


            mViewPager.setAdapter(new PhotoViewPagerAdapter(getSupportFragmentManager(),fragments));

            mViewPager.setCurrentItem(pos);

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    mToolbar.setTitle("查看大图\t( " + (position + 1) + "/" + imgUrls.size() + " )");
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }

    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        mToolbar.setTitle("查看大图");
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }


        mViewPager = (ViewPagerFixed) findViewById(R.id.vp_big_show);
//        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);

    }





    class MyViewPagerAdapter extends PagerAdapter {

        PhotoViewAttacher photoViewAttacher;

        public MyViewPagerAdapter() {
        }

        @Override
        public int getCount() {
            return imgUrls == null ? 0 : imgUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String imgURL = imgUrls.get(position);
            String url = Constant.BASE_URL_NO_END + imgURL;
            final PhotoView photoView = new PhotoView(container.getContext());
//            photoView.setTag(imgURL);


            GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder().addHeader("token", Constant.TOKEN).build());

            Glide.with(mContext)
                    .load(glideUrl)
//                    .crossFade()//淡入淡出300ms
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<GlideUrl, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, GlideUrl model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Log.d("MyViewPagerAdapter", e.getMessage());
                            showMyCustomToast("加载图片资源错误,请检查网络后重试");
                            pbLoading.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, GlideUrl model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            photoViewAttacher = new PhotoViewAttacher(photoView);
                            pbLoading.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(photoView);


//            Picasso.with(mContext)
//                    .load(Constant.BASE_URL_NO_END + imgURL)
//                    .config(Bitmap.Config.RGB_565)
//                    .fit()
//                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//                    .into(photoView, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            photoViewAttacher.update();
//                            pbLoading.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onError() {
//                            pbLoading.setVisibility(View.GONE);
//                        }
//                    });


            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
