# GitTrend
An Android App that lists the most trending repositories in Github.
<img src="https://github.com/mdazharips/GitTrend/blob/main/Screenshots/loading.jpg" width="200" style="max-width:100%;">   <img src="https://github.com/mdazharips/GitTrend/blob/main/Screenshots/nonetwork.jpg" width="200" style="max-width:100%;"> <img src="https://github.com/mdazharips/GitTrend/blob/main/Screenshots/loaded%20data.jpg" width="200" style="max-width:100%;"> <img src="https://github.com/mdazharips/GitTrend/blob/main/Screenshots/Search.jpg" width="200" style="max-width:100%;"> <img src="https://github.com/mdazharips/GitTrend/blob/main/Screenshots/pull%20to%20reload.gif" width="200" style="max-width:100%;"></br></br>

#### App Features
* Users can view the most trending repositories in Github.
* Users can Search and filter based on name.
 
 #### The app includes the following main components:

* A local database that servers as a single source of truth for data presented to the user. 
* A web api service.


#### App Packages
* <b>data</b> - contains 
    * <b>api</b> -  make api calls using android-volley. 
    * <b>db</b> - contains the db classes to store data.
* <b>ui</b> - contains classes needed to display Activity.



#### App Specs
* Minimum SDK 15
* [Picasso](http://square.github.io/picasso/) for image loading.
* [mcxiaoke volley](https://github.com/mcxiaoke/android-volley/) for API integration.
