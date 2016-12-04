package com.example.ocr.linkfetcherocr;


import android.util.Log;
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
//        // Perform HTTP request to the URL and receive a JSON response back
//        String jsonResponse = null;
//        try {
//            jsonResponse = makeHttpRequest(url);
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
//        }
//
//        // Extract relevant fields from the JSON response and create a list of {@link Link}s
//        List<Link> links = extractFeatureFromJson(jsonResponse);
//
//        // Return the list of {@link Article}s
//        return links;
//    }

    /**
     * Returns a list of {@link Link} objects from the database.
     */
    public static Link createLink(String pUrl){
        Link link;

        if(!checkUrl(pUrl)){
            Log.e(LOG_TAG, "Link your trying to create is invalid/nInvalid URL: " + pUrl);
            return null;
        }

        String formattedUrl = formatUrl(pUrl);

        String url = formattedUrl;

        try{
            String favIcon = getPageFavIcon(url);
            String name = null;
            String title = getPageTitle(url);
            String date = getCurrentDate();

            try{
                name = getPageName(url);
            }catch (URISyntaxException e){
                Log.e(LOG_TAG, e.getMessage());
                return null;
            }

            Log.v(LOG_TAG, "favIcon: " + favIcon + "\nname: " + name + "\ntitle: " + title + "\ndate: " + date);

            link = new Link(url, favIcon, name, title, date);

            return link;
        }catch (IOException e){
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param stringUrl link to site
     * @return URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
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
            formattedURL = "http://" + url;
        }

        return formattedURL;
    }

    /**
     * @param url to a site
     * @return formatted url.
     */
    private static String baseUrl(String url){

        String formattedURL = "";

        Pattern pattern = Pattern.compile("http:\\/\\/|https:\\/\\/");
        Matcher matcher = pattern.matcher(url);

        if(matcher.find()){
            formattedURL = url;
        }else{
            formattedURL = "http://" + url;
        }

        return formattedURL;
    }

    /**
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
     * @return current date.
     */
    private static String getCurrentDate(){

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MMM dd/yyyy");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    /**
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
        Document doc = Jsoup.connect(formattedUrl).get();
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

