package com.cgstate.boxmobile.netapi;

/**
 * Created by Administrator on 2016/12/13.
 */

public interface ProgressDownloadListener {
    /**
     * @param bytesRead     已下载字节数
     * @param contentLength 总字节数
     * @param done          是否下载完成
     */
    void update(long bytesRead, long contentLength, boolean done);
}
