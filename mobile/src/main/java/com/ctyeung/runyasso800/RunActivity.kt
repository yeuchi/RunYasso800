package com.ctyeung.runyasso800

import android.content.Intent
import android.graphics.PointF
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

import androidx.lifecycle.Observer
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ctyeung.runyasso800.databinding.ActivityRunBinding
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.utilities.GPSTracker
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import com.ctyeung.runyasso800.viewModels.SplitViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

/*
 * Distance icon credit to Freepike from Flaticon
 * https://www.flaticon.com/free-icon/distance-to-travel-between-two-points_55212#term=distance&page=1&position=4
 */
class RunActivity : AppCompatActivity() {
    lateinit var binding:ActivityRunBinding
    lateinit var activity: RunActivity
    lateinit var splitViewModel:SplitViewModel
    lateinit var stepViewModel:StepViewModel
    lateinit var gps:GPSTracker
    val timer = Timer()

    var prevLatitude:Double = 0.0
    var prevLongitude:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run)

        activity = this

        prevLatitude = SharedPrefUtility.getLatitude(activity)
        prevLongitude = SharedPrefUtility.getLongitude(activity)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_run)
        binding?.listener = this

        gps = GPSTracker(this);
        getLocation()

        // data
        stepViewModel = ViewModelProvider(this).get(StepViewModel::class.java)
        // observe when total steps delta >= 800meter ?

        splitViewModel = ViewModelProvider(this).get(SplitViewModel::class.java)
        splitViewModel.yasso.observe(this, Observer { yasso ->
            // Update the cached copy of the words in the adapter.
            yasso?.let {
                // update here ..
            }
        })
        if (shouldAskPermissions())
            askPermissions()
    }

    override fun onStop() {
        super.onStop()

        SharedPrefUtility.setLatitude(activity, prevLatitude)
        SharedPrefUtility.setLongitude(activity, prevLongitude)
    }

    protected fun shouldAskPermissions(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

    protected fun askPermissions() {
        val permissions = arrayOf(
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.ACCESS_FINE_LOCATION"
        )
        val requestCode = 200
        requestPermissions(permissions, requestCode)
    }

    /*
 * User permission request -> result
 */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray)
    {
        for(permission:String in permissions)
        {
            // result:0 is ok
            val result = ContextCompat.checkSelfPermission(activity, permission)
            if (0!=result)
            {
                // not permitted to save or read -- !!! data-binding refactor

            }
        }
    }

    /*
     * If cleared, start GPS and Timer
     */
    fun onClickStart()
    {
        // 0. start GPS
        startTimer()

        // 1. record current time/location -> Sprint 0

        // 2. start timer event to detect 800 meters

        // 3. when Sprint 0 reach 800 meter, record time/location

        // 4. Inform user of Sprint completion

        // 5. record current time/location -> Jog 0

        // 6. start timer event to detect 800 meters

        // 7. when Jog 0 reach 800 meter, record time/location

        // 8. Inform user of Jog completion

        // 9. loop step 1 for next Sprint 1...9
    }

    fun getLocation()
    {
        // check if GPS enabled
        if(gps.canGetLocation())
        {
            val lat = gps.getLatitude()
            val long = gps.getLongitude()

            // insert db new data points & distance when user move more than unit of 1
            if(lat != prevLatitude || long != prevLongitude)
            {
                val dis = calculateDistance(prevLatitude, prevLongitude)

                // these values should be initialized from database -- not sharedPreference !!!!
                prevLatitude = lat.toDouble()
                prevLongitude = long.toDouble()

                val txtLat = findViewById<TextView>(R.id.txtLat)
                txtLat?.setText(prevLatitude.toString())

                val txtLong = findViewById<TextView>(R.id.txtLong)
                txtLong?.setText(prevLongitude.toString())

                val iteration = splitViewModel.yasso.value?.size ?: 0
                val index = stepViewModel.steps.value?.size ?: 0
                val step = Step(iteration, index, getRunType(iteration), System.currentTimeMillis(), prevLatitude, prevLongitude)
                stepViewModel.insert(step, dis)


                // query total distance
                /*
                 * if total distance >= 800meter -> next state
                 *  a. sprint, jog or done
                 */
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Can't get location", Toast.LENGTH_LONG).show()
        }
    }

    private fun getRunType(iteration:Int):String
    {
        if(0==iteration%2)
            return Split.RUN_TYPE_SPRINT

        else
            return Split.RUN_TYPE_JOG
    }

    fun calculateDistance(lat:Double, long:Double):Double
    {
        val dis = sqrt((lat - prevLatitude).pow(2) + (long - prevLongitude).pow(2))
        return dis
    }

    /*
     * If running, stop GPS and Timer,
     * persist data for result -> review
     */
    fun onClickStop()
    {
        timer?.cancel()

        /*
         * change state -> Interrupt
         */
    }

    /*
     * If Stopped, clear data
     */
    fun onClickClear()
    {
        timer?.cancel()

    }

    fun onClickDone()
    {
        timer?.cancel()

        val intent = Intent(this.applicationContext, ResultActivity::class.java)
        startActivity(intent)
    }

    fun startTimer()
    {
        /*
         * sampling rate - 200 ms
         */
        var milliseconds:Int = SharedPrefUtility.INTERVAL_MULTIPLY * SharedPrefUtility.getInterval(this.applicationContext)

        //Set the schedule function
        timer?.scheduleAtFixedRate(
            object : TimerTask() {

                override fun run() {
                    getLocation()
                }
            },
            0, milliseconds.toLong()
        )   // 1000 Millisecond  = 1 second
    }
}
