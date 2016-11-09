package com.example.ocr.linkfetcherocr;

import android.graphics.Bitmap;

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
    private String name;


    /**
     * Constructs a new {@link Link} object without a description.
     *
     * @param pUrl is the link
     * @param pFavicon is favicon that the web page has
     * @param pName is the title the web page has
     */
    public Link(String pUrl, Bitmap pFavicon, String pName){
        url = pUrl;
        favicon = pFavicon;
        name = pName;
    }


    public String getUrl(){return url;}
    public Bitmap getFavicon(){return favicon;}
    public String getName(){return name;}
}
