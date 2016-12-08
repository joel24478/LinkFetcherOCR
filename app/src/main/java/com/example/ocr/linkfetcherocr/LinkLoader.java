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

    private LnkFtchDbHelper db;

    /**
     * Constructs a new {@link LinkLoader}.
     *
     * @param context of the activity
     * @param pUrl to load data from
     */
    public LinkLoader(Context context, String pUrl, LnkFtchDbHelper pDb) {
        super(context);
        url = pUrl;
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
            return null;
        }

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

        QueryUtils.createLink(url, db);

        // Perform the network request, parse the response, and extract a list of links.
        List<Link> links = null;

        return links;
    }
}
