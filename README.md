# RunYasso800
"Yasso 800s are a popular workout among runners who are trying to achieve a specific marathon goal."<sup>[1]</sup>

# !!!!!! WORK-IN-PROGRESS !!!!!

# Introduction 
This is a mobile phone implementation of Yasso800 in Kotlin for runners.  I intend to use this for my personal marathon training.  Hopefully, it is useful to you! (for those whom don't have a fancy running watch with this feature)

## Workflow
<img width="738" alt="workflow" src="https://user-images.githubusercontent.com/1282659/68631194-7790fd00-04af-11ea-9a5d-4ed80a8c9cd5.png">

<img src="https://user-images.githubusercontent.com/1282659/68713828-4b838380-0564-11ea-97c9-b645e0fe66f6.jpg" width="200"> 

### Configure 
- Sprint and Jog distances are configured under menu selection : default 800 meters
- Number of iterations (Sprint + Jog) pair : default 10

<img src="https://user-images.githubusercontent.com/1282659/76650050-d3d45e00-652f-11ea-81d2-3023eaefa023.jpg" width="200"> <img src="https://user-images.githubusercontent.com/1282659/76635445-7c28f900-6515-11ea-8c4a-2b43c893b1f3.jpg" width="200"> 

### Goal (run time) Activity
Activity provides the following functions.
- Name this run.
- Select user Marathon goal (run-time) in Hours:Minutes.
- Generates Yasso800 sprint time in Minutes:Seconds.

<img src="https://user-images.githubusercontent.com/1282659/68713832-4e7e7400-0564-11ea-9cb4-7b75d55d30d3.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/68714362-5be82e00-0565-11ea-802d-7c3aa2ed0555.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/77270701-a8154e80-6c7a-11ea-908c-cf0a66de26ef.jpg" width="200">

### Run Activity
Activity manages the running portion of Yasso800.  For detail of Yasso800 workout, see below reference article.  
Supported functions are as follow:
- GPS location service
- track/location/timing of 800 meter x 10 runs
- track/location/timing of 800 meter x 10 jogs 

Note: Failure to meet the time goal at any point(s) will produce a red highlight.  The run <-> jog loop will continue until all 10X have been completed.  

<img src="https://user-images.githubusercontent.com/1282659/74595865-1758ac80-5004-11ea-8dd6-c38dfc117162.jpg" width="200"> 

#### Data
http://erdraw.com/graphs/487344524370/edit
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

IDLE -> START -> SPRINT <-> JOG ->
<img src="https://user-images.githubusercontent.com/1282659/77270772-e3b01880-6c7a-11ea-9651-8b9688b7fc4c.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/77270817-fcb8c980-6c7a-11ea-8966-92d5229db13c.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/77270830-07735e80-6c7b-11ea-9230-108e3fbfbfd9.jpg" width="200"> 

-> PAUSE -> DONE\
<img src="https://user-images.githubusercontent.com/1282659/77334281-966f8d80-6cf2-11ea-8b46-2455e2a7d501.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/77270846-122df380-6c7b-11ea-8b2d-d8d88a6a6688.jpg" width="200"> 

### Result Activity
Activity presents run and jog results in map and sprint listing.\
Splits separated by map markers for selection to obtain detail recorded metrics.\

PLEASE INSERT YOUR GOOGLE MAP KEY in values/google_map_api.xml before running app.
<string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">!! MY_KEY_HERE !!</string>\
<img src="https://user-images.githubusercontent.com/1282659/77263354-1d2a5900-6c66-11ea-8129-7c84f70166ef.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/77124552-338ba700-6a11-11ea-8af6-5a800ad1413b.jpg" width="200">

### Persist (Share) Activity
Activity provides persistence (share) methods to user.  They include the follow:
- Facebook - summary
- Email - xml file attachments
- Drive - xml files
- database management - delete 

<img src="https://user-images.githubusercontent.com/1282659/68807583-b220a400-062d-11ea-9494-7003950ac881.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/68807590-b482fe00-062d-11ea-947b-2c4ea054e054.jpg" width="200"> 

### Example Email received

Header msg

Race Goal:2hours 5minutes 0seconds

Sprint Goal:0hours 2minutes 5seconds

Total Run distance:36.716938614845276m time:0hours 0minutes 15seconds\
Total Jog distance:26.044604063034058m time:0hours 1minutes 37seconds

{"Performance":[\
{"split": 0, {"type": "jog", "distance":20.631344318389893, "elapsed":"0hours 0minutes 8seconds"},\
{"split": 0, {"type": "sprint", "distance":30.197261810302734, "elapsed":"0hours 0minutes 5seconds"},\
{"split": 1, {"type": "jog", "distance":5.413259744644165, "elapsed":"0hours 1minutes 28seconds"},\
{"split": 1, {"type": "sprint", "distance":6.5196768045425415, "elapsed":"0hours 0minutes 9seconds"}]


{"Splits":[
{"dis":20.631344318389893,"endLat":44.9112042,"endLong":-93.3179533,"endTime":1584745186015,"run_type":"jog","splitIndex":0,"startLat":44.9113008,"startLong":-93.3180796,"startTime":1584745177179},
{"dis":30.197261810302734,"endLat":44.9113352,"endLong":-93.3181378,"endTime":1584745171185,"run_type":"sprint","splitIndex":0,"startLat":44.9112409,"startLong":-93.317998,"startTime":1584745165917},
{"dis":5.413259744644165,"endLat":44.9112118,"endLong":-93.3179725,"endTime":1584745296173,"run_type":"jog","splitIndex":1,"startLat":44.9112271,"startLong":-93.3179993,"startTime":1584745207179},
{"dis":6.5196768045425415,"endLat":44.9112248,"endLong":-93.3179962,"endTime":1584745201180,"run_type":"sprint","splitIndex":1,"startLat":44.9112395,"startLong":-93.3179862,"startTime":1584745191194}]


{"Steps":[
{"dis":14.975629806518555,"latitude":44.9112409,"longitude":-93.317998,"run_type":"sprint","splitIndex":0,"stepIndex":0,"time":1584745165917},
{"dis":15.22163200378418,"latitude":44.9113352,"longitude":-93.3181378,"run_type":"sprint","splitIndex":0,"stepIndex":1,"time":1584745171185},
{"dis":5.978060245513916,"latitude":44.9113008,"longitude":-93.3180796,"run_type":"jog","splitIndex":0,"stepIndex":2,"time":1584745177179},
{"dis":14.653284072875977,"latitude":44.9112042,"longitude":-93.3179533,"run_type":"jog","splitIndex":0,"stepIndex":3,"time":1584745186015},
{"dis":4.705210208892822,"latitude":44.9112395,"longitude":-93.3179862,"run_type":"sprint","splitIndex":1,"stepIndex":4,"time":1584745191194},
{"dis":1.8144665956497192,"latitude":44.9112248,"longitude":-93.3179962,"run_type":"sprint","splitIndex":1,"stepIndex":5,"time":1584745201180},
{"dis":0.3539195954799652,"latitude":44.9112271,"longitude":-93.3179993,"run_type":"jog","splitIndex":1,"stepIndex":6,"time":1584745207179},
{"dis":0.6080166101455688,"latitude":44.911231,"longitude":-93.3179939,"run_type":"jog","splitIndex":1,"stepIndex":7,"time":1584745216117},
{"dis":0.7732329964637756,"latitude":44.9112244,"longitude":-93.3179908,"run_type":"jog","splitIndex":1,"stepIndex":8,"time":1584745221185},
{"dis":0.23923984169960022,"latitude":44.9112247,"longitude":-93.3179878,"run_type":"jog","splitIndex":1,"stepIndex":9,"time":1584745227184},
{"dis":0.07850266993045807,"latitude":44.911225,"longitude":-93.3179869,"run_type":"jog","splitIndex":1,"stepIndex":10,"time":1584745236202},
{"dis":0.2721477746963501,"latitude":44.9112254,"longitude":-93.3179903,"run_type":"jog","splitIndex":1,"stepIndex":11,"time":1584745246188},
{"dis":0.17780806124210358,"latitude":44.911227,"longitude":-93.3179903,"run_type":"jog","splitIndex":1,"stepIndex":12,"time":1584745251172},
{"dis":0.725297212600708,"latitude":44.9112206,"longitude":-93.3179885,"run_type":"jog","splitIndex":1,"stepIndex":13,"time":1584745266174},
{"dis":0.3918304145336151,"latitude":44.9112171,"longitude":-93.3179891,"run_type":"jog","splitIndex":1,"stepIndex":14,"time":1584745271175},
{"dis":1.0636401176452637,"latitude":44.9112094,"longitude":-93.3179811,"run_type":"jog","splitIndex":1,"stepIndex":15,"time":1584745286197},
{"dis":0.7296244502067566,"latitude":44.9112118,"longitude":-93.3179725,"run_type":"jog","splitIndex":1,"stepIndex":16,"time":1584745296173}]

Footer msg

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
