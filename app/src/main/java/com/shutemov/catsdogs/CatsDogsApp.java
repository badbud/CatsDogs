package com.shutemov.catsdogs;

import android.app.Application;

public class CatsDogsApp extends Application {

    private static CatsDogsApp mInstance;
    private static IDownloadTaskProvider mDowloadTaskProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mDowloadTaskProvider = new NormalDownloadTaskProvider();
    }

    public static CatsDogsApp getInstance() {
        return mInstance;
    }

    public DownloadTask getDownloadTask(DownloadCallback callback) {
        return mDowloadTaskProvider.getDownloadTask(callback);
    }

}