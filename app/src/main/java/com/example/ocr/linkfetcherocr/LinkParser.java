package com.example.ocr.linkfetcherocr;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by Joel on 11/29/16.
 */

public class LinkParser {

    private static final String LOG_TAG = LinkParser.class.getSimpleName();

    // Pattern for recognizing a URL, based off RFC 3986
    private static final Pattern linkPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    //Regular expression for finding email addresses
    private static final Pattern emailPattern = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9]+");

    //Regular expression for finding email addresses
    private static final Pattern phoneNumberPattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");

    /**
     * Create a private constructor because no one should ever create a {@link LinkParser} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name LinkParser (and an object instance of LinkParser is not needed).
     */
    private LinkParser(){
        //Empty Constructor
    }


    public static String getLink(String text){
        //if no link is found then return N/A
        String link = "N/A";
        //List<String> links = new ArrayList<String>();

        //Compare the text given with the regular expression
        Matcher matcher = linkPattern.matcher(text);

        Log.v(LOG_TAG, "Retrieving link...");

        while (matcher.find()){
            link = text.substring(matcher.start(0),
                    matcher.end(0));
            Log.v(LOG_TAG, "Link retrieved: " + link);
        }

        Log.v(LOG_TAG, "All links retrieved: " + link);
        return link;
    }

    public static String getEmail(String text){
        //if no email is found then return N/A
        String email = "N/A";
        //List<String> emails = new ArrayList<String>();

        //Compare the text given with the regular expression
        Matcher matcher = emailPattern.matcher(text);

        while (matcher.find()){
            for( int groupIdx = 0; groupIdx < matcher.groupCount()+1; groupIdx++ ){
                email = matcher.group(groupIdx);
                //emails.add(email);
                Log.v(LOG_TAG,"Email retrived: " + email);
            }
        }

        return email;
    }

    public static String getPhoneNumber(String text){
        //if no phone number is found then return N/A
        String phoneNumber = "N/A";

        //Compare the text given with the regular expression
        Matcher matcher = phoneNumberPattern.matcher(text);

        while (matcher.find()){
            for( int groupIdx = 0; groupIdx < matcher.groupCount()+1; groupIdx++ ){
                phoneNumber = matcher.group(groupIdx);
                System.out.println(phoneNumber);
            }
        }

        Log.v(LOG_TAG, "Phone number: " + phoneNumber);

        return phoneNumber;
    }
}
