package com.example.ocr.linkfetcherocr;

import android.graphics.Bitmap;

import com.example.ocr.linkfetcherocr.dbLnkFtch.LnkFtchDbHelper;

import java.net.URL;

/**
 * Created by Joel on 11/3/16.
 */

public class Link {

    //Link
    private String url;

    //A bitmap image of the favicon from the link
    Bitmap favicon;

    // The <title> that is found in the <head> of the html page
    private String tabName;

    //The name of the website/company (not the domain name)
    private String name;

    //The date when the link was fetched
    private String date;

    /**
     * Constructs a new {@link Link} object without a description.
     *
     * @param pUrl is the link
     * @param pFavicon is favicon that the web page has
     * @param pName is the name of the web page
     * @param pTabName is the title the web page has
     * @param pDate is the date when the link was fetched
     */
    public Link(String pUrl, Bitmap pFavicon, String pName, String pTabName, String pDate){
        url = pUrl;
        favicon = pFavicon;
        name = pName;
        tabName = pTabName;
        date = pDate;
    }


    public String getUrl(){return url;}
    public Bitmap getFavicon(){return favicon;}
    public String getName(){return name;}
    public String getTabName(){return tabName;}
    public String getDate(){return date;}
}
