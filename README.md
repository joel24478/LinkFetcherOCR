

#Final Project Paper
##Mobile App Programming 1
##Group: Jonathan Wydola, Joel Cruz, Martin Rudzki

Video of project: https://www.youtube.com/watch?v=98_KWLSdYAI&feature=youtu.be
##LinkFetcherOcr
Link fetcher ocr is a student developed android app highlighting the skills and progress of our java, xml, and general android developer knowledge. 


###Goal: 
the goal of the program is to scan links, emails, and phone numbers from the googleOCR api. Once this data is acquired, it is checked to guarantee that the data scanned from the camera is correct and properly placed into its own table in our database. This data will then be displayed from its table to a ListView within the proper fragment. The user will then be able to click entries from each fragment and have it spawn the following activities. A web page for a url, a email for a email address, and a phone dial view for a phone number

###Features: 
The programs biggest  features are the camera, the link parser, and the database implementations. 
The camera and submission of the text is a key feature. The user creates a new activity when the camera icon is selected. From that activity the user can set the textbox of the image scanned with a static value. The detect text button spawns the camera with the googleOCR api and allows text to be pulled from the scanned image. This is then saved to the text field in the previous activity. When the save button is pressed, the text is passed to query utils and the features to parse the text.
The link parser and utilities are created to validate the data provided before it is searched for or entered in the database. These utilities also handle features to parse the supplied url. Getting the favicon is an example to how a utility used to get a favicon and store that favicon url based on the url from the camera activity. An asynchronous task does this parsing and grabbing of the favicon. Once the data passes inspection it is entered into the database.
Directly from the main activity the program uses view pager to display our three fragments.  Each of these fragments represent a ListView for each individual table. To display this data we use a simpleCursorAdapter with the columns desired and the proper id in each list. When the database is clicked on, depending on which data set is clicked, a new intent is spawned. This intent will either launch a new webpage, phone dial, or email view. 

###File structure: 
This is the general file structure of the program and listed below each member is the files he worked on. Keep in mind that at some points during development we each helped other with problems that caused roadblocks in development.

*The indentation represents the text under it being sub files of the folder*

###Martin:

camera

   CameraSource.java
    
   CameraSourcePreview.java
    
   GraphicOverlay.java
CameraActivity.java

CameraView.java

OcrCaptureActivity.java

OcrDetectorProcessor.java

OcrGraphic.java

Camera_activity.xml

Ocr_capture.xml

main.xml


###Joel: 

CategoryAdapter.java

Link.java

QuesyUtils.java

Linkloader.java

SettingsActivity.java

MainActivity

res/General design of program



###Jonathan:

dbLnkFtch

LnkFtchrDbHelper.java

LnkProvider.java

LnkContract.java

LinkFragment.java

EmailFragment.java

PhoneNumberFragment.java

List_item.xml

Link_list_item.xml

Link_list_iteme.xml

Link_list_itemp.xml



/* the following tables hold the data for each type of entry, no connections are made*/
Tables included:

links(LINK_ID, name, tab_name, url, favicon, time)

phone(PHONE_ID, name, phoneNumber, time)

email(EMAIL_ID, name, email, time)
    
    ##Project Design:


(Below contains a UML of our entire app)
    
![alt tag](http://i50.photobucket.com/albums/f333/MartinRudzki/UML_zpsfyqvwgnn.jpg)


