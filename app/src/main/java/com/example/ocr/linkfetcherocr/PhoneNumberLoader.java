package com.example.ocr.linkfetcherocr;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.ocr.linkfetcherocr.dbLnkFtch.LnkFtchDbHelper;

import java.util.List;

/**
 * Created by Joel on 12/8/16.
 */

public class PhoneNumberLoader extends AsyncTaskLoader<List<Link>> {

    private static final String LOG_TAG = LinkLoader.class.getSimpleName();

    private String phoneNumber;

    private LnkFtchDbHelper db;

    /**
     * Constructs a new {@link LinkLoader}.
     *
     * @param context of the activity
     * @param pPhoneNumber to load data from
     */
    public PhoneNumberLoader(Context context, String pPhoneNumber, LnkFtchDbHelper pDb) {
        super(context);
        phoneNumber = pPhoneNumber;
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
        if (phoneNumber == null) {
            Log.e(LOG_TAG, "phoneNumber is null");
            return null;
        }

        //Log.v(LOG_TAG, "starting parser");

        if(!LinkParser.getPhoneNumber(phoneNumber).equals("N/A")){
            Log.v(LOG_TAG, phoneNumber + " is a phoneNumber ");
            QueryUtils.createPhoneNumber(phoneNumber ,LinkFragment.db);
        }else{
            Log.e(LOG_TAG, "This is not a phoneNumber => " + phoneNumber);

        }

        // Perform the network request, parse the response, and extract a list of links.
        List<Link> links = null;

        return links;
    }
}
