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

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Pets app.
 */
public final class LnkContract {

    private LnkContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.ocr.linkfetcherocr";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_LINKS = "linkfetcherocr";

//    /*
//     * Inner class that defines constant values for the User database table.
//     * each entry in the table represents a single User
//     * Later User
//     *
//     */
//    public static final class UserEntry implenets BaseColumns {
//    /** The content URI to access the searched data in the provider */
//    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_LINKS);
//
//    /**
//     * The MIME type of the {@link #CONTENT_URI} for a list of saved searched.
//     */
//    public static final String CONTENT_LIST_TYPE =
//            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LINKS;
//
//    /**
//     * The MIME type of the {@link #CONTENT_URI} for a single saved search.
//     */
//    public static final String CONTENT_ITEM_TYPE =
//            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LINKS;
//
//    /** Name of database table for pets */
//    public final static String TABLE_NAME = "links";
//
//    /**
//     * Unique ID number for entry in table
//     * Type: INTEGER
//     */
//    public final static String _ID = BaseColumns._ID;
//     * }
//

    public static final class LinkEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_LINKS);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LINKS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LINKS;
        public final static String TABLE_NAME = "lnkFtchr";
        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_FETCHED_NAME ="name";

        public final static String COLUMN_FETCHED_ADDRESS = "eaddress";

        public final static String COLUMN_FETCHED_URL = "url";
        public final static String COLUMN_SEARCHED_TIME = "time";
        public final static String COLUMN_IMAGE = "image";
        public static boolean isValidEmail(String email) {
            return true;
        }

        public static boolean isValidBaseUrl(String url){
            return true;
        }
    }

}

