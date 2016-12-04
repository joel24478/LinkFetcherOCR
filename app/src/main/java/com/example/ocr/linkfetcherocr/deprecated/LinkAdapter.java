package com.example.ocr.linkfetcherocr.deprecated;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ocr.linkfetcherocr.Link;
import com.example.ocr.linkfetcherocr.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        //Get the ImageView that holds the image of the link
        ImageView linkImage = (ImageView) listItemView.findViewById(R.id.link_image);
        //Set the image
        //linkImage.setImageBitmap(currentLink.getFavicon());

        //Get the TextView that holds the name of the link
        TextView linkName = (TextView) listItemView.findViewById(R.id.link_name);
        //Set the name
        linkName.setText(currentLink.getName());

        //Get the TextView that holds the tab title of the link
        TextView linkTabName = (TextView) listItemView.findViewById(R.id.link_address);
        //Set the name
        linkName.setText(currentLink.getTabName());

        //Get the TextView that holds the date for the link
        TextView linkDate = (TextView) listItemView.findViewById(R.id.link_date);
        //Set the date
        linkDate.setText(currentLink.getDate());


        return listItemView;
    }

    public String convertStringToDate(String dateString)
    {
        Date date = null;
        String formattedDate = null;
        DateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat newFormat = new SimpleDateFormat("LLL dd, yyyy");
        try{
            date = oldFormat.parse(dateString);
            formattedDate = newFormat.format(date);
        }
        catch ( Exception ex ){
            System.out.println(ex);
        }
        return formattedDate;
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a String.
     */
    public String convertStringToTime(String dateString)
    {
        Date time = null;
        String formattedDate = null;
        DateFormat oldFormat = new SimpleDateFormat("hh:mm:ss");
        DateFormat newFormat = new SimpleDateFormat("h:mm a");
        try{
            time = oldFormat.parse(dateString);
            formattedDate = newFormat.format(time);
        }
        catch ( Exception ex ){
            System.out.println(ex);
        }
        return formattedDate;
    }
}
