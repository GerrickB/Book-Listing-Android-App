package com.example.android.booklistingapp;

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



public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<BookListing> fetchBooklistingData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }


        List<BookListing> booklistings = extractFeatureFromJson(jsonResponse);

        return booklistings;
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

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the booklisting JSON results.", e);
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

    private static List<BookListing> extractFeatureFromJson(String booklistingJSON) {
        if (TextUtils.isEmpty(booklistingJSON)) {
            return null;
        }

        List<BookListing> booklistings = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(booklistingJSON);

            JSONArray booklistingArray = baseJsonResponse.getJSONArray("items");


            for (int i = 0; i < booklistingArray.length(); i++) {


                JSONObject currentBooklisting = booklistingArray.getJSONObject(i);

                JSONObject properties = currentBooklisting.getJSONObject("volumeInfo");

                String title = properties.getString("title");

                String author = "";

                if (properties.has("authors")) {
                    author = properties.getString("authors");
                }

                // Create a new {@link Earthquake} object with the magnitude, location, time,
                // and url from the JSON response.
                BookListing booklisting = new BookListing(title, author);

                // Add the new {@link Earthquake} to the list of earthquakes.
                booklistings.add(booklisting);
            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the booklisting JSON results", e);
        }

        // Return the list of earthquakes
        return booklistings;
    }
}
