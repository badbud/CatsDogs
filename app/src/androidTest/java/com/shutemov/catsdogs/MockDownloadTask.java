package com.shutemov.catsdogs;

import java.io.IOException;
import java.net.URL;
import static java.lang.Thread.sleep;

public class MockDownloadTask extends DownloadTask {

    static private final String RES_HEADER = "{\"title\": \"Uploads from everyone\"," +
            "\"link\": \"https:\\/\\/www.flickr.com\\/photos\\/\"," +
            "\"description\": \"\"," +
            "\"modified\": \"2018-03-27T17:50:26Z\"," +
            "\"generator\": \"https:\\/\\/www.flickr.com\"," +
            "\"items\": [";

    static private final String RES_ITEM =
            "{" +
            "\"title\": \"%s\"," +
            "\"link\": \"%s\"," +
            "\"media\": {\"m\":\"%s\"}," +
            "\"date_taken\": \"%s\",\n" +
            "\"description\": \"%s\" ," +
            "\"published\": \"%s\"," +
            "\"author\": \"%s\"," +
            "\"author_id\": \"%s\"," +
            "\"tags\": \"%s\"" +
            "}";

    static private final String RES_FOOTER = "]}";
    static private final int kItemsCount = 20;

    MockDownloadTask(DownloadCallback<Result> callback) {
        super(callback);
    }

    @Override
    protected byte[] downloadUrl(URL url) throws IOException {

        // Emulating network latency
        try {
            sleep(300);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String res = RES_HEADER;
        String title = "Title ";
        String link = "https://www.flickr.com/photos/149410886@N08/26198941947/";
        String media = "https:\\/\\/farm1.staticflickr.com\\/797\\/26198941947_e7c047234a_m.jpg";
        String date_taken = "2018-05-12T11:40:25+03:00";
        String description = "Test Description";
        String date_published = "2016-01-11T13:11:37Z";
        String author = "Some author";
        String author_id = "987654321";
        String tags = "";
        String prefix = "stuff";

        if (url.toString().contains("cat")) {
            tags += "cats";
            title = "Kitten ";
            prefix = "cat";
        } else if (url.toString().contains("dog")) {
            tags += "dogs";
            title = "Puppy ";
            prefix = "dog";
        }

        for (int i  = 0; i < kItemsCount; ++i) {
            Integer ii = new Integer(i);
            Integer index = new Integer(i % 4 + 1) ;
            media = "file:///android_asset/" + prefix + index.toString() + ".jpg" ;
            String item = String.format(RES_ITEM, title + ii.toString(), link, media,
                    date_taken, description, date_published, author, author_id, tags);
            res += item;
            if (i != kItemsCount - 1) {
                res += ",";
            }
        }
        res += RES_FOOTER;
        return res.getBytes();
    }

}
