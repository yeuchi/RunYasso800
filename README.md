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

<img src="https://user-images.githubusercontent.com/1282659/68713832-4e7e7400-0564-11ea-9cb4-7b75d55d30d3.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/68714362-5be82e00-0565-11ea-802d-7c3aa2ed0555.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/68714364-5e4a8800-0565-11ea-892d-ff842ad82eaf.jpg" width="200">

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

IDLE -> START -> SPRINT -> PAUSE\
<img src="https://user-images.githubusercontent.com/1282659/76312197-82626f80-62a0-11ea-9f15-ff4f8b8f25e8.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/76326432-a03acf00-62b6-11ea-8516-88a4cef42dda.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/76674258-52a2b880-657b-11ea-8c2d-789b356bccbb.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/76326435-a0d36580-62b6-11ea-8dd5-750c32df8f69.jpg" width="200"> 

### Result Activity
Activity presents run and jog results in map and sprint listing.\
Splits separated by map markers for selection to obtain detail recorded metrics.\

PLEASE INSERT YOUR GOOGLE MAP KEY in values/google_map_api.xml before running app.
<string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">!! MY_KEY_HERE !!</string>\
<img src="https://user-images.githubusercontent.com/1282659/72771599-a36ee400-3bbe-11ea-8d76-e2eb1ff11cda.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/77124552-338ba700-6a11-11ea-8af6-5a800ad1413b.jpg" width="200">

### Persist (Share) Activity
Activity provides persistence (share) methods to user.  They include the follow:
- Facebook - summary
- Email - xml file attachments
- Drive - xml files
- database management - delete 

<img src="https://user-images.githubusercontent.com/1282659/68807583-b220a400-062d-11ea-9494-7003950ac881.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/68807590-b482fe00-062d-11ea-947b-2c4ea054e054.jpg" width="200"> 

### Example data received via email

Header msg

{"Splits":
[{"dis":2.073444226756692,"endLat":44.911246,"endLong":-93.3179815,"endTime":1584724769197,"run_type":"jog","splitIndex":0,"startLat":44.9112449,"startLong":-93.3179797,"startTime":1584724334196},
{"dis":2.8574838638305664,"endLat":44.9112455,"endLong":-93.3179789,"endTime":1584724325221,"run_type":"sprint","splitIndex":0,"startLat":44.9112411,"startLong":-93.3179959,"startTime":1584724319776}]}

{"Steps":
[{"dis":1.4287419319152832,"latitude":44.9112411,"longitude":-93.3179959,"run_type":"sprint","splitIndex":0,"stepIndex":0,"time":1584724319776},
{"dis":1.4287419319152832,"latitude":44.9112455,"longitude":-93.3179789,"run_type":"sprint","splitIndex":0,"stepIndex":1,"time":1584724325221},
{"dis":0.09185320138931274,"latitude":44.9112449,"longitude":-93.3179797,"run_type":"jog","splitIndex":0,"stepIndex":2,"time":1584724334196},
{"dis":0.07302355021238327,"latitude":44.9112444,"longitude":-93.3179803,"run_type":"jog","splitIndex":0,"stepIndex":3,"time":1584724339218},
{"dis":0.06496883183717728,"latitude":44.911244,"longitude":-93.3179809,"run_type":"jog","splitIndex":0,"stepIndex":4,"time":1584724344196},
{"dis":0.20389369130134583,"latitude":44.9112458,"longitude":-93.3179804,"run_type":"jog","splitIndex":0,"stepIndex":5,"time":1584724355177},
{"dis":0.013633012771606445,"latitude":44.9112459,"longitude":-93.3179805,"run_type":"jog","splitIndex":0,"stepIndex":6,"time":1584724365045},
{"dis":0.04717438668012619,"latitude":44.9112455,"longitude":-93.3179807,"run_type":"jog","splitIndex":0,"stepIndex":7,"time":1584724374196},
{"dis":0.013633012771606445,"latitude":44.9112454,"longitude":-93.3179808,"run_type":"jog","splitIndex":0,"stepIndex":8,"time":1584724379198},
{"dis":0.07749170064926147,"latitude":44.9112448,"longitude":-93.3179813,"run_type":"jog","splitIndex":0,"stepIndex":9,"time":1584724394187},
{"dis":0.06714402139186859,"latitude":44.9112454,"longitude":-93.3179812,"run_type":"jog","splitIndex":0,"stepIndex":10,"time":1584724399207},
{"dis":0.07779102772474289,"latitude":44.9112461,"longitude":-93.3179812,"run_type":"jog","splitIndex":0,"stepIndex":11,"time":1584724404193},
{"dis":0.0078968470916152,"latitude":44.9112461,"longitude":-93.3179811,"run_type":"jog","splitIndex":0,"stepIndex":12,"time":1584724409188},
{"dis":0.026167547330260277,"latitude":44.911246,"longitude":-93.3179814,"run_type":"jog","splitIndex":0,"stepIndex":13,"time":1584724414186},
{"dis":0.12260224670171738,"latitude":44.9112453,"longitude":-93.3179826,"run_type":"jog","splitIndex":0,"stepIndex":14,"time":1584724429203},
{"dis":0.08203666657209396,"latitude":44.9112451,"longitude":-93.3179816,"run_type":"jog","splitIndex":0,"stepIndex":15,"time":1584724439219},
{"dis":0.1624220758676529,"latitude":44.9112461,"longitude":-93.3179801,"run_type":"jog","splitIndex":0,"stepIndex":16,"time":1584724449190},
{"dis":0.039484232664108276,"latitude":44.9112461,"longitude":-93.3179796,"run_type":"jog","splitIndex":0,"stepIndex":17,"time":1584724454187},
{"dis":0.013633012771606445,"latitude":44.9112462,"longitude":-93.3179795,"run_type":"jog","splitIndex":0,"stepIndex":18,"time":1584724459195},
{"dis":0.04531004652380943,"latitude":44.911246,"longitude":-93.31798,"run_type":"jog","splitIndex":0,"stepIndex":19,"time":1584724464186},
{"dis":0.034261494874954224,"latitude":44.9112457,"longitude":-93.3179801,"run_type":"jog","splitIndex":0,"stepIndex":20,"time":1584724469197}]}

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
