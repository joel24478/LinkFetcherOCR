package com.example.ocr.linkfetcherocr;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.ocr.linkfetcherocr.dbLnkFtch.LnkFtchDbHelper;

import java.io.IOException;
import java.util.List;

/**
 * Created by Joel on 11/3/16.
 */

public class LinkLoader extends AsyncTaskLoader<List<Link>> {

    private static final String LOG_TAG = LinkLoader.class.getSimpleName();

    private String url;
    private Context context;
    LnkFtchDbHelper db;

    private LnkFtchDbHelper db;

    /**
     * Constructs a new {@link LinkLoader}.
     *
     * @param pContext of the activity
     * @param pUrl to load data from
     */
<<<<<<< HEAD
    public LinkLoader(Context pContext, String pUrl, LnkFtchDbHelper pDb) {
        super(pContext);
        url = pUrl;
        //context = pContext;
=======
    public LinkLoader(Context context, String pUrl, LnkFtchDbHelper pDb) {
        super(context);
        url = pUrl;
>>>>>>> 8ed03a4763473510e23485196273b37d19dcb821
        db = pDb;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Link> loadInBackground() {
        if (url == null) {
            Log.e(LOG_TAG, "url is null");
            return null;
        }

<<<<<<< HEAD
//        String test = "Failed";
//
//        try {
//
//            test = QueryUtils.getPageFavIcon(url);
//
//        }catch(IOException e){
//            Log.e(LOG_TAG, e.getMessage());
//        }
//
//        Log.v(LOG_TAG, "FavIcon: " + test);
=======
        Log.v(LOG_TAG, "starting parser");
         if(!LinkParser.getLink(url).equals("N/A")){
            Log.v(LOG_TAG, url + " is a link ");
             QueryUtils.createLink(url ,LinkFragment.db);
        }else if(!LinkParser.getEmail(url).equals("N/A")){
            Log.v(LOG_TAG, url + " is a email ");
            QueryUtils.createEmail(url ,LinkFragment.db);

        }else if(!LinkParser.getPhoneNumber(url).equals("N/A")){
            Log.v(LOG_TAG, url + " is a phone number ");
            QueryUtils.createPhoneNumber(url, LinkFragment.db);

        }else{
            Log.e(LOG_TAG, "This is neither a link, email nor phone number: " + url);

        }
>>>>>>> 8ed03a4763473510e23485196273b37d19dcb821

        QueryUtils.createLink(url, db);

        // Perform the network request, parse the response, and extract a list of links.
        List<Link> links = null;

        return links;
    }
}
