package com.example.android.booklistingapp;



public class BookListing {

    private String mTitle;

    private String mAuthor;

    public BookListing(String title, String author) {
        mTitle = title;
        mAuthor = author;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
