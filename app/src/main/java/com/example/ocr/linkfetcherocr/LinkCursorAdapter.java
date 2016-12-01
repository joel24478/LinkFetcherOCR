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
    LayoutInflater inflater;

    public LinkCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.link_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.link_name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.link_address);

        int nameColumnIndex = cursor.getColumnIndex(LnkContract.LinkEntry.COLUMN_FETCHED_NAME);
        int urlColumnIndex = cursor.getColumnIndex(LnkContract.LinkEntry.COLUMN_FETCHED_URL);
        String linkName = cursor.getString(nameColumnIndex);
        String linkUrl = cursor.getString(urlColumnIndex);

        if (TextUtils.isEmpty(linkUrl)) {
          linkUrl = "nothing";
        }
        nameTextView.setText(linkName);
        summaryTextView.setText(linkUrl);
    }
}
