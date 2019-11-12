# RunYasso800
"Yasso 800s are a popular workout among runners who are trying to achieve a specific marathon goal."<sup>[1]</sup>

# WORK-IN-PROGRESS

# Introduction 
This is a mobile phone implementation of Yasso800 in Kotlin for runners.  I intend to use this for my personal marathon training.  Hopefully, it is useful to others as well.

## Workflow
<img width="738" alt="workflow" src="https://user-images.githubusercontent.com/1282659/68631194-7790fd00-04af-11ea-9a5d-4ed80a8c9cd5.png">

<img src="https://user-images.githubusercontent.com/1282659/68713828-4b838380-0564-11ea-97c9-b645e0fe66f6.jpg" width="200"> 

### Goal (run time) Activity
Activity calculate the Yasso800 meter goal time in minutes and seconds.
The calculated value is from user's marathon time in hours and minutes.

<img src="https://user-images.githubusercontent.com/1282659/68713832-4e7e7400-0564-11ea-9cb4-7b75d55d30d3.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/68714362-5be82e00-0565-11ea-802d-7c3aa2ed0555.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/68714364-5e4a8800-0565-11ea-892d-ff842ad82eaf.jpg" width="200">

### Run Activity
Activity features support the running porting of Yasso800 as described below, "How to Do a Yasso 800 Workout".  They include:
- GPS location service
- track/location/timing of 800 meter x 10 runs
- track/location/timing of 800 meter x 10 jogs 

Note: Failure to meet the time goal at any point(s) will produce a red highlight.  The run <-> jog loop will continue until all 10X have been completed.  

<img width="495" alt="RunStateMachine" src="https://user-images.githubusercontent.com/1282659/68631191-74960c80-04af-11ea-8142-fe9e71b9d292.png">

<img src="https://user-images.githubusercontent.com/1282659/68713851-53dbbe80-0564-11ea-81be-0f3ccc34ae49.jpg" width="200"> 

#### State machine 
- IDLE - Activity intialization or CLEAR by user
- Run <-> Jog - loop for 10X .
- DONE - Yasso800 completed successfully.
- INTERRUP - STOP by user or ERROR conditions

### Result Activity
Activity presents run and jog results in map and sprint listing.

### Persist (Share) Activity
Activity provides persistence (share) methods to user.  They include the follow:
- Facebook - summary
- Email - xml file attachments
- Drive - xml files
- database management - delete 

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

