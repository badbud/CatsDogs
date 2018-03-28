package com.shutemov.catsdogs;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Implementation of AsyncTask designed to fetch data from the network.
 */
class DownloadTask extends AsyncTask<String, Integer, DownloadTask.Result> {

    private DownloadCallback<Result> mCallback;

    DownloadTask(DownloadCallback<Result> callback) {
        mCallback = callback;
    }

    /**
     * Wrapper class that serves as a union of a result value and an exception. When the download
     * task has completed, either the result value or exception can be a non-null value.
     * This allows to pass exceptions to the UI thread that were thrown during doInBackground().
     */
    static class Result {
        public byte[] mResultValue;
        public Exception mException;
        public String mURL;
        public Result(byte[] resultValue) {
            mResultValue = resultValue;
        }
        public Result(Exception exception) {
            mException = exception;
        }
    }

    @Override
    protected DownloadTask.Result doInBackground(String... urls) {
        Result result = null;
        if (!isCancelled() && urls != null && urls.length > 0) {
            String urlstring = urls[0];
            try {
                URL url = new URL(urlstring);
                byte[] resultBytes = downloadUrl(url);
                if (resultBytes != null) {
                    result = new Result(resultBytes);
                    result.mURL = urlstring;
                } else {
                    throw new IOException("No response received.");
                }
            } catch(Exception e) {
                result = new Result(e);
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(Result result) {
        if (result != null && mCallback != null) {
            mCallback.updateFromDownload(result);
            mCallback.finishDownloading();
        }
    }

    protected byte[] downloadUrl(URL url) throws IOException {

        // Setup
        HttpURLConnection connection;
        byte[] bytes;
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);

        // Work
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + responseCode);
        }
        InputStream in = new BufferedInputStream(connection.getInputStream());
        bytes = readStream(in);

        // Cleanup
        if (connection != null) {
            connection.disconnect();
        }
        return bytes;
    }

    public static byte[] readStream(InputStream stream)
            throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = stream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toByteArray();
    }
}