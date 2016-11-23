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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ocr.linkfetcherocr.dbLnkFtch.LnkContract.LinkEntry;

    public class LnkFtchDbHelper extends SQLiteOpenHelper {

        public  final String LOG_TAG = LnkFtchDbHelper.class.getSimpleName();

        /**
         * Name of the database file
         */
        private static final String DATABASE_NAME = "lnkFtchr.db";

        /**
         * Database version. If you change the database schema, you must increment the database version.
         */
        private static final int DATABASE_VERSION = 1;

        /**
         * Constructs a new instance of {@link LnkFtchDbHelper}.
         *
         * @param context of the app
         */
        public LnkFtchDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * This is called when the database is created for the first time.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            // Create a String that contains the SQL statement to create the pets table
            String SQL_CREATE_LINKS_TABLE = "CREATE TABLE " + LnkContract.LinkEntry.TABLE_NAME + " ("
                    + LnkContract.LinkEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + LnkContract.LinkEntry.COLUMN_FETCHED_NAME + " TEXT, "
                    + LnkContract.LinkEntry.COLUMN_FETCHED_ADDRESS + " TEXT, "
                    + LnkContract.LinkEntry.COLUMN_FETCHED_URL + " TEXT, "
                    + LnkContract.LinkEntry.COLUMN_SEARCHED_TIME + " TEXT);";

            // Execute the SQL statement
            db.execSQL(SQL_CREATE_LINKS_TABLE);
        }

        /**
         * This is called when the database needs to be upgraded.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // The database is still at version 1, so there's nothing to do be done here.
            db.execSQL("DROP TABLE IF EXISTS " + LinkEntry.TABLE_NAME);
            onCreate(db);
        }
    }