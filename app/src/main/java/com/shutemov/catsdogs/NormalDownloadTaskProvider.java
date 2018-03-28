package com.shutemov.catsdogs;

public class NormalDownloadTaskProvider implements IDownloadTaskProvider {
    @Override
    public DownloadTask getDownloadTask(DownloadCallback callback) {
        return new DownloadTask(callback);
    }

}
