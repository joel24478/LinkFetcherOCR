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
We have the camera detecting text and now are in the process of getting everytrhing together.
The GUI is still a work in progress but you can see what we are going for here is a picture below.



