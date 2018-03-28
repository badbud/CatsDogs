package com.shutemov.catsdogs.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class FlickrContent {

    public static final String TAG_ITEMS = "items";

    class Sortbydate implements Comparator<FlickrItem>
    {
        // Used for sorting in descending order
        public int compare(FlickrItem a, FlickrItem b)
        {
            return b.date_taken.compareTo(a.date_taken);
        }
    }

    private List<FlickrItem> mItems = new ArrayList<FlickrItem>();

    public FlickrContent()
    {

    }

    /**
     * Updates list with data returned by Flickr web API in JSON format
     * @param json object returned by Flickr HTTP request
     * @throws JSONException
     */
    public void UpdateContent(JSONObject json) throws Exception
    {
        JSONArray items = json.getJSONArray(TAG_ITEMS);
        mItems.clear();
        for (int i = 0; i < items.length(); ++i) {
            mItems.add(new FlickrItem(items.getJSONObject(i)));
        }
        Collections.sort(mItems, new Sortbydate());
    }

    public int getSize()
    {
        return mItems.size();
    }

    public FlickrItem getItem(int i)
    {
        return mItems.get(i);
    }


    /**
     * An item representing a piece of flicker content.
     */
    public static class FlickrItem {

        /*  JSON Tags  */

        public static final String TAG_TITLE = "title";
        public static final String TAG_LINK = "link";
        public static final String TAG_MEDIA = "media";
        public static final String TAG_DATE_TAKEN = "date_taken";
        public static final String TAG_DESCRIPTION = "description";
        public static final String TAG_PUBLISHED = "published";
        public static final String TAG_AUTHOR = "author";
        public static final String TAG_AUTHOR_ID = "author_id";
        public static final String TAG_TAGS = "tags";

        /* Data storage */

        public final String title;
        public final String link;
        public final String media;
        public final Date date_taken;
        public final String description;
        public final Date published;
        public final String author;
        public final String author_id;
        public final String tags;

        /**
         * Constructor from JSON object
         * @param json JSONObject to use for construction
         * @throws JSONException
         */
        public FlickrItem(JSONObject json) throws Exception {

            title = json.getString(TAG_TITLE);
            link = json.getString(TAG_LINK);
            media = json.getJSONObject(TAG_MEDIA).getString("m");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

            date_taken = sdf.parse(json.getString(TAG_DATE_TAKEN));
            description = json.getString(TAG_DESCRIPTION);
            published = sdf2.parse(json.getString(TAG_PUBLISHED));
            author = json.getString(TAG_AUTHOR);
            author_id = json.getString(TAG_AUTHOR_ID);
            tags = json.getString(TAG_TAGS);
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
