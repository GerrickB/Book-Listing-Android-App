package com.example.android.booklistingapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BooklistingAdapter extends ArrayAdapter<BookListing> {

    public BooklistingAdapter(Context context, List<BookListing> booklistings) {
        super(context, 0, booklistings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        BookListing currentBooklisting = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        String title = currentBooklisting.getTitle();
        titleView.setText(title);

        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        String author = currentBooklisting.getAuthor();
        authorView.setText(author);



        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

}
