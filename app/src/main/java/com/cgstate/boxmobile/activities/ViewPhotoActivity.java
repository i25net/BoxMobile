package com.cgstate.boxmobile.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cgstate.boxmobile.R;
import com.cgstate.boxmobile.global.Constant;
import com.cgstate.boxmobile.view.ViewPagerFixed;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewPhotoActivity extends BaseActivity {

    private Toolbar mToolbar;

    private ArrayList<String> imgUrls;
    private ViewPagerFixed mViewPager;
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

            final MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();

            mViewPager.setAdapter(myViewPagerAdapter);

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


    }


    class MyViewPagerAdapter extends PagerAdapter {


        private PhotoViewAttacher photoViewAttacher;

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
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setTag(imgURL);
//            photoViewAttacher = new PhotoViewAttacher(photoView);
            Picasso.with(mContext)
                    .load(Constant.BASE_URL + imgURL)
                    .config(Bitmap.Config.RGB_565)
                    .placeholder(R.drawable.custom_progress)
                    .into(photoView);



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
