# RunYasso800 <img src="https://user-images.githubusercontent.com/1282659/78828831-f0d34400-79aa-11ea-81fc-9f8e5c32ed59.png" width="40"> 
"Yasso 800s are a popular workout among runners who are trying to achieve a specific marathon goal."<sup>[1]</sup>

# Tutorial
Run 16,000 meters (9.94 miles) of equal jogs and sprints.\
Yasso800 is composed of 800 meter jog followed by 800 sprint for 10 times.\
The total workout distance accomplished from Yasso800 is 16,000 meters (9.94 miles).\
Of that, total sprint distance is 8,000 meters (4.97 miles) and total jog distance is also 8,000 meters (4.97 miles).

### How to Do a Yasso 800 Workout 
"Take your marathon goal time in hours and minutes and convert this to minutes and seconds. \
For example, if your marathon goal is 3 hours and 10 minutes then convert that to 3 minutes and 10 seconds.

- First, starts with a jog.  You should warm up for the 1st 800 meters.
- Next, try to sprint 800 meters (approximately 1/2 mile).
- Recover after each 800 sprint by jogging or walking for the same distance.
- Start with three or four repetitions per workout in the first week.
- Don't forget to cool down with 5 minutes of easy running or walking, followed by stretching."<sup>[1]</sup>

### Measure of success
It is a successful workout if each sprint time is under your sprint goal.

# Introduction 
This is a mobile phone implementation of Yasso800 in Kotlin for runners.  I am developing this for my personal marathon training with a polar wrist watch. It is my understanding that certain Garmin running watches have this feature already.\
Any suggestion(s) and feedbacks are welcomed. 

## Workflow
<img width="738" alt="workflow" src="https://user-images.githubusercontent.com/1282659/79381139-1d490c00-7f27-11ea-88d8-a05552fbd9cb.png">

This application is composed of 5 distint activities:  
1. Goal - set session name and race goal to determine qualifying sprint time. 
2. Run - jog and sprint 10x to complete a Yasso800 training session. 
3. Result - review your run on Google Play with details for qualifying sprints. 
4. Persist - save results via email, Google Drive, or other available on your phone. 

### Configure 
Start with less distance, iterations or want to less jog (rest) ?  
Customizable configuration is available under the menu selections.
- Jog and Sprint distances can be reduced from 800 meter. 
- Number of iterations (Jog + Sprint) can be reduced from 10 X
- GPS sampling rate can be increased or decreased from @ 5,000 milliseconds (5 seconds).
- Factory Reset for above default values is available in 'About' dialog box.

MENU -> CONFIGURATION        
MENU -> ABOUT -> FACTORY RESET\
<img src="https://user-images.githubusercontent.com/1282659/79158436-f6120380-7d9b-11ea-9973-2de91ad84a34.jpg" width="200"> <img src="https://user-images.githubusercontent.com/1282659/76635445-7c28f900-6515-11ea-8c4a-2b43c893b1f3.jpg" width="200">
<img src="https://user-images.githubusercontent.com/1282659/79158422-ef838c00-7d9b-11ea-9e85-efe89751fb1b.jpg" width="200">

### Goal Activity
Activity provides the following functions.
- Name this run.
- Select user Marathon goal (run-time) in Hours:Minutes.
- Generates Yasso800 sprint time in Minutes:Seconds.

<img src="https://user-images.githubusercontent.com/1282659/78502933-3680da00-7729-11ea-8c90-12683cfd88fa.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/78502935-37197080-7729-11ea-9c94-8df088358cd4.jpg" width="200">    <img src="https://user-images.githubusercontent.com/1282659/78502936-384a9d80-7729-11ea-9c0b-a056b88f7841.jpg" width="200">

### Run Activity
Activity manages the running portion of Yasso800.  For detail of Yasso800 workout, see below reference article.  
Supported functions are as follow:
- GPS location service
- track/locate/time of 800 meter x 10 sprints
- track/locate/time of 800 meter x 10 jogs 

#### Data
http://erdraw.com/graphs/487344524370/edit \
<img width="583" src="https://user-images.githubusercontent.com/1282659/78720941-3d0b7f00-78ec-11ea-87cb-ba8875fb142e.png">

A Yasso800 Entity is composed of 10 splits.\
Each split is composed of 2 collections, jog steps and sprint steps.\
By default (but configurable in menu), a step is recorded every 5000 milliseconds (5 seconds).\
In an example with Sprint Goal= 40 minutes, there would be 960 steps.\
( 40 minutes (sprint) + 40 minutes (jog) )  * 60 seconds / 5 seconds = 960 samples

#### State machine 
- IDLE : Starting point, nothing is happening.
- START : button <img src="https://user-images.githubusercontent.com/1282659/79134254-03ff5e80-7d73-11ea-97eb-0da899e0403d.png" width="20"> begin session with a jog for 800 meters. (no time constraint)
- JOG -> SPRINT : iterate for 10 times. 
- DONE : Yasso800 completed; button (>) to ResultActivity. 
- PAUSE : button <img src="https://user-images.githubusercontent.com/1282659/79134259-05c92200-7d73-11ea-8e5b-4c24bee74d15.png" width="20"> puts session on temporary suspension.
- RESUME : button <img src="https://user-images.githubusercontent.com/1282659/79134254-03ff5e80-7d73-11ea-97eb-0da899e0403d.png" width="20"> to resume jog or sprint.
- CLEAR : button <img src="https://user-images.githubusercontent.com/1282659/79134266-0792e580-7d73-11ea-8efd-5bfe352d7e4f.png" width="20"> to delete all and return to IDLE state.
- ERROR : unexpected conditions (Examples: GPS fails or Phone dies)

<img width="495" alt="Screen Shot 2020-02-15 at 3 30 04 PM" src="https://user-images.githubusercontent.com/1282659/78703407-b5177c00-78cf-11ea-81c2-fda1fd8716bb.png">

IDLE -> START -> JOG <-> SPRINT -> PAUSE\
<img src="https://user-images.githubusercontent.com/1282659/77359501-a8b0f200-6d19-11ea-9efb-68c7dfc4da21.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/77270830-07735e80-6c7b-11ea-9230-108e3fbfbfd9.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/77270817-fcb8c980-6c7a-11ea-8966-92d5229db13c.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/79081035-4d9e6980-7cdf-11ea-984a-60c3c6e112ca.jpg" width="200"> 

-> DONE\
<img src="https://user-images.githubusercontent.com/1282659/77836547-acc78000-711c-11ea-9f49-dc592b8a3153.jpg" width="200"> 

### Result Activity
- Map view of sprint and jog iterations. 
- Location markers defines split start position. 
- Click on marker to reveal split detail.\
<img src="https://raw.githubusercontent.com/Concept211/Google-Maps-Markers/master/images/marker_blue2.png" width="15"> Jog \
<img src="https://raw.githubusercontent.com/Concept211/Google-Maps-Markers/master/images/marker_green3.png" width="15"> Sprint (meets goal)\
<img src="https://raw.githubusercontent.com/Concept211/Google-Maps-Markers/master/images/marker_red4.png" width="15"> Sprint (failed)

PLEASE INSERT YOUR GOOGLE MAP KEY in values/google_map_api.xml before running app.
<string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">!! MY_KEY_HERE !!</string>\
<img src="https://user-images.githubusercontent.com/1282659/78513888-3a861980-7774-11ea-97d9-f8be36a69eb3.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/79158438-f7433080-7d9b-11ea-9de9-c89f53b14438.jpg" width="200"> 

### Persist (Share) Activity
Activity provides persistence (share) methods to user.  They include the follow:
- Facebook - summary
- Email - xml file attachments
- Drive - xml files
- database management - delete 

<img src="https://user-images.githubusercontent.com/1282659/78736600-275d8000-7913-11ea-8463-8c2329d8766c.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/68807590-b482fe00-062d-11ea-947b-2c4ea054e054.jpg" width="200"> 
 <img src="https://user-images.githubusercontent.com/1282659/77836627-6d4d6380-711d-11ea-90b5-d4c449cb2657.jpg" width="200">

### Example Email received
https://github.com/yeuchi/RunYasso800/tree/master/results 

## Tests

#### Manual
This application has been tested on Samsung Galaxy S9 phone.

#### Espresso 
https://github.com/yeuchi/RunYasso800/tree/master/mobile/src/androidTest/java/com/ctyeung/runyasso800

## IDE
- Android Studio 3.5.2
- JRE: 1.8.0_202-release-1483-b49-5587405 x86_64
- JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
- compileSdkVersion 28

### Skills Practiced
JetPack Architecture MVVM, Databinding, LiveData, Extension method, Room persistence, Location API, Google Map, EmailIntent, Espresso tests, Co-routine, State-machine, Gson/JSON, Vibration, Alarm-beep.

## Wrist Watch
Look forward to implementation.  Next to implement watch <-> phone interface.

## Known Issues (Opportunities)
1. Civilian GPS resolution is 4 meter RMS (7.8 meter 95% Confidence Interval).\
Without data processing (such as Kalman filter), current result can look choppy like this recording below. \
<img src="https://user-images.githubusercontent.com/1282659/78793671-578b3a00-7978-11ea-84e7-882177eb0506.jpg" width="400">

2. Database table/data/SharePref Normalization: remove duplications.
3. Landscape Layout support: RunActivity - state machine requires refactoring.
4. Dagger dependency injection.

# Google Play Versions
1. April 11, 2020: 1st available on Google Play; functionally tested by 1 self run work-out.
2. April 12, 2020: available on Google Play.\
   a. Refactored viewModels. \
   b. Fixed: PersistActivity no longer renders partial data when there are no splits.
3. April 12, 2020: available on Google Play. \
   a. Fixed: RunActivity PAUSE / RESUME.
4. April 12, 2020: available on Google Play.\
   a. Fixed: RunActivity CLEAR. 
5. April 14, 2020: available on Google Play.\
   a. About Dialog with factory reset.\
   b. Refactored SplitDetailDialogFragment viewModel / databinding.
6. April 16, 2020: submitted for Google approval.\
   a. Fixed: Steps for sprint are sometimes registered as jog.\
   b. Fixed: Jog before Sprint order for menu.\
   c. Fixed: Sprint's 'meet-goal' unit in milli-seconds, not seconds.
   
# References
1. How to do Yasso 800s, by Christine Luff, VeryWellFit, August 04, 2019\
https://www.verywellfit.com/how-to-do-yasso-800s-2911888

2. How to use Yasso 800 in your marathon training, by Jason Fitzgerald, Active.com, October 20, 2019\
https://www.active.com/running/articles/how-to-use-yasso-800s-in-your-marathon-training

3. Distance icon credit to Freepike from Flaticon\
https://www.flaticon.com/free-icon/distance-to-travel-between-two-points_55212#term=distance&page=1&position=4

4. Android-Location Based services, GPSTracker, TutorialPoints, 2019\
https://www.tutorialspoint.com/android/android_location_based_services.htm

5. Polylines and Polygons to Represent Routes and Areas, Android documentation\
https://developers.google.com/maps/documentation/android-sdk/polygon-tutorial

6. Reduce GPS data error on Android with Kalman filter and accelerometer, by Oleg Katkov, Mar 19, 2018\
https://blog.maddevs.io/reduce-gps-data-error-on-android-with-kalman-filter-and-accelerometer-43594faed19c

7. Google Maps Markers, by Concept211, August 4, 2015\
https://github.com/Concept211/Google-Maps-Markers

8. Android Asset Studio - Launcher icon generator\
https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html

9. LiveDataExtension, by Rafa Vázquez, February 6, 2018\
https://gist.github.com/Sloy/7a267237f7bc27a2057be744209c1c61

10. Testing LiveData on Android, Ale's MainThread, by Alessandro Diaferia, December 17, 2018
https://alediaferia.com/2018/12/17/testing-livedata-room-android/
