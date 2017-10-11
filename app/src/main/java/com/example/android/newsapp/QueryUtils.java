package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noura on 9/15/17.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    private QueryUtils() {

    }


    public static List<News> fetchNewsData(String urlRequest) {

        URL url = createUrl(urlRequest);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<News> newsInfo = extractFeatureFromJson(jsonResponse);

        return newsInfo;

    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("QueryUtils ", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static List<News> extractFeatureFromJson(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> newsList = new ArrayList<>();


        try {

            JSONObject baseJsonResponse = new JSONObject(newsJSON);


            JSONObject responseNewsObject = baseJsonResponse.getJSONObject("response");


            JSONArray newsArray = responseNewsObject.getJSONArray("results");

            for (int i = 0; i < newsArray.length(); i++) {

                JSONObject currentnNews = newsArray.getJSONObject(i);

                String authorName = "Unknown";
                //to check if the News article has an author or not.
                if (currentnNews.has("author")) {
                    authorName = currentnNews.getString("author");
                }


                String newsTitle = currentnNews.getString("webTitle");

                String newsSection = currentnNews.getString("sectionName");


                String newsDate = "Unknown";
                //to check if the News article has date or not.
                if (currentnNews.has("webPublicationDate")) {
                    newsDate = currentnNews.getString("webPublicationDate");
                }


                String newsUrl = currentnNews.getString("webUrl");


                News newsInfo = new News(newsTitle, newsSection, newsUrl, newsDate, authorName);
                newsList.add(newsInfo);

            }

        } catch (JSONException e) {

            Log.e(LOG_TAG, "Problem parsing the Education News JSON results", e);
        }

        return newsList;
    }

}
