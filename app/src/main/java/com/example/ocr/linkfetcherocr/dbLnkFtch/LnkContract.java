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

    public static final class LinkEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_LINKS);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LINKS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LINKS;
        public final static String TABLE_NAME = "lnkFtchr";
        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_FETCHED_NAME ="name";

        public final static String COLUMN_FETCHED_FAVICON = "favicon";

        public final static String COLUMN_FETCHED_URL = "url";
        public final static String COLUMN_FETCHED_TIME = "time";
        public final static String COLUMN_FETCHED_TAB_NAME = "tabname";
        public static boolean isValidEmail(String email) {
            return true;
        }

        public static boolean isValidBaseUrl(String url){
            return true;
        }
    }

}

