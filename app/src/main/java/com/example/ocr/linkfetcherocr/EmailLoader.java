package com.example.ocr.linkfetcherocr;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.ocr.linkfetcherocr.dbLnkFtch.LnkFtchDbHelper;

import java.util.List;

/**
 * Created by Joel on 12/8/16.
 */

public class EmailLoader extends AsyncTaskLoader<List<Link>> {

    private static final String LOG_TAG = LinkLoader.class.getSimpleName();

    private String email;

    private LnkFtchDbHelper db;

    /**
     * Constructs a new {@link LinkLoader}.
     *
     * @param context of the activity
     * @param pEmail to load data from
     */
    public EmailLoader(Context context, String pEmail, LnkFtchDbHelper pDb) {
        super(context);
        email = pEmail;
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
        if (email == null) {
            Log.e(LOG_TAG, "email is null");
            return null;
        }

        Log.v(LOG_TAG, "starting parser");

        if(!LinkParser.getEmail(email).equals("N/A")){
            Log.v(LOG_TAG, email + " is a email ");
            QueryUtils.createEmail(email ,LinkFragment.db);
        }else{
            Log.e(LOG_TAG, "This is not a email => " + email);

        }

        QueryUtils.createEmail(email, db);

        // Perform the network request, parse the response, and extract a list of links.
        List<Link> links = null;

        return links;
    }
}
