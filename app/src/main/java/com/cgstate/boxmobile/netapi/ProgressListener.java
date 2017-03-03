package com.cgstate.boxmobile.netapi;

/**
 * Created by Administrator on 2017/2/28.
 */

public interface ProgressListener {
    /**
     * @param progress  已经下载或者上传的字节数
     * @param total     总字节数
     * @param done      是否完成
     */
    void onProgress(long progress, long total, boolean done);

}


