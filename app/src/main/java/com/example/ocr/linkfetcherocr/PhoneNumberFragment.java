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

public class PhoneNumberFragment extends Fragment
        implements android.app.LoaderManager.LoaderCallbacks<List<Link>>,
        SharedPreferences.OnSharedPreferenceChangeListener,
        LinkFragmentLifecycle{
    @Override
    public void onPauseFragment(){
        //something
    }
    @Override
    public void onResumeFragment(){
        //something
    }

    @Override
    public void onResume(){
        db.open();
        Cursor newCursor = db.fetchAllPhoneInfo();
        dataAdapter.changeCursor(newCursor);
        super.onResume();
    }
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

    public PhoneNumberFragment(){
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
        /*the Db will load correctly and everything, just need to invoke calls like these*/


        displayListView();

        /*Jwydo * Deprecated to use simplecursor adapter
        *
        // Create a new adapter that takes an empty list of links as input
        //adapter = new LinkAdapter(getActivity(), new ArrayList<Link>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        linksListView.setAdapter(adapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected link.
        linksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current link that was clicked on
                Link currentLink = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri linkUri = Uri.parse(currentLink.getUrl());

                // Create a new intent to view the Link URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, linkUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
        */

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            //LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            //loaderManager.initLoader(LINK_LOADER_ID, null, (android.support.v4.app.LoaderManager.LoaderCallbacks<List<Link>>) activity);
            activity.getLoaderManager().initLoader(LINK_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = rootView.findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
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
        /*
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getContext(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }
    private void displayListView(){
        Cursor cursor = db.fetchAllPhoneInfo();

        View loadingIndicator = rootView.findViewById(R.id.loading_indicator);

        if(cursor.getCount() == 0){
            // Hide loading indicator because the data has been loaded
            loadingIndicator.setVisibility(View.GONE);

            // Set empty state text to display "No links found."
            emptyStateTextView.setText(R.string.no_links);
        }


        // The desired columns to be bound
        String[] columns = new String[] {
                LnkContract.LinkEntry.COLUMN_PHONE_NAME,
                LnkContract.LinkEntry.COLUMN_PHONE_PHONENUM,
                LnkContract.LinkEntry.COLUMN_PHONE_TIME
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.link_pname,
                R.id.link_phone,
                R.id.link_phone_time
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                rootView.getContext(), R.layout.link_list_itemp,
                cursor,
                columns,
                to,
                0);

        linksListView.setAdapter(dataAdapter);

        /*adds the webpage intent*/

        linksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                String phone =
                        cursor.getString(cursor.getColumnIndexOrThrow("phnNum"));
                Intent newI = new Intent(Intent.ACTION_DIAL);
                newI.setData(Uri.parse("tel:" + phone));
                    startActivity(newI);
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return db.fetchPhoneByName(constraint.toString());
            }
        });

        // Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);
    }

}
