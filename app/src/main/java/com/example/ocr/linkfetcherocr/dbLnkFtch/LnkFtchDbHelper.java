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
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.ocr.linkfetcherocr.Link;
import com.example.ocr.linkfetcherocr.R;
import com.example.ocr.linkfetcherocr.dbLnkFtch.LnkContract.LinkEntry;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

public class LnkFtchDbHelper extends SQLiteOpenHelper {

    /*private mdbhelper*/
    private LnkFtchDbHelper mDbHelper;
    private Context mCtx;
    private SQLiteDatabase mDb;

    public  final String LOG_TAG = LnkFtchDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "lnkFtchr.db";
    private static final int DATABASE_VERSION = 9;

    /*SQL to create the tables*/
    /*Link*/
    public static final String SQL_CREATE_LINKS_TABLE = "CREATE TABLE " + LnkContract.LinkEntry.TABLE_NAME_LINKS + " ( "
        + LnkContract.LinkEntry.LINK_ID + " INTEGER PRIMARY KEY autoincrement, "
        + LnkContract.LinkEntry.COLUMN_LINK_NAME + " TEXT, "
        + LnkContract.LinkEntry.COLUMN_LINK_TAB_NAME + " TEXT, "
        + LnkContract.LinkEntry.COLUMN_LINK_FAVICON + " TEXT, "
        + LnkContract.LinkEntry.COLUMN_LINK_URL + " TEXT, "
        + LnkContract.LinkEntry.COLUMN_LINK_TIME + " TEXT); ";

    /*Phone*/
    public static final String SQL_CREATE_PHONE_TABLE ="CREATE TABLE " + LinkEntry.TABLE_NAME_PHONE+ " ( "
        + LnkContract.LinkEntry.PHONE_ID + " INTEGER PRIMARY KEY autoincrement, "
        + LinkEntry.COLUMN_PHONE_NAME + " TEXT, "
        + LinkEntry.COLUMN_PHONE_PHONENUM + " TEXT, "
        + LinkEntry.COLUMN_PHONE_TIME + " TEXT); ";

    /*Email*/
    public static final String SQL_CREATE_EMAIL_TABLE = "CREATE TABLE " + LinkEntry.TABLE_NAME_EMAIL + " ( "
            + LnkContract.LinkEntry.EMAIL_ID + " INTEGER PRIMARY KEY autoincrement, "
            + LinkEntry.COLUMN_EMAIL_NAME + " TEXT, "
            + LinkEntry.COLUMN_EMAIL_EM + " TEXT, "
            + LinkEntry.COLUMN_EMAIL_TIME + " TEXT);";


    public LnkFtchDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mCtx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /* on a new creation create all of our tables*/
        db.execSQL(SQL_CREATE_LINKS_TABLE);
        db.execSQL(SQL_CREATE_PHONE_TABLE);
        db.execSQL(SQL_CREATE_EMAIL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LnkContract.LinkEntry.TABLE_NAME_LINKS);
        db.execSQL("DROP TABLE IF EXISTS " + LnkContract.LinkEntry.TABLE_NAME_PHONE);
        db.execSQL("DROP TABLE IF EXISTS " + LnkContract.LinkEntry.TABLE_NAME_EMAIL);
        onCreate(db);
    }

    public LnkFtchDbHelper open() throws SQLException{
        mDbHelper = new LnkFtchDbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        if(mDbHelper != null){
            mDbHelper.close();
        }
    }

    public long createLinkEntry(String name, String tabName, String url, String photoUrl, String time){
        ContentValues initVals = new ContentValues();
        //Ref: @http://stackoverflow.com/questions/6341776/how-to-save-bitmap-in-database
        /*for a link*/
        initVals.put(LinkEntry.COLUMN_LINK_NAME, name);
        initVals.put(LinkEntry.COLUMN_LINK_TAB_NAME, tabName);
        initVals.put(LinkEntry.COLUMN_LINK_URL, url);
        initVals.put(LinkEntry.COLUMN_LINK_FAVICON, photoUrl);
        initVals.put(LinkEntry.COLUMN_LINK_TIME, time);
        return mDb.insert(LinkEntry.TABLE_NAME_LINKS, null, initVals);
    }
    /*For inserting a entry to the Phone table*/
    public long createPhoneEntry(String name, String phnNmb, String time){
        ContentValues initVals = new ContentValues();
        initVals.put(LinkEntry.COLUMN_PHONE_NAME, name);
        initVals.put(LinkEntry.COLUMN_PHONE_PHONENUM, phnNmb);
        initVals.put(LinkEntry.COLUMN_PHONE_TIME, time);
        return mDb.insert(LinkEntry.TABLE_NAME_PHONE, null, initVals);
    }

    /*For inserting a entry to the Phone Table*/
    public long createEmailEntry(String name, String email, String time){
        ContentValues initVals = new ContentValues();
        initVals.put(LinkEntry.COLUMN_EMAIL_NAME, name);
        initVals.put(LinkEntry.COLUMN_EMAIL_EM, email);
        initVals.put(LinkEntry.COLUMN_EMAIL_TIME, time);
        return mDb.insert(LinkEntry.TABLE_NAME_EMAIL, null, initVals);
    }



    public boolean deleteAllEntries(String tableName){
        int doneDeed = 0;
        doneDeed = mDb.delete(tableName, null, null);
        return doneDeed > 0;
    }
    /*Jwydo
     *Select statements where column is like inpurl, Only works for the links table
     */
    public Cursor fetchLinkByUrl(String inpUrl) throws SQLException{
        Cursor nmCursor = null;
        if (inpUrl == null || inpUrl.length() == 0) {
            nmCursor = mDb.query(LinkEntry.TABLE_NAME_LINKS, new String[]{
                    LinkEntry.LINK_ID, LinkEntry.COLUMN_LINK_NAME, LinkEntry.COLUMN_LINK_TAB_NAME, LinkEntry.COLUMN_LINK_URL, LinkEntry.COLUMN_LINK_FAVICON, LinkEntry.COLUMN_LINK_TIME
            }, null, null, null, null, null);
        }
        else{
            nmCursor = mDb.query(true, LinkEntry.TABLE_NAME_LINKS, new String[] {
                    LinkEntry.LINK_ID, LinkEntry.COLUMN_LINK_NAME, LinkEntry.COLUMN_LINK_TAB_NAME, LinkEntry.COLUMN_LINK_URL, LinkEntry.COLUMN_LINK_FAVICON, LinkEntry.COLUMN_LINK_TIME
            }, LinkEntry.COLUMN_LINK_URL + "like '%'" + inpUrl + "'%'", null, null, null, null, null);
        }
        if (nmCursor != null){
            nmCursor.moveToFirst();
        }
        return nmCursor;
    }

    /*Jwydo
     *set up to retrieve a user by their given name in the LnkFtchr table */
    public Cursor fetchLinkByName(String inpUrl) throws SQLException {
        Cursor nmCursor = null;
        if (inpUrl == null || inpUrl.length() == 0) {
            nmCursor = mDb.query(LinkEntry.TABLE_NAME_LINKS, new String[]{
                    LinkEntry.LINK_ID, LinkEntry.COLUMN_LINK_NAME, LinkEntry.COLUMN_LINK_TAB_NAME, LinkEntry.COLUMN_LINK_URL, LinkEntry.COLUMN_LINK_FAVICON, LinkEntry.COLUMN_LINK_TIME
            }, null, null, null, null, null);
        } else {
            nmCursor = mDb.query(true, LinkEntry.TABLE_NAME_LINKS, new String[] {
                    LinkEntry.LINK_ID, LinkEntry.COLUMN_LINK_NAME, LinkEntry.COLUMN_LINK_TAB_NAME, LinkEntry.COLUMN_LINK_URL, LinkEntry.COLUMN_LINK_FAVICON, LinkEntry.COLUMN_LINK_TIME
            }, LinkEntry.COLUMN_LINK_NAME + "like '%'" + inpUrl + "'%'", null, null, null, null, null);

        }
        return nmCursor;
    }
    /*Fetches all link info*/
    public Cursor fetchAllLinkInfo(){
        Cursor nmCursor = mDb.query(LinkEntry.TABLE_NAME_LINKS, new String[] {
                LinkEntry.LINK_ID, LinkEntry.COLUMN_LINK_NAME, LinkEntry.COLUMN_LINK_TAB_NAME, LinkEntry.COLUMN_LINK_URL, LinkEntry.COLUMN_LINK_FAVICON, LinkEntry.COLUMN_LINK_TIME
        },null, null, null, null, null);
        if(nmCursor != null){
            nmCursor.moveToFirst();
        }
        return nmCursor;
    }

    /*fetches by phone numbers*/
    public Cursor fetchPhoneByNumber(String number){
        Cursor nmCursor;
        if(number == null || number.length() == 0){
            nmCursor= mDb.query(LinkEntry.TABLE_NAME_PHONE, new String[] {
                    LinkEntry.PHONE_ID, LinkEntry.COLUMN_PHONE_NAME, LinkEntry.COLUMN_PHONE_PHONENUM, LinkEntry.COLUMN_PHONE_TIME
            }, null, null, null, null, null);
        } else {
            nmCursor = mDb.query(LinkEntry.TABLE_NAME_PHONE, new String[]{
                    LinkEntry.PHONE_ID, LinkEntry.COLUMN_PHONE_NAME, LinkEntry.COLUMN_PHONE_PHONENUM, LinkEntry.COLUMN_PHONE_TIME
            }, LinkEntry.COLUMN_PHONE_PHONENUM + "like '&'" + number + "'&'", null, null, null, null, null);
        }
        if (nmCursor != null){
            nmCursor.moveToFirst();
        }
        return nmCursor;
    }
    /*fetches by phone name*/
    public Cursor fetchPhoneByName(String name){
        Cursor nmCursor;
        if(name == null || name.length() == 0){
            nmCursor= mDb.query(LinkEntry.TABLE_NAME_PHONE, new String[] {
                    LinkEntry.PHONE_ID, LinkEntry.COLUMN_PHONE_NAME, LinkEntry.COLUMN_PHONE_PHONENUM, LinkEntry.COLUMN_PHONE_TIME
            }, null, null, null, null, null);
        } else{
            nmCursor = mDb.query(LinkEntry.TABLE_NAME_PHONE, new String[]{
                    LinkEntry.PHONE_ID, LinkEntry.COLUMN_PHONE_NAME, LinkEntry.COLUMN_PHONE_PHONENUM, LinkEntry.COLUMN_PHONE_TIME
            }, LinkEntry.COLUMN_PHONE_NAME + "like '&'" + name + "'&'", null, null, null, null, null);
        }
        if(nmCursor != null){
            nmCursor.moveToFirst();
        }
        return nmCursor;
    }
    /*Fetches all phone info*/
    public Cursor fetchAllPhoneInfo(){
        Cursor nmCursor = mDb.query(LinkEntry.TABLE_NAME_PHONE, new String[] {
                LinkEntry.PHONE_ID, LinkEntry.COLUMN_PHONE_NAME, LinkEntry.COLUMN_PHONE_PHONENUM, LinkEntry.COLUMN_PHONE_TIME
        },null, null, null, null, null);
        if(nmCursor != null){
            nmCursor.moveToFirst();
        }
        return nmCursor;
    }

    /*fetches based on email adress*/
    public Cursor fetchEmailByName(String name){
        Cursor nmCursor;
        if(name == null || name.length() == 0 ){
            nmCursor = mDb.query(LinkEntry.TABLE_NAME_EMAIL, new String[]{
                    LinkEntry.EMAIL_ID, LinkEntry.COLUMN_EMAIL_NAME, LinkEntry.COLUMN_EMAIL_EM, LinkEntry.COLUMN_EMAIL_TIME
            },null, null, null, null, null);
        } else {
            nmCursor = mDb.query(LinkEntry.TABLE_NAME_EMAIL, new String[]{
                    LinkEntry.EMAIL_ID, LinkEntry.COLUMN_EMAIL_NAME, LinkEntry.COLUMN_EMAIL_EM, LinkEntry.COLUMN_EMAIL_TIME
            }, LinkEntry.COLUMN_EMAIL_NAME + "like '&'" + name + "'&'", null, null, null, null, null);
            if (nmCursor != null) {
                nmCursor.moveToFirst();
            }
        }
        return nmCursor;
    }

    public Cursor fetchEmailNameByEmail(String email){
        Cursor nmCursor;
        if(email == null || email.length() == 0 ){
            nmCursor = mDb.query(LinkEntry.TABLE_NAME_EMAIL, new String[]{
                    LinkEntry.EMAIL_ID, LinkEntry.COLUMN_EMAIL_NAME, LinkEntry.COLUMN_EMAIL_EM, LinkEntry.COLUMN_EMAIL_TIME
            },null, null, null, null, null);
        } else {
            nmCursor = mDb.query(LinkEntry.TABLE_NAME_EMAIL, new String[]{
                    LinkEntry.EMAIL_ID, LinkEntry.COLUMN_EMAIL_NAME, LinkEntry.COLUMN_EMAIL_EM, LinkEntry.COLUMN_EMAIL_TIME
            }, LinkEntry.COLUMN_EMAIL_EM + "like '&'" + email + "'&'", null, null, null, null, null);
            if (nmCursor != null) {
                nmCursor.moveToFirst();
            }
        }
        return nmCursor;
    }

    /*Fetches all email info*/
    public Cursor fetchAllEmailInfo(){
        Cursor nmCursor = mDb.query(LinkEntry.TABLE_NAME_EMAIL, new String[] {
                LinkEntry.EMAIL_ID, LinkEntry.COLUMN_EMAIL_NAME, LinkEntry.COLUMN_EMAIL_EM, LinkEntry.COLUMN_EMAIL_TIME
        },null, null, null, null, null);
        if(nmCursor != null){
            nmCursor.moveToFirst();
        }
        return nmCursor;
    }

    /*Test function for insertion of links */
    public void insertSomeFakeEntries(){
        createLinkEntry("Jonathan", "JJ", "http://www.facebook.com" ,"something", "11:53");
        createLinkEntry("Katherine", "kObert", "http://www.reddit.com", "something", "12:24");
        createLinkEntry("csDepartment", "csDepot", "http://www.cs.uml.edu", "some", "24:00");
    }

    }