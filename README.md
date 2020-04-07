# RunYasso800
"Yasso 800s are a popular workout among runners who are trying to achieve a specific marathon goal."<sup>[1]</sup>

# !!!!!! WORK-IN-PROGRESS !!!!!

# Tutorial
Run 16,000 meters (9.94 miles) of equal jogs and sprints.\
Yasso800 is composed of 800 meter jog followed by 800 sprint for 10 times.\
The total workout distance accomplished from Yasso800 is 16,000 meters (9.94 miles).\
Of that, total sprint distance is 8,000 meters (4.97 miles) and total jog distance is also 8,000 meters (4.97 miles).

### How to Do a Yasso 800 Workout 
"Take your marathon goal time in hours and minutes and convert this to minutes and seconds. \
For example, if your marathon goal is 3 hours and 10 minutes then convert that to 3 minutes and 10 seconds.

- First, this application's RunActivity starts with a jog.  You should warm up for the 1st 800 meters.
- Next, try to sprint 800 meters (approximately 1/2 mile).
- Recover after each 800 sprint by jogging or walking for the same distance.
- Start with three or four repetitions per workout in the first week.
- Don't forget to cool down with 5 minutes of easy running or walking, followed by stretching."<sup>[1]</sup>

## Measure of success
It is a success workout if each sprint time is under your sprint goal.

# Introduction 
This is a mobile phone implementation of Yasso800 in Kotlin for runners.  \
I intend to use this for my personal marathon training (since I don't have a Garmin).\
Any suggestion(s) and feedbacks are welcomed. 

## Workflow
<img width="738" alt="workflow" src="https://user-images.githubusercontent.com/1282659/68631194-7790fd00-04af-11ea-9a5d-4ed80a8c9cd5.png">

### Configure 
Start with less distance, iterations or want to less jog (rest) ?  
You may customize configurable parameters under the menu.
- Sprint and Jog distances are configured under menu selection : default 800 meters
- Number of iterations (Sprint + Jog) pair : default 10

<img src="https://user-images.githubusercontent.com/1282659/76650050-d3d45e00-652f-11ea-81d2-3023eaefa023.jpg" width="200"> <img src="https://user-images.githubusercontent.com/1282659/76635445-7c28f900-6515-11ea-8c4a-2b43c893b1f3.jpg" width="200">

### Goal (run time) Activity
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
<img width="583" src="https://user-images.githubusercontent.com/1282659/78603291-7cb76580-781d-11ea-813e-68308d531cb8.png">

A Yasso800 Entity is composed of 10 splits.\
Each split is composed of 2 collections, jog steps and sprint steps.\
By default (but configurable in menu), a step is recorded every 5000 milliseconds (5 seconds).\
In an example with Sprint Goal= 40 minutes, there would be 960 steps.\
( 40 minutes (sprint) + 40 minutes (jog) )  * 60 seconds / 5 seconds = 960 samples

#### State machine 
- IDLE - Activity intialization or CLEAR by user
- START - transition to SPRINT
- SPRINT <-> JOG - loop for 10X 
- DONE - Yasso800 completed successfully.
- PAUSE - User put the porgram on temporary suspension.
- RESUME - User de-selects PAUSE button; program resumes.
- ERROR - unexpected conditions (Examples: GPS fails or Phone dies)

<img width="495" alt="Screen Shot 2020-02-15 at 3 30 04 PM" src="https://user-images.githubusercontent.com/1282659/78703407-b5177c00-78cf-11ea-81c2-fda1fd8716bb.png">

IDLE -> START -> SPRINT <-> JOG ->PAUSE\
<img src="https://user-images.githubusercontent.com/1282659/77359501-a8b0f200-6d19-11ea-9efb-68c7dfc4da21.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/77270817-fcb8c980-6c7a-11ea-8966-92d5229db13c.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/77270830-07735e80-6c7b-11ea-9230-108e3fbfbfd9.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/77334281-966f8d80-6cf2-11ea-8b46-2455e2a7d501.jpg" width="200"> 

-> DONE\
<img src="https://user-images.githubusercontent.com/1282659/77836547-acc78000-711c-11ea-9f49-dc592b8a3153.jpg" width="200"> 

### Result Activity
- Map view of sprint and jog iterations. 
- Location markers defines split start position. [Sprint in green , Jog in cyan] 
- Click on marker to reveal split detail.

PLEASE INSERT YOUR GOOGLE MAP KEY in values/google_map_api.xml before running app.
<string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">!! MY_KEY_HERE !!</string>\
<img src="https://user-images.githubusercontent.com/1282659/78513888-3a861980-7774-11ea-97d9-f8be36a69eb3.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/78513892-3c4fdd00-7774-11ea-9442-430302512b54.jpg" width="200"> 

### Persist (Share) Activity
Activity provides persistence (share) methods to user.  They include the follow:
- Facebook - summary
- Email - xml file attachments
- Drive - xml files
- database management - delete 

<img src="https://user-images.githubusercontent.com/1282659/78503756-36370d80-772e-11ea-894b-a6f0fd39b6b0.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/68807590-b482fe00-062d-11ea-947b-2c4ea054e054.jpg" width="200"> 
 <img src="https://user-images.githubusercontent.com/1282659/77836627-6d4d6380-711d-11ea-90b5-d4c449cb2657.jpg" width="200">

### Example Email received
https://github.com/yeuchi/RunYasso800/tree/master/results \

## Tests

#### Manual
This application has been tested on Samsung Galaxy9.

#### Espresso 
https://github.com/yeuchi/RunYasso800/tree/master/mobile/src/androidTest/java/com/ctyeung/runyasso800

## IDE
- Android Studio 3.5.2
- JRE: 1.8.0_202-release-1483-b49-5587405 x86_64
- JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
- compileSdkVersion 28

## Wrist Watch
Look forward to implementation.  Next to implement watch <-> phone interface.

## Known Issues (Opportunities)
Civilian GPS resolution is 4 meter RMS (7.8 meter 95% Confidence Interval).\
Without data processing (such as Kalman filter), current result can look choppy like this recording below.\
<img src="https://user-images.githubusercontent.com/1282659/78703757-60c0cc00-78d0-11ea-8248-b3cdc4cd251d.jpg" width="400">

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
