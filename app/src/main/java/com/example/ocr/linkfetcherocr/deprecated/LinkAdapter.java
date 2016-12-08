package com.example.ocr.linkfetcherocr.deprecated;

import android.content.Context;
import android.media.Image;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ocr.linkfetcherocr.Link;
import com.example.ocr.linkfetcherocr.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joel on 11/3/16.
 */

public class LinkAdapter extends ArrayAdapter<Link> {

    private static final String LOG_TAG = LinkAdapter.class.getSimpleName();
    private List<String> list = new ArrayList<String>();
    private int layout;
    public LinkAdapter(Context context, List<Link> articles, int resource) {
        super(context, 0, articles);
        layout = resource;
    }

    @Override
    public int getCount() {
        return list.size();
    }

   // @Override
    //public Object getItem(int pos) {
   //     return list.get(pos);
   // }

    @Override
    public long getItemId(int pos) {
       // return list.get(pos).getBytes()getId();
        //just return 0 if your list items do not have an Id variable.
        return pos;
    }

    /**
     * Returns a list item view that displays information about the earthquake at the given position
     * in the list of earthquakes.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        Log.d("Iam here", "hello3");
        View listItemView = convertView;
        ViewHolder mainViewHolder = null;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.link_list_item, parent, false);
            /*
            ViewHolder viewHolder = new ViewHolder();
            //viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_item_thumbnail);
            viewHolder.link_name = (TextView) convertView.findViewById(R.id.link_name);
            viewHolder.link_field = (TextView) convertView.findViewById(R.id.link_address);
            viewHolder.editEntryButton = (ImageButton) convertView.findViewById(R.id.threeDotSettingsButton);
            convertView.setTag(viewHolder);
        } else {
            mainViewHolder = (ViewHolder) convertView.getTag();
            //mainViewHolder.link_name.setText(getItem(position));
            */
        }

        //Get an link and give it a position
        Link currentLink = getItem(position);

        //Get the ImageView that holds the image of the link
        ImageView linkImage = (ImageView) listItemView.findViewById(R.id.link_image);
        //Set the image
        linkImage.setImageBitmap(currentLink.getFavicon());

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

        //Handle buttons and add onClickListeners

        ImageButton linkImageButton = (ImageButton) listItemView.findViewById(R.id.threeDotSettingsButton);
        Log.d("I am here", "hello2");
        linkImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("I am here", "hello1");
                //Toast.makeText(getContext(), "Button was pressed at" + position, Toast.LENGTH_SHORT).show();
                //do something
            }
        });

        return listItemView;
    }

    public boolean threeDotSettingsButtonClick(View V) {
        Log.d("Iam here", "hello5");
        return true;
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
    public class ViewHolder{
        ImageView thumbnail;
        TextView link_name;
        TextView link_field;
        ImageButton editEntryButton;
    }

}

