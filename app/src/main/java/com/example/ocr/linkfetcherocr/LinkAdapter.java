package com.example.ocr.linkfetcherocr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Joel on 11/3/16.
 */

public class LinkAdapter extends ArrayAdapter<Link> {

    private static final String LOG_TAG = LinkAdapter.class.getSimpleName();

    public LinkAdapter(Context context, List<Link> articles) {
        super(context, 0, articles);

    }

    /**
     * Returns a list item view that displays information about the earthquake at the given position
     * in the list of earthquakes.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.link_list_item, parent, false);
        }

        //Get an link and give it a position
        Link currentLink = getItem(position);



        return listItemView;
    }
}
