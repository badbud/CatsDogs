package com.shutemov.catsdogs;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.shutemov.catsdogs.data.FlickrContent.FlickrItem.TAG_TITLE;
import static com.shutemov.catsdogs.data.FlickrContent.FlickrItem.TAG_AUTHOR;
import static com.shutemov.catsdogs.data.FlickrContent.FlickrItem.TAG_AUTHOR_ID;
import static com.shutemov.catsdogs.data.FlickrContent.FlickrItem.TAG_DATE_TAKEN;
import static com.shutemov.catsdogs.data.FlickrContent.FlickrItem.TAG_DESCRIPTION;
import static com.shutemov.catsdogs.data.FlickrContent.FlickrItem.TAG_LINK;
import static com.shutemov.catsdogs.data.FlickrContent.FlickrItem.TAG_MEDIA;
import static com.shutemov.catsdogs.data.FlickrContent.FlickrItem.TAG_PUBLISHED;
import static com.shutemov.catsdogs.data.FlickrContent.FlickrItem.TAG_TAGS;

import static com.shutemov.catsdogs.data.FlickrContent.TAG_ITEMS;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


import static junit.framework.Assert.assertEquals;
import com.shutemov.catsdogs.data.FlickrContent;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.TimeZone;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class DataUnitTest {

    // Test data
    private static final String CORRECT1_TITLE = "Sample_title 1";
    private static final String CORRECT1_LINK = "https:\\/\\/www.flickr.com\\/photos\\/user\\/12345678\\/";
    private static final String CORRECT1_MEDIA = "https:\\/\\/farm1.staticflickr.com\\/123\\/12345.jpg";
    private static final String CORRECT1_DATE = "2018-03-24T12:16:32-0800";
    private static final String CORRECT1_DESCRIPTION = "Sample description";
    private static final String CORRECT1_PUBDATE = "2018-05-12T11:40:25+0300";
    private static final String CORRECT1_AUTH_NAME = "nobody@flickr.com (\\\"SomeAuthor\\\")";
    private static final String CORRECT1_AUTH_ID = "147976818@N03";
    private static final String CORRECT1_TAGS = "sample tags";

    @Mock JSONObject media;
    @Mock JSONObject correctItem1;
    @Mock JSONObject testItem;

    @Mock JSONObject api_response;
    @Mock JSONObject api_response2;
    @Mock JSONArray array1;
    @Mock JSONArray array2;

    FlickrContent content;

    private String randomized_date() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        final Long max =0L;
        final Long min =100000000000L;
        Random r = new Random();
        Long randomLong=(r.nextLong() % (max - min)) + min;
        Date dt = new Date(randomLong);
        return sdf.format(dt);
    }

    @Before
    public void setup() throws Exception {
        when(media.getString("m")).thenReturn(CORRECT1_MEDIA);

        // Mocking first correct item
        when(correctItem1.getString(TAG_TITLE)).thenReturn(CORRECT1_TITLE);
        when(correctItem1.getString(TAG_LINK)).thenReturn(CORRECT1_LINK);
        when(correctItem1.getJSONObject(TAG_MEDIA)).thenReturn(media);
        when(correctItem1.getString(TAG_DATE_TAKEN)).thenReturn(CORRECT1_DATE);
        when(correctItem1.getString(TAG_DESCRIPTION)).thenReturn(CORRECT1_DESCRIPTION);
        when(correctItem1.getString(TAG_PUBLISHED)).thenReturn(CORRECT1_PUBDATE);
        when(correctItem1.getString(TAG_AUTHOR)).thenReturn(CORRECT1_AUTH_NAME);
        when(correctItem1.getString(TAG_AUTHOR_ID)).thenReturn(CORRECT1_AUTH_ID);
        when(correctItem1.getString(TAG_TAGS)).thenReturn(CORRECT1_TAGS);

        when(testItem.getString(TAG_DATE_TAKEN)).thenReturn(CORRECT1_TAGS);


        // Mocking element with random dates
        when(testItem.getString(TAG_TITLE)).thenReturn(CORRECT1_TITLE);
        when(testItem.getString(TAG_LINK)).thenReturn(CORRECT1_LINK);
        when(testItem.getJSONObject(TAG_MEDIA)).thenReturn(media);
        when(testItem.getString(TAG_DATE_TAKEN)).thenReturn(randomized_date())
                .thenReturn(randomized_date())
                .thenReturn(randomized_date())
                .thenReturn(randomized_date())
                .thenReturn(randomized_date())
                .thenReturn(randomized_date())
                .thenReturn(randomized_date())
                .thenReturn(randomized_date());
        when(testItem.getString(TAG_DESCRIPTION)).thenReturn(CORRECT1_DESCRIPTION);
        when(testItem.getString(TAG_PUBLISHED)).thenReturn(randomized_date());
        when(testItem.getString(TAG_AUTHOR)).thenReturn(CORRECT1_AUTH_NAME);
        when(testItem.getString(TAG_AUTHOR_ID)).thenReturn(CORRECT1_AUTH_ID);
        when(testItem.getString(TAG_TAGS)).thenReturn(CORRECT1_TAGS);

        // Mocking array of 20 elements
        when(api_response.getJSONArray(TAG_ITEMS)).thenReturn(array1);
        when(array1.getJSONObject(anyInt())).thenReturn(testItem);
        when(array1.length()).thenReturn(20);


        // Mocking array of 13 elements
        when(api_response2.getJSONArray(TAG_ITEMS)).thenReturn(array2);
        when(array2.getJSONObject(anyInt())).thenReturn(testItem);
        when(array2.length()).thenReturn(13);

        content = new FlickrContent();
    }

    @Test
    public void Construct_Item_fromJSON_Test() throws Exception {

        FlickrContent.FlickrItem test_item = new FlickrContent.FlickrItem(correctItem1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        assertEquals(test_item.title, CORRECT1_TITLE);
        assertEquals(test_item.link, CORRECT1_LINK);
        assertEquals(test_item.media, CORRECT1_MEDIA);
        assertEquals(test_item.date_taken, sdf.parse(CORRECT1_DATE));
        assertEquals(test_item.description, CORRECT1_DESCRIPTION);
        assertEquals(test_item.published, sdf.parse(CORRECT1_PUBDATE));
        assertEquals(test_item.author, CORRECT1_AUTH_NAME);
        assertEquals(test_item.author_id, CORRECT1_AUTH_ID);
        assertEquals(test_item.tags, CORRECT1_TAGS);
        assertEquals(test_item.toString(), CORRECT1_TITLE);
    }

    /**
     *  Testing initialization with array and sorting by Date Taken (Latest first)
     * @throws Exception
     */
    @Test
    public void Update_Contents_With_Array_Test() throws Exception {
        content.UpdateContent(api_response);
        assertEquals(content.getSize(), 20);
        for (int i = 1; i < content.getSize(); ++i) {
            FlickrContent.FlickrItem item2 = content.getItem(i);
            FlickrContent.FlickrItem item1 = content.getItem(i-1);
            assertTrue(item1.date_taken.compareTo(item2.date_taken) >= 0);
        }
    }

    /**
     *  Testing updating contents with new data
     * @throws Exception
     */
    @Test
    public void Replace_Contents_With_Array_Test() throws Exception {
        content.UpdateContent(api_response2);
        assertEquals(content.getSize(), 13);
        for (int i = 1; i < content.getSize(); ++i) {
            FlickrContent.FlickrItem item2 = content.getItem(i);
            FlickrContent.FlickrItem item1 = content.getItem(i-1);
            assertTrue(item1.date_taken.compareTo(item2.date_taken) >= 0);
        }
    }
}