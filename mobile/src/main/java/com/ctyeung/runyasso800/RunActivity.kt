package com.ctyeung.runyasso800

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

import androidx.databinding.DataBindingUtil
import com.ctyeung.runyasso800.databinding.ActivityRunBinding
import com.ctyeung.runyasso800.utilities.GPSTracker

/*
 * Distance icon credit to Freepike from Flaticon
 * https://www.flaticon.com/free-icon/distance-to-travel-between-two-points_55212#term=distance&page=1&position=4
 */
class RunActivity : AppCompatActivity() {
    lateinit var binding:ActivityRunBinding
    lateinit var gps:GPSTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_run)
        binding?.listener = this

        startGPS()
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

    fun startGPS()
    {
        gps = GPSTracker(this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            val latitude = gps.getLatitude()
            val txtLat = findViewById<TextView>(R.id.txtLat)
            txtLat?.setText(latitude.toString())

            val longitude = gps.getLongitude()
            val txtLong = findViewById<TextView>(R.id.txtLong)
            txtLong?.setText(longitude.toString())
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Can't get location", Toast.LENGTH_LONG).show()
        }
    }

    /*
     * If running, stop GPS and Timer,
     * persist data for result -> review
     */
    fun onClickStop()
    {

    }

    /*
     * If Stopped, clear data
     */
    fun onClickClear()
    {

    }

    fun onClickDone()
    {
        val intent = Intent(this.applicationContext, ResultActivity::class.java)
        startActivity(intent)
    }
}
