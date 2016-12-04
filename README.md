# LinkFetcherOCR
Fetches links of of your phones camera


Jonathan Wydola



This app will open to the main activity, user will be prompted with 2 selections, to scan image using camera or to scan image using a picture from the gallery. The picture will then be rendered by the goolge vision api to pull a url from the picture. This url will then be stored in a database that will keep previously searched items. A match on previously searched items will act as a cache to quickly open url’s that are contained already in the database. The title will be pulled from the html page and will display the title in place of the url.

![alt tag](http://i795.photobucket.com/albums/yy234/joel24478/Screen%20Shot%202016-11-01%20at%208.48.10%20PM_zpsy88hv2zc.png)

##Tasks:##
####Martin####
* GUI
* Camera implementation

####Joel####
* API call
* HTTP Request

####Jonathan####
* JSON
* SQL Queries

whoever wants to take these
* Parse title result (html)
* Fetch url images

##Progress Report #1:##
####Joel####
Google API - We are using the google api to fetch the text from images. We moved away from using tesseract, because we were having horrible results where it was getting the text from images completely wrong. Its implemented into our code.
####Martin####
We have the camera detecting text and now are in the process of getting everything together.
The GUI is still a work in progress, this is what we have so far.

![alt tag](http://i50.photobucket.com/albums/f333/MartinRudzki/510197de-2de6-4b86-b339-daca73fbdd5e_zpsiqwg7z26.jpg)
####Jonathan####
The Database is completed and we are in the process of getting everything together.

##Progress Report #2:##
####Joel####
Google API - Up and running, is working with the images

####Martin####
GUI is comming along but still needs a little polishing. Camera is not where we want it.

####Jonathan####
The Database is up and running and we have it working great with the rest of the app.

##Progress Report #3:##
####Joel####
The static class LinkParser has a set of static functions that are able to find email’s, URL’s and phone number’s. It uses the Pattern and Matcher classes that take in regular expression to find matching text.

The static class QueryUtils has a set of static functions that can create Link objects using the Link class, grab images from a url, and travers html files from a url to grab information such as title, and favicon. I used the jsoup library to traverse the html page, and the picasso library to grab images from a url. Both libraries are quick and saved us much time in. I could have wrote a function to grab images from a url, but picasso has many other useful functionality like its own garbage collector.

The asynchronous class LinkLoader uses both the classes mentioned above to do all the dirty work in the background as to not slow down our app. It's not completely finished because I have to fetch the data from the database and populate the view.

####Martin####
Worked on bringing on adding extra GUI elements together as well as finally adding the camera activity to the overall app. They were initially set as two different projects. Also was able to pull text from the camera app call that takes the text on the screen and put it as a string. That string gets passed onto (Joel’s part) where he parses the string and then gets sent to the database(Jonathan’s part). Currently working on adding an edit feature to be able to edit the saved link, phone numbers or email address.

####Jonathan####
db is now created with a single take named lnkftchr. The database also functions with the URI and is of similar structure to the pets program but handles the displaying of data differently. Has the columns name, eadress, url, time and is functional with insertion and deletion. The table supports truncation and creation upon version updates. The database information is displayed using a simple cursor adapter in the linkfragments.java file. When a entry is clicked on the url associated with the entry spawns a new intent that brings up the web browser, saving the link you scanned into your browser's history. The program now is able to call the home button without crashing. It can also completely rotate from portrait to landscape to portrait.

####Picture show of the app####
1) App gui
2) Clicking on camera
3) paper of text that it is grabbing from
4) how the app looks once text is read
![alt tag](http://i50.photobucket.com/albums/f333/MartinRudzki/IMG_2502_zps2qofsgag.jpg)
![alt tag](http://i50.photobucket.com/albums/f333/MartinRudzki/IMG_2503_zpsy4vlmego.jpg)
![alt tag](http://i50.photobucket.com/albums/f333/MartinRudzki/IMG_2505_zps65ygirww.jpg)
![alt tag](http://i50.photobucket.com/albums/f333/MartinRudzki/IMG_2504_zpsrozdzqlk.jpg)


