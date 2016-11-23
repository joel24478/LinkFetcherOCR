package com.example.ocr.linkfetcherocr;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.ocr.linkfetcherocr.dbLnkFtch.LnkContract;

/**
 * Created by jwydo on 11/22/2016.
 */

public class LinkCursorAdapter extends CursorAdapter {

    public LinkCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.link_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.link_name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.link_tab_title);

        // Find the columns of link attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(LnkContract.LinkEntry.COLUMN_FETCHED_NAME);
        int urlColumnIndex = cursor.getColumnIndex(LnkContract.LinkEntry.COLUMN_FETCHED_URL);

        // Read the pet attributes from the Cursor for the current pet
        String linkName = cursor.getString(nameColumnIndex);
        String linkUrl = cursor.getString(urlColumnIndex);

        // If the pet breed is empty string or null, then use some default text
        // that says "Unknown breed", so the TextView isn't blank.
        if (TextUtils.isEmpty(linkUrl)) {
          linkUrl = "nothing";
        }

        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(linkName);
        summaryTextView.setText(linkUrl);
    }
}
