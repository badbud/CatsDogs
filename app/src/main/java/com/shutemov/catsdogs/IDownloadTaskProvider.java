package com.shutemov.catsdogs;

interface IDownloadTaskProvider {

    DownloadTask getDownloadTask(DownloadCallback callback);
}
