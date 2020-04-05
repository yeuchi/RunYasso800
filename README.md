# RunYasso800
"Yasso 800s are a popular workout among runners who are trying to achieve a specific marathon goal."<sup>[1]</sup>

# !!!!!! WORK-IN-PROGRESS !!!!!

# Introduction 
This is a mobile phone implementation of Yasso800 in Kotlin for runners.  I intend to use this for my personal marathon training.  Hopefully, it is useful to you! (for those whom don't have a fancy running watch with this feature)

## Workflow
<img width="738" alt="workflow" src="https://user-images.githubusercontent.com/1282659/68631194-7790fd00-04af-11ea-9a5d-4ed80a8c9cd5.png">

<img src="https://user-images.githubusercontent.com/1282659/68713828-4b838380-0564-11ea-97c9-b645e0fe66f6.jpg" width="200"> 

### Configure 
Thinking out of the box ?  Want to rest (jog) less ?  
You may adjust some configurable parameters under the menu.
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
- track/locate/time of 800 meter x 10 runs
- track/locate/time of 800 meter x 10 jogs 

#### Data
http://erdraw.com/graphs/487344524370/edit \
<img width="583" src="https://user-images.githubusercontent.com/1282659/70194212-37482780-16c7-11ea-8cf8-de4c32708f22.png">

A Yasso800 Entity is composed of 10 x 2 (sprint/jog) Split Entities.  Each Split is a measure of 800 meter.  Within a split, samplings of GPS lat/long are considered every 200 milli-seconds and stored in the database as Step Entities.

#### State machine 
- IDLE - Activity intialization or CLEAR by user
- START - transition to SPRINT
- SPRINT <-> JOG - loop for 10X 
- DONE - Yasso800 completed successfully.
- PAUSE - User put the porgram on temporary suspension.
- RESUME - User de-selects PAUSE button; program resumes.
- ERROR - unexpected conditions (Examples: GPS fails or Phone dies)

<img width="495" alt="Screen Shot 2020-02-15 at 3 30 04 PM" src="https://user-images.githubusercontent.com/1282659/74596116-145fbb00-5008-11ea-9a89-86802782d4dc.png">

IDLE -> START -> SPRINT <-> JOG ->PAUSE\
<img src="https://user-images.githubusercontent.com/1282659/77359501-a8b0f200-6d19-11ea-9efb-68c7dfc4da21.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/77270817-fcb8c980-6c7a-11ea-8966-92d5229db13c.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/77270830-07735e80-6c7b-11ea-9230-108e3fbfbfd9.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/77334281-966f8d80-6cf2-11ea-8b46-2455e2a7d501.jpg" width="200"> 

-> DONE\
<img src="https://user-images.githubusercontent.com/1282659/77836547-acc78000-711c-11ea-9f49-dc592b8a3153.jpg" width="200"> 

### Result Activity
- Geometric view of run and jog iterations onn map. 
- Map markers defines split start location. [Run in green , Jog in cyan] 
- Click on marker to reveal details.

PLEASE INSERT YOUR GOOGLE MAP KEY in values/google_map_api.xml before running app.
<string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">!! MY_KEY_HERE !!</string>\
<img src="https://user-images.githubusercontent.com/1282659/77263354-1d2a5900-6c66-11ea-8129-7c84f70166ef.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/78047459-99dbc800-7335-11ea-8692-b2a6ce3113a9.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/78464074-91272100-76aa-11ea-9c39-c54893d1f346.jpg" width="200">

### Persist (Share) Activity
Activity provides persistence (share) methods to user.  They include the follow:
- Facebook - summary
- Email - xml file attachments
- Drive - xml files
- database management - delete 

<img src="https://user-images.githubusercontent.com/1282659/68807583-b220a400-062d-11ea-9494-7003950ac881.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/68807590-b482fe00-062d-11ea-947b-2c4ea054e054.jpg" width="200"> 

### Example Email received
https://github.com/yeuchi/RunYasso800/tree/master/results \
<img src="https://user-images.githubusercontent.com/1282659/77836627-6d4d6380-711d-11ea-90b5-d4c449cb2657.jpg" width="200">

### How to Do a Yasso 800 Workout 
"Take your marathon goal time in hours and minutes and convert this to minutes and seconds. 
For example, if your marathon goal is 3 hours and 10 minutes then convert that to 3 minutes and 10 seconds.

- First, do an easy warm-up of 5 to 10 minutes jogging and a few warm-up exercises.
- Next, try to run 800 meters (approximately 1/2 mile) at your converted time (3:10 in this example).
- Recover after each 800 by jogging or walking for the same amount of time (again, 3:10 in this example).
- Start with three or four repetitions per workout in the first week.
- Don't forget to cool down with 5 minutes of easy running or walking, followed by stretching."<sup>[1]</sup>

## Test
This application has been tested on Samsung Galaxy9.

## IDE
- Android Studio 3.5.2
- JRE: 1.8.0_202-release-1483-b49-5587405 x86_64
- JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
- compileSdkVersion 28

## Watch
Look forward to implementation.

# References
1. How to do Yasso 800s, by Christine Luff, VeryWellFit, August 04, 2019
https://www.verywellfit.com/how-to-do-yasso-800s-2911888

2. How to use Yasso 800 in your marathon training, by Jason Fitzgerald, Active.com, October 20, 2019
https://www.active.com/running/articles/how-to-use-yasso-800s-in-your-marathon-training

3. Distance icon credit to Freepike from Flaticon
https://www.flaticon.com/free-icon/distance-to-travel-between-two-points_55212#term=distance&page=1&position=4

4. Android-Location Based services, GPSTracker, TutorialPoints, 2019
https://www.tutorialspoint.com/android/android_location_based_services.htm

5. Polylines and Polygons to Represent Routes and Areas, Android documentation
https://developers.google.com/maps/documentation/android-sdk/polygon-tutorial

6. Reduce GPS data error on Android with Kalman filter and accelerometer, by Oleg Katkov, Mar 19, 2018
https://blog.maddevs.io/reduce-gps-data-error-on-android-with-kalman-filter-and-accelerometer-43594faed19c

7. Google Maps Markers, by Concept211, August 4, 2015
https://github.com/Concept211/Google-Maps-Markers
