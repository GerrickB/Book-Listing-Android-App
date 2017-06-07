package com.example.android.booklistingapp;


import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class BooklistingLoader extends AsyncTaskLoader<List<BookListing>> {

    private static final String LOG_TAG = BooklistingLoader.class.getName();


    private String mUrl;


    public BooklistingLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<BookListing> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<BookListing> booklistings = QueryUtils.fetchBooklistingData(mUrl);
        return booklistings;
    }
}
