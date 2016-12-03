package com.example.ocr.linkfetcherocr;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.StringTokenizer;
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
    private static final Pattern TITLE_TAG =
            Pattern.compile("\\<title>(.*)\\</title>", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);

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

        String url = null;
        Bitmap favIcon = null;
        String name = null;
        String tabName = null;
        String date = null;

        try {
            //if its a valid url then make it the new url, else leave it null
            if(validLink(pUrl)){
                url = pUrl;
                Log.v(LOG_TAG, "Page URL: " + url);
            }

            tabName = getPageTitle(pUrl);
            Log.v(LOG_TAG, "Page Title: " + tabName);

        }catch (IOException i){
            Log.e(LOG_TAG, i.getMessage());
        }

        link = new Link(url, favIcon, name, tabName, date);

        return link;
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
     * @param url link to site
     * @return Drawable object (favicon) from a url link.
     */
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable drawable = Drawable.createFromStream(is, "src name");
            return drawable;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean validLink(String pUrl) throws IOException{

        URL u = new URL(pUrl);
        URLConnection conn = u.openConnection();

        // ContentType is an inner class defined below
        ContentType contentType = getContentTypeHeader(conn);

        if (!contentType.contentType.equals("text/html"))
            return false; // don't continue if not HTML

        return true;
    }

    /**
     * @param url the HTML page
     * @return title text (null if document isn't HTML or lacks a title tag)
     * @throws IOException
     */
    public static String getPageTitle(String url) throws IOException {
        URL u = new URL(url);
        URLConnection conn = u.openConnection();

        // ContentType is an inner class defined below
        ContentType contentType = getContentTypeHeader(conn);
        if (!contentType.contentType.equals("text/html"))
            return null; // don't continue if not HTML
        else {
            // determine the charset, or use the default
            Charset charset = getCharset(contentType);
            if (charset == null)
                charset = Charset.defaultCharset();

            // read the response body, using BufferedReader for performance
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
            int n = 0, totalRead = 0;
            char[] buf = new char[1024];
            StringBuilder content = new StringBuilder();

            // read until EOF or first 8192 characters
            while (totalRead < 8192 && (n = reader.read(buf, 0, buf.length)) != -1) {
                content.append(buf, 0, n);
                totalRead += n;
            }
            reader.close();

            // extract the title
            Matcher matcher = TITLE_TAG.matcher(content);
            if (matcher.find()) {
            /* replace any occurrences of whitespace (which may
             * include line feeds and other uglies) as well
             * as HTML brackets with a space */
                return matcher.group(1).replaceAll("[\\s\\<>]+", " ").trim();
            }
            else
                return null;
        }
    }

    /**
     * @param url the HTML page
     * @return favIcon url (null if document isn't HTML or lacks a favicon)
     * @throws IOException
     */
    public static String getPageFavIcon(String url) throws IOException {

        URL u = new URL(url);
        URLConnection conn = u.openConnection();

        // ContentType is an inner class defined below
        ContentType contentType = getContentTypeHeader(conn);
        if (!contentType.contentType.equals("text/html"))
            return null; // don't continue if not HTML
        else {
            // determine the charset, or use the default
            Charset charset = getCharset(contentType);
            if (charset == null)
                charset = Charset.defaultCharset();

            // read the response body, using BufferedReader for performance
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
            int n = 0, totalRead = 0;
            char[] buf = new char[1024];
            StringBuilder content = new StringBuilder();

            // read until EOF or first 8192 characters
            while (totalRead < 8192 && (n = reader.read(buf, 0, buf.length)) != -1) {
                content.append(buf, 0, n);
                totalRead += n;
            }
            reader.close();

            // extract the favicon
            Matcher matcher = FAVICON_TAG.matcher(content);
            Matcher matcher2 = FAVICON_TAG2.matcher(content);

            //Pattern for find the Href, so it can extract the link to the favicon
            Pattern pattern =
                    Pattern.compile("href=\"(.*?)\"", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
            Matcher matcher3 = pattern.matcher(content);

            /* url where favicon could be located.
             * Some times the favicon is stored in the root of the server under
             * www.domain.xxx/favicon.ico
             */
            String favLink = url + "/favicon.ico";

            if (matcher.find() || matcher2.find()) {
            /* replace any occurrences of whitespace (which may
             * include line feeds and other uglies) as well
             * as HTML brackets with a space */

                //Get the href value from the link tag
                if(matcher3.find()){
                    favLink = matcher3.group(1).replaceAll("href=\"", "").replaceAll("\"", "").trim();
                    Log.v(LOG_TAG, "favLink: " + favLink);
                    return matcher.group(1).replaceAll("[\\s\\<>]+", " ").trim();
                }
                else{
                    Log.e(LOG_TAG, "Error retrieving favicon link");
                    return null;
                }
            }
            else if(validLink(favLink)){
                return favLink;
            }
            else
                return null;
        }
    }

    /**
     * Loops through response headers until Content-Type is found.
     * @param conn
     * @return ContentType object representing the value of
     * the Content-Type header
     */
    private static ContentType getContentTypeHeader(URLConnection conn) {
        int i = 0;
        boolean moreHeaders = true;
        do {
            String headerName = conn.getHeaderFieldKey(i);
            String headerValue = conn.getHeaderField(i);
            if (headerName != null && headerName.equals("Content-Type"))
                return new ContentType(headerValue);

            i++;
            moreHeaders = headerName != null || headerValue != null;
        }
        while (moreHeaders);

        return null;
    }

    private static Charset getCharset(ContentType contentType) {
        if (contentType != null && contentType.charsetName != null && Charset.isSupported(contentType.charsetName))
            return Charset.forName(contentType.charsetName);
        else
            return null;
    }

    /**
     * Class holds the content type and charset (if present)
     */
    private static final class ContentType {
        private static final Pattern CHARSET_HEADER = Pattern.compile("charset=([-_a-zA-Z0-9]+)", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);

        private String contentType;
        private String charsetName;
        private ContentType(String headerValue) {
            if (headerValue == null)
                throw new IllegalArgumentException("ContentType must be constructed with a not-null headerValue");
            int n = headerValue.indexOf(";");
            if (n != -1) {
                contentType = headerValue.substring(0, n);
                Matcher matcher = CHARSET_HEADER.matcher(headerValue);
                if (matcher.find())
                    charsetName = matcher.group(1);
            }
            else
                contentType = headerValue;
        }
    }
}
