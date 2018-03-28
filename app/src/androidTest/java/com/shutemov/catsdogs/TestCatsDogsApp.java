package com.shutemov.catsdogs;

public class TestCatsDogsApp extends CatsDogsApp {

    @Override
    public DownloadTask getDownloadTask(DownloadCallback callback) {
        return new IDownloadTaskProvider() {
            @Override
            public DownloadTask getDownloadTask(DownloadCallback callback) {
                return new MockDownloadTask(callback);
            }
        }.getDownloadTask(callback);
    }

}