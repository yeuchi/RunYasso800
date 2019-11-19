package com.ctyeung.runyasso800

import android.content.Intent
import android.graphics.PointF
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat

import androidx.databinding.DataBindingUtil
import com.ctyeung.runyasso800.databinding.ActivityRunBinding
import com.ctyeung.runyasso800.utilities.GPSTracker
import com.ctyeung.runyasso800.utilities.SharedPrefUtility
import java.util.*
import java.util.jar.Manifest

/*
 * Distance icon credit to Freepike from Flaticon
 * https://www.flaticon.com/free-icon/distance-to-travel-between-two-points_55212#term=distance&page=1&position=4
 */
class RunActivity : AppCompatActivity() {
    lateinit var binding:ActivityRunBinding
    lateinit var gps:GPSTracker
    val timer = Timer()
    var origin:PointF = PointF(0F,0F)
    var last:PointF = PointF(0F,0F)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_run)
        binding?.listener = this

        gps = GPSTracker(this);
        getLocation()
        origin = last

        if (shouldAskPermissions())
            askPermissions()
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
     * If cleared, start GPS and Timer
     */
    fun onClickStart()
    {
        // 0. start GPS

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

            if(last.x != lat.toFloat() || last.y != long.toFloat())
            {
                val dis = calculateDistance()

                last.x = lat.toFloat()
                last.y = long.toFloat()

                val txtLat = findViewById<TextView>(R.id.txtLat)
                txtLat?.setText(last.x.toString())

                val txtLong = findViewById<TextView>(R.id.txtLong)
                txtLong?.setText(last.y.toString())

                // insert db new data points & distance

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

    fun calculateDistance()
    {

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
         * blink interval = duration of both colors
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
