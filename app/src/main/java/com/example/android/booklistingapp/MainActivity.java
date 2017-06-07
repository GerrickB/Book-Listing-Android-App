package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<BookListing>> {

    private static final String LOG_TAG = MainActivity.class.getName();


    private String GoogleBooksRequestURL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    String completeURL;

    private static final int BOOK_LOADER_ID = 1;

    private BooklistingAdapter mAdapter;

    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView BooklistingListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        BooklistingListView.setEmptyView(mEmptyStateTextView);
        mEmptyStateTextView.setText(R.string.advice);

        mAdapter = new BooklistingAdapter(this, new ArrayList<BookListing>());
        BooklistingListView.setAdapter(mAdapter);

        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText text = (EditText) findViewById(R.id.edit_text);
                String searchKeyword = text.getText().toString();
                completeURL = GoogleBooksRequestURL + searchKeyword + "&maxResults=20";

                ConnectivityManager connectivityManager = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
                } else {
                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }

                getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);

                mAdapter = new BooklistingAdapter(MainActivity.this, new ArrayList<BookListing>());
                BooklistingListView.setAdapter(mAdapter);
            }
        });
    }

    @Override
    public Loader<List<BookListing>> onCreateLoader(int i, Bundle bundle) {

        return new BooklistingLoader(this, completeURL);
    }

    @Override
    public void onLoadFinished(Loader<List<BookListing>> loader, List<BookListing> booklistings) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);


        mEmptyStateTextView.setText(R.string.no_books);


        mAdapter.clear();


        if (booklistings != null && !booklistings.isEmpty()) {
            mAdapter.addAll(booklistings);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BookListing>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}
