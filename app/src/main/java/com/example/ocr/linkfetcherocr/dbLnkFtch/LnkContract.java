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
    public static final String PATH_PHONE = "phonefetcher";
    public static final String PATH_EMAIL = "emailfetcher";

    public static final class LinkEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_LINKS);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LINKS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LINKS;

        /*For link Table*/
        public final static String TABLE_NAME_LINKS = "lnkFtchr";
        public final static String LINK_ID = BaseColumns._ID;
        public final static String COLUMN_LINK_NAME ="name";
        public final static String COLUMN_LINK_FAVICON = "favicon";
        public final static String COLUMN_LINK_URL = "url";
        public final static String COLUMN_LINK_TIME = "time";
        public final static String COLUMN_LINK_TAB_NAME = "tabname";

        /*for phone table*/
        public final static String TABLE_NAME_PHONE = "phones";
        public final static String PHONE_ID = BaseColumns._ID;
        public final static String COLUMN_PHONE_NAME ="name";
        public final static String COLUMN_PHONE_PHONENUM = "phnNum";
        public final static String COLUMN_PHONE_TIME = "time";

        /*for the email table*/
        public final static String TABLE_NAME_EMAIL = "emails";
        public final static String EMAIL_ID = BaseColumns._ID;
        public final static String COLUMN_EMAIL_NAME = "name";
        public final static String COLUMN_EMAIL_EM = "email";
        public final static String COLUMN_EMAIL_TIME = "time";



        public static boolean isValidBaseUrl(String url){
            return true;
        }
    }


}

