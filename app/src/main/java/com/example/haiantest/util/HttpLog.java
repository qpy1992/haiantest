package com.example.haiantest.util;

import android.util.Log;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author: FX
 * Eamail: 876111689@qq.com
 * Time: 2019-11-13
 * Description: HttpLog
 */

public class HttpLog implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
        Log.d("HttpLogInfo", message);
    }
}

