package com.cgstate.boxmobile.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgstate.boxmobile.MyApplication;
import com.cgstate.boxmobile.R;
import com.cgstate.boxmobile.bean.HomeModuleEntity;
import com.cgstate.boxmobile.bean.LoginBean;
import com.cgstate.boxmobile.impl.OnItemClickListener;
import com.cgstate.boxmobile.utils.DensityUtils;
import com.cgstate.boxmobile.utils.PrefUtils;
import com.cgstate.boxmobile.view.MyItemDecoration;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends BaseActivity implements OnItemClickListener, View.OnClickListener {
    private CircleImageView iconImage;
    private TextView tvShanghuMingcheng;
    private TextView tvShanghuMiaoshu;
    private RecyclerView rvHome;
    private ArrayList<HomeModuleEntity> mDatas;
    private int width;

    private int[] backGrounds = {R.drawable.icon_module3_selector,
            R.drawable.icon_module4_selector,
            R.drawable.ic_random1_selector,
            R.drawable.ic_random2_selector,
            R.drawable.ic_random3_selector};
    private TextView btnExitLogin;
    private ArrayList<Class> clazzList;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initDisplay();
        initService();
        initViews();
        initTitleData();
        initRecyclerViewDivider();
        initData();
    }

    private void initDisplay() {
        WindowManager wm = this.getWindowManager();

        width = wm.getDefaultDisplay().getWidth();
    }

    /**
     * 启动服务
     */
    private void initService() {
        MyApplication.startService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.stopService();
    }

    /**
     * 初始化头部内容
     */
    private void initTitleData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                LoginBean.DataBean loginData = (LoginBean.DataBean) bundle.getSerializable("login");
                tvShanghuMingcheng.setText(loginData.store_name);
                tvShanghuMiaoshu.setText(loginData.staff_name);
            }
        }
    }

    /**
     * rv添加数据
     */
    private void initData() {

        clazzList = new ArrayList<>();
        clazzList.add(UploadGoodsInfoActivity.class);//第一个
        clazzList.add(DownLoadGoodsInfoActivity.class);//第二个
        clazzList.add(AboutActivity.class);//第三个

        mDatas = new ArrayList<>();

        HomeModuleEntity module1 = new HomeModuleEntity("上传图文信息", R.drawable.icon_module1_selector);
        HomeModuleEntity module2 = new HomeModuleEntity("查看图闻信息", R.drawable.icon_module2_selector);
        HomeModuleEntity module3 = new HomeModuleEntity("关于", R.drawable.icon_about_selector);
        mDatas.add(module1);
        mDatas.add(module2);
        mDatas.add(module3);

//        for (int i = 3; i <= 6; i++) {
//            int back = backGrounds[new Random().nextInt(backGrounds.length)];
//            HomeModuleEntity homeModuleEntity = new HomeModuleEntity("模块" + i, back);
//            mDatas.add(homeModuleEntity);
//        }
        HomeDataAdapter homeDataAdapter = new HomeDataAdapter();
        rvHome.setAdapter(homeDataAdapter);

        homeDataAdapter.setOnItemClickListener(this);


    }

    /**
     * 初始化rv分割线
     */
    private void initRecyclerViewDivider() {
        gridLayoutManager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);

        rvHome.setLayoutManager(gridLayoutManager);
        //dp转换px
        int offset = DensityUtils.dip2px(1.3f, mContext);

        rvHome.addItemDecoration(new MyItemDecoration(offset));


    }


    private void initViews() {
        iconImage = (CircleImageView) findViewById(R.id.icon_image);
        tvShanghuMingcheng = (TextView) findViewById(R.id.tv_shanghu_mingcheng);
        tvShanghuMiaoshu = (TextView) findViewById(R.id.tv_shanghu_miaoshu);
        rvHome = (RecyclerView) findViewById(R.id.rv_home);
        btnExitLogin = (TextView) findViewById(R.id.btn_exit_login);
        btnExitLogin.setOnClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        if (position < clazzList.size()) {
            openAnotherActivity(clazzList.get(position));
        } else {
            showMyCustomToast("该模块暂未开放");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit_login:
                exitLogin();
                break;
        }
    }

    private void exitLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("是否要退出当前登陆账号？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyApplication.stopService();//停止服务
                PrefUtils.setBoolean(mContext, "isSavedLoginInfo", false);
                openAnotherActivity(LoginActivity.class, FINISH_THIS_ACTIVITY);
            }
        });
        builder.show();
    }


    class HomeDataAdapter extends RecyclerView.Adapter {


        private OnItemClickListener onItemClickListener;


        private void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new HomeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home2, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof HomeViewHolder) {
                HomeViewHolder homeViewHolder = (HomeViewHolder) holder;
                homeViewHolder.tvModuleName.setText(mDatas.get(position).name);


                Drawable drawable = mContext.getResources().getDrawable(mDatas.get(position).background);
                homeViewHolder.ivModuleName.setBackground(drawable);

//                homeViewHolder.tvModuleName.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                homeViewHolder.rlHomeItemContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(position);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mDatas == null ? 0 : mDatas.size();
        }
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {

        ImageView ivModuleName;
        TextView tvModuleName;
        RelativeLayout rlHomeItemContainer;

        public HomeViewHolder(View itemView) {
            super(itemView);
            rlHomeItemContainer = (RelativeLayout) itemView.findViewById(R.id.rl_home_container);
            ivModuleName = (ImageView) itemView.findViewById(R.id.iv_item_home);
            tvModuleName = (TextView) itemView.findViewById(R.id.tv_item_home);
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) rlHomeItemContainer.getLayoutParams();
            layoutParams.height = width / 3;
            rlHomeItemContainer.setLayoutParams(layoutParams);
            rlHomeItemContainer.setGravity(Gravity.CENTER);

        }
    }


}
