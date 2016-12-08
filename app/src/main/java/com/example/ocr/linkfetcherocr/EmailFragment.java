package com.example.ocr.linkfetcherocr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ocr.linkfetcherocr.dbLnkFtch.LnkContract;
import com.example.ocr.linkfetcherocr.dbLnkFtch.LnkFtchDbHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Martin Rudzki on 11/3/16.
 */

public class EmailFragment extends Fragment
        implements android.app.LoaderManager.LoaderCallbacks<List<Link>>,
        SharedPreferences.OnSharedPreferenceChangeListener {


    private static final String LOG_TAG = EmailFragment.class.getSimpleName();
    private static final String REQUEST_URL = "www.yahoo.com";

    /**
     * Constant value for the link loader ID. We can choose any integer.
     */
    private static final int LINK_LOADER_ID = 1;

    /** Adapter for the list of link */
    //not used
    //private LinkAdapter adapter2;

    /** TextView that is displayed when the list is empty */
    private TextView emptyStateTextView;

    private View rootView;

    private Activity activity;

    /*Jwydo*/
    private LnkFtchDbHelper db;

    private SimpleCursorAdapter dataAdapter;

    private ListView linksListView;
    /*Jwydo*/

    public EmailFragment(){
        //Empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Have to set it to true so you can your the OnCreateOptionsMenu() function
        setHasOptionsMenu(true);

        rootView = inflater.inflate(R.layout.link_list, container, false);
        activity = getActivity();

        // Find a reference to the {@link ListView} in the layout
        linksListView = (ListView) rootView.findViewById(R.id.list);

        emptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        linksListView.setEmptyView(emptyStateTextView);

        /*Jwydo*/
        db = new LnkFtchDbHelper(getActivity());
        db.open();
        /*the Db will load correctly and everything, just need to invoke calls like these
        db.deleteAllEntries(LnkContract.LinkEntry.TABLE_NAME_EMAIL);
        db.createEmailEntry("SomeName", "SomeEmail", "SomeTime");
        db.createEmailEntry("NewEmail", "newEmail@student.uml.edu", "SomeTime");
        */

        //Testing whether QueryUtils works
        QueryUtils.createEmail("joel24478@gmail.com", db);

        displayListView();


        return rootView;
    }


    @Override
    public Loader<List<Link>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

//        String language = sharedPrefs.getString(
//                getString(R.string.settings_language_key),
//                getString(R.string.settings_language_default));
//
        return new LinkLoader(getContext(), REQUEST_URL, db);
    }

    @Override
    public void onLoadFinished(Loader<List<Link>> loader, List<Link> links) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = rootView.findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No links found."
        emptyStateTextView.setText(R.string.no_links);

        /* Jwydo depercated
        // Clear the adapter of previous link data
        //adapter.clear();

        // If there is a valid list of {@link Links}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (links != null && !links.isEmpty()) {
            adapter.addAll(links);
        }
        */
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
//        if (key.equals(getString(R.string.settings_world_key)) ||
//                key.equals(getString(R.string.settings_language_key))) {
//
//            // Clear the ListView as a new query will be kicked off
//            adapter.clear();
//
//            // Hide the empty state text view as the loading indicator will be displayed
//            emptyStateTextView.setVisibility(View.GONE);
//
//            // Show the loading indicator while new data is being fetched
//            View loadingIndicator = rootView.findViewById(R.id.loading_indicator);
//            loadingIndicator.setVisibility(View.VISIBLE);
//
//            // Restart the loader to requery the Bing News as the query settings have been updated
//            activity.getLoaderManager().restartLoader(LINK_LOADER_ID, null, this);
//        }
    }
    @Override
    public void onLoaderReset(Loader<List<Link>> loader) {
        // Loader reset, so we can clear out our existing data.
        //adapter2.clear();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getContext(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void displayListView(){
        Cursor cursor = db.fetchAllEmailInfo();

        View loadingIndicator = rootView.findViewById(R.id.loading_indicator);

        if(cursor.getCount() == 0){
            // Hide loading indicator because the data has been loaded
            loadingIndicator.setVisibility(View.GONE);

            // Set empty state text to display "No links found."
            emptyStateTextView.setText(R.string.no_links);
        }

        // The desired columns to be bound
        String[] columns = new String[] {
                LnkContract.LinkEntry.COLUMN_EMAIL_NAME,
                LnkContract.LinkEntry.COLUMN_EMAIL_EM,
                LnkContract.LinkEntry.COLUMN_EMAIL_TIME
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.link_ename,
                R.id.link_email,
                R.id.link_email_time
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                rootView.getContext(), R.layout.link_list_iteme,
                cursor,
                columns,
                to,
                0);
        /*
        dataAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder(){
            public boolean setViewValue(View view, Cursor cursor, int i){
                //hardcode favicon
                if (i == 3) {
                    ImageView favIconView = (ImageView) view;
                    Picasso.with(rootView.getContext()).load("http://i.imgur.com/DvpvklR.png").into(favIconView);



                }
                return true;
            }

        });
        */

        linksListView.setAdapter(dataAdapter);

        /*adds the webpage intent*/

        linksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);


                String email =
                        cursor.getString(cursor.getColumnIndexOrThrow("email"));
                Intent newI = new Intent(Intent.ACTION_SENDTO);
                newI.setData(Uri.parse("mailto:" + email));
                startActivity(newI);

            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return db.fetchEmailByName(constraint.toString());
            }
        });

        // Hide loading indicator because the data has been loaded

        loadingIndicator.setVisibility(View.GONE);

    }
}
