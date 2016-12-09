package com.example.ocr.linkfetcherocr;


import android.content.Context;
import android.util.Log;

import com.example.ocr.linkfetcherocr.dbLnkFtch.LnkFtchDbHelper;
import com.squareup.picasso.Picasso;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Joel on 12/2/16.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /* the CASE_INSENSITIVE flag accounts for sites that use uppercase title tags.
    *The DOTALL flag accounts for sites that have line feeds in the title text
    */
    private static final Pattern FAVICON_TAG =
            Pattern.compile("<link.*?href=\"(.*?\\.ico)\".*?\\/>", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
    private static final Pattern FAVICON_TAG2 =
            Pattern.compile("<link.*?rel=\"(.*?icon)\".*?\\/>", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Returns a list of {@link Link} objects from the database.
     */
//    public static List<Link> fetchLinks() {
//
//
//
//        //Get the data from the database and create a list of {@link Link}s
//        List<Link> links = createLink();
//
//        // Return the list of {@link Link}s
//        return links;
//    }

    /**
     * Create a URL entry in the database.
     * @param pUrl the url for the site
     * @param pDb the database to place entry in
     *
     */
    public static void createLink(String pUrl, LnkFtchDbHelper pDb){

        if(!checkUrl(pUrl)){
            Log.e(LOG_TAG, "Link your trying to create is invalid/nInvalid URL: " + pUrl);
        }

        String formattedUrl = formatUrl(pUrl);

        String url = formattedUrl;

        try{
            String favIcon = getPageFavIcon(url);
            String name = null;
            String title = getPageTitle(url);
            String date = getTimeStamp();

            try{
                name = getPageName(url);
            }catch (URISyntaxException e){
                Log.e(LOG_TAG, e.getMessage());
            }

            Log.v(LOG_TAG, "favIcon: " + favIcon + "\nname: " + name + "\ntitle: " + title + "\ndate: " + date);

            //Create an entry in the database
            pDb.createLinkEntry(name, title, url, favIcon, date);
        }catch (IOException e){
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    /**
     * Create a Email entry in the database
     * @param pEmail the email to be add to the database
     * @param pDb the database to place the entry in
     * @return void.
     */
    public static void createEmail(String pEmail, LnkFtchDbHelper pDb) {

        String date = getTimeStamp();
        pDb.createEmailEntry("N/A", pEmail, date);
    }

    /**
     * Create a Phone Number entry in the database
     * @param pNumber the Phone number to be add to the database
     * @param pDb the database to place the entry in
     * @return void.
     */
    public static void createPhoneNumber(String pNumber, LnkFtchDbHelper pDb) {

        String date = getTimeStamp();
        pDb.createPhoneEntry("N/A", pNumber, date);
    }

    /**
     * Format a url so that it has http:// or else you'll get errors on trying to connect
     * @param url to a site
     * @return formatted url.
     */
    private static String formatUrl(String url){

        String formattedURL = "";

        Pattern pattern = Pattern.compile("http:\\/\\/|https:\\/\\/");
        Matcher matcher = pattern.matcher(url);

        if(matcher.find()){
            formattedURL = url;
        }else{
            formattedURL = "https://" + url;
        }

        return formattedURL;
    }

    /**
     * Check to see if its a valid url
     * @param url to a site
     * @return true if its a base url, and false if its not.
     */
    private static boolean checkUrl(String url){

        String formattedURL = "";

        Pattern pattern = Pattern.compile(
                "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                        + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                        + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(url);

        if(matcher.find()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Get the current date
     * @return current date.
     */
    private static String getTimeStamp(){

        Calendar c = Calendar.getInstance();
        //System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MMM dd/yyyy");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    /**
     * Get the HTML's name
     * @param url to a site
     * @return Drawable object (favicon) from a url.
     */
    public static String getPageName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        domain = domain.replace("www\\.", "");
        return domain;
    }


    /**
     * Get a HTML's title tag off a url
     * @param url of HTML page
     * @return title of HTML page
     * @throws IOException
     */
    public static String getPageTitle(String url) throws IOException {

        String formattedURL = formatUrl(url);

        Document doc = Jsoup.connect(url).get();

        return doc.title();
    }

    /**
     * Get a HTML's favicon off a url
     * @param url the HTML page
     * @return favIcon url (N/A if document isn't HTML or lacks a favicon)
     * @throws IOException
     */
    public static String getPageFavIcon(String url) throws IOException {

        Log.v(LOG_TAG, "Connecting to " + url);

        String formattedUrl = formatUrl(url);
        boolean failedConnection = false;

        Log.v(LOG_TAG, "Formatted Url: " + formattedUrl);

        String favicon = "N/A";

        //Connected to the url and get the html file so we can travers the file
        Document doc = Jsoup.connect("https://www.google.com").get();
        //Travers the file to find the first occurrence of a .ico file
        Element element = doc.head().select("link[href~=.*\\.ico]").first();

        //Log.v(LOG_TAG, "element: " + element);

        //Check to see if it was found
        if(element == null){
            Log.e(LOG_TAG, "element not found on page, attempting another method");

            formattedUrl = formattedUrl + "/favicon.ico";

            Log.v(LOG_TAG, "Connecting to " + formattedUrl);

            try{
                Document doc2 = Jsoup.connect(formattedUrl).get();
                Log.v(LOG_TAG, "Connected");
            }catch (IOException e){
                Log.e(LOG_TAG, "Couldn't connect to " + formattedUrl);
            }

            return formattedUrl;
        }
        //Else the element was found on the page
        else {
            //store that in favicon
            favicon = element.attr("href");
        }

        //Check to see if favicon url is complete
        if(!checkUrl(favicon)){
            //Was most likely missing its base url;
            favicon = url + favicon;

            //Check again to see if its a correct url
            if(!checkUrl(favicon)){
                //If its not then most sites have it on the root of the site
                //www.example.com/favicon.ico
                favicon = url + "/favicon.ico";

                //Check it one more time to make sure
                if(!checkUrl(favicon)) {
                    //The favicon doesn't exist on the site
                    favicon = "N/A";
                }
            }
        }

        return favicon;
    }
}



