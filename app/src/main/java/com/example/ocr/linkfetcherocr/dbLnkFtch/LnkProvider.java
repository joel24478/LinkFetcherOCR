/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.ocr.linkfetcherocr.dbLnkFtch;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.ocr.linkfetcherocr.dbLnkFtch.LnkContract.LinkEntry;

public class LnkProvider extends ContentProvider {


    public static final String LOG_TAG = LnkProvider.class.getSimpleName();
    private static final int LINK = 100;
    private static final int LINK_ID = 101;

    private static final int PHONE = 200;
    private static final int PHONE_ID = 201;

    private static  final int EMAIL = 300;
    private static final int EMAIL_ID = 301;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        sUriMatcher.addURI(LnkContract.CONTENT_AUTHORITY, LnkContract.PATH_LINKS, LINK);
        sUriMatcher.addURI(LnkContract.CONTENT_AUTHORITY, LnkContract.PATH_LINKS + "/#", LINK_ID);
        sUriMatcher.addURI(LnkContract.CONTENT_AUTHORITY, LnkContract.PATH_PHONE, PHONE);
        sUriMatcher.addURI(LnkContract.CONTENT_AUTHORITY, LnkContract.PATH_PHONE + "/#", PHONE_ID);
        sUriMatcher.addURI(LnkContract.CONTENT_AUTHORITY, LnkContract.PATH_EMAIL, EMAIL_ID);
        sUriMatcher.addURI(LnkContract.CONTENT_AUTHORITY, LnkContract.PATH_EMAIL + "/#", EMAIL_ID);

    }

    /** Database helper object */
    private LnkFtchDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new LnkFtchDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case LINK:

                cursor = database.query(LinkEntry.TABLE_NAME_LINKS, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case LINK_ID:
                selection = LinkEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };


                cursor = database.query(LinkEntry.TABLE_NAME_LINKS, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            /*
            case PHONE:
                break;
            case PHONE_ID:
                break;
            case EMAIL:
                break;
            case EMAIL_ID:
                break;
                */
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case LINK:
                return insertLink(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertLink(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(LinkEntry.COLUMN_LINK_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Link requires a name");
        }

        // Check that the email Address
        String ftchTabName = values.getAsString(LinkEntry.COLUMN_LINK_TAB_NAME);
        if (ftchTabName == null) {
            throw new IllegalArgumentException("Link requires valid tab name");
        }

        // check bse url
        String ftchUrl = values.getAsString(LinkEntry.COLUMN_LINK_URL);
        if (ftchUrl == null || !LinkEntry.isValidBaseUrl(ftchUrl)) {
            throw new IllegalArgumentException("Link requires valid Url");
        }


        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(LinkEntry.TABLE_NAME_LINKS, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case LINK:
                return updateLink(uri, contentValues, selection, selectionArgs);
            case LINK_ID:
                selection = LinkEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateLink(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateLink(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(LinkEntry.COLUMN_LINK_NAME)) {
            String name = values.getAsString(LinkEntry.COLUMN_LINK_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Link requires a name");
            }
        }

        if (values.containsKey(LinkEntry.COLUMN_LINK_FAVICON)) {
            String favIcon = values.getAsString(LinkEntry.COLUMN_LINK_FAVICON);
            if (favIcon == null) {
                throw new IllegalArgumentException("Link requires valid favIcon");
            }
        }

        if (values.containsKey(LinkEntry.COLUMN_LINK_URL)) {
            //check valid url
            String fURL = values.getAsString(LinkEntry.COLUMN_LINK_URL);
            if (fURL != null && LinkEntry.isValidBaseUrl(fURL)) {
                throw new IllegalArgumentException("Link requires valid URL");
            }
        }
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(LinkEntry.TABLE_NAME_LINKS, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case LINK:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(LinkEntry.TABLE_NAME_LINKS, selection, selectionArgs);
                break;
            case LINK_ID:
                // Delete a single row given by the ID in the URI
                selection = LinkEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(LinkEntry.TABLE_NAME_LINKS, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case LINK:
                return LinkEntry.CONTENT_LIST_TYPE;
            case LINK_ID:
                return LinkEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
