package com.cgstate.boxmobile.netapi;

public interface ProgressUploadListener {
    void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish);
}