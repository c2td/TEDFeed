package com.application.pm.tedfeed;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/* Utility class for network query related operations */
class QueryUtils {

    /** Tag for log messages */
    static final String LOG_TAG = QueryUtils.class.getName();

    // describes all the XML tags that need to be parsed
    static final String ITEM_TAG = "item";
    static final String TITLE_TAG = "title";
    static final String IMAGE_TAG = "media:thumbnail";
    static final String DESCRIPTION_TAG = "description";
    static final String MEDIA_TAG = "media:content";
    static final String URL_ATTR = "url";

    List<CatalogItem> fetchCatalogData(String requestUrl) {

        List<CatalogItem> items = new ArrayList<>();

        // Create URL object
        URL url = createUrl(requestUrl);

        try {
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // If the request was successful (response code 200),
                // then read the input stream and parse the response.
                if (urlConnection.getResponseCode() == 200) {

                    inputStream = urlConnection.getInputStream();
                    items = parseXmlData(inputStream);

                } else {
                    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the xml results.", e);
            } catch (XmlPullParserException e) {
                Log.e(LOG_TAG, "Problem with parsing the xml.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return items;

    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /* Parses the XML data tag by tag */
    private List<CatalogItem> parseXmlData(InputStream stream) throws XmlPullParserException, IOException {

        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(stream, null);

        CatalogItem item = null;
        String text = "";
        List<CatalogItem> itemObjects = new ArrayList<>();
        int eventType = parser.getEventType();

        boolean inItemTag = false;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagname = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagname.equalsIgnoreCase(ITEM_TAG)) {
                        item = new CatalogItem();
                        inItemTag = true;
                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase(ITEM_TAG)) {
                        itemObjects.add(item);
                        inItemTag = false;
                    } else if (tagname.equalsIgnoreCase(TITLE_TAG) && inItemTag) {
                        item.setTitle(text);
                    } else if (tagname.equalsIgnoreCase(IMAGE_TAG) &&  inItemTag) {
                        item.setImageUrl(parser.getAttributeValue(null, URL_ATTR));
                    } else if (tagname.equalsIgnoreCase(DESCRIPTION_TAG) && inItemTag) {
                        item.setDescription(text);
                    } else if (tagname.equalsIgnoreCase(MEDIA_TAG) && inItemTag) {
                        item.setVideoUrl(parser.getAttributeValue(null, URL_ATTR));
                    }
                    break;

                default:
                    break;
            }
            eventType = parser.next();
        }

        stream.close();

        return itemObjects;

    }

}
