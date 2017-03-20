package com.cgstate.boxmobile.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgstate.boxmobile.MyApplication;
import com.cgstate.boxmobile.R;
import com.cgstate.boxmobile.utils.ActivityCollector;
import com.cgstate.boxmobile.utils.PrefUtils;

import java.io.File;
import java.math.BigDecimal;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView ivAbout;
    private RelativeLayout btnClearCache;
    private TextView tvCacheSize;
    private Button btnExitApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();


    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        mToolbar.setTitle("关于软件");
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }


        ivAbout = (TextView) findViewById(R.id.iv_about);
        btnClearCache = (RelativeLayout) findViewById(R.id.btn_clear_cache);
        tvCacheSize = (TextView) findViewById(R.id.tv_cache_size);
        btnExitApp = (Button) findViewById(R.id.btn_exit_app);


        btnClearCache.setOnClickListener(this);
        btnExitApp.setOnClickListener(this);

        tvCacheSize.setText(getTotalCacheSize());

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear_cache:
                clearAllCache();
                break;
            case R.id.btn_exit_app:
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

                ActivityCollector.finishAll();

                openAnotherActivity(LoginActivity.class);
            }
        });
        builder.show();
    }


    /**
     * 清除缓存
     */
    public void clearAllCache() {
        deleteDir(mContext.getCacheDir());

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (deleteDir(mContext.getExternalCacheDir())) {
                tvCacheSize.setText(getTotalCacheSize());
            }
        }
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 获取缓存大小
     *
     * @return
     * @throws Exception
     */
    public String getTotalCacheSize() {
        long cacheSize = 0;
        try {
            cacheSize = getFolderSize(mContext.getCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                cacheSize += getFolderSize(mContext.getExternalCacheDir());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return getFormatSize(cacheSize);
    }


    // 获取文件大小
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

}
