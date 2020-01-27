package com.ctyeung.runyasso800

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ctyeung.runyasso800.databinding.ActivityRunBinding
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.utilities.LocationUtils
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import com.ctyeung.runyasso800.viewModels.SplitViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel


/*
 * GPS noise is a big problem and need to be address before this can ever be of value.
 * There is a Kalman filter in C example to try; linear regression is an alternative.
 *
 * Distance icon credit to Freepike from Flaticon
 * https://www.flaticon.com/free-icon/distance-to-travel-between-two-points_55212#term=distance&page=1&position=4
 *
 * tutorial on fusedLocationProviderClient
 * https://medium.com/@droidbyme/get-current-location-using-fusedlocationproviderclient-in-android-cb7ebf5ab88e
 * */
class RunActivity : AppCompatActivity() {
    lateinit var binding:ActivityRunBinding
    lateinit var activity: RunActivity
    lateinit var splitViewModel:SplitViewModel
    lateinit var stepViewModel:StepViewModel
    var prevLocation:Location ?= null
    var stateMachine:RunStateMachine = RunStateMachine()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run)

        activity = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_run)
        binding?.listener = this

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

        /*
         * No need to save.  User must restart from scratch.
         */

       // SharedPrefUtility.setLatitude(activity, prevLocation.latitude)
       // SharedPrefUtility.setLongitude(activity, prevLocation.longitude)
    }

    protected fun shouldAskPermissions(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

    protected fun askPermissions() {
        val permissions = arrayOf(
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"
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
                return
            }
        }
        startLocationService()
    }

    fun startLocationService()
    {
        LocationUtils.getInstance(activity)
        LocationUtils.getLocation().observe(this, Observer {location ->
            location?.let{
                // Yay! location recived. Do location related work here
                Log.i("RunActivity","Location: ${location.latitude}  ${location.longitude}")
                getLocation(location)
            }
        })
    }

    /*
     * If cleared, start GPS and Timer
     */
    fun onClickStart()
    {
        // must be in Idle
        if(RunState.Idle == stateMachine.state) {
            stateMachine.changeState(RunState.Resume)
        }

    }

    fun getLocation(location: Location)
    {
        if(null==prevLocation)
        {
            prevLocation = location
            return
        }

        // insert db new data points & distance when user move more than unit of 1
        val dis:Float = location?.distanceTo(prevLocation)?:0F
        if(dis>0)
        {
            // these values should be initialized from database -- not sharedPreference !!!!
            prevLocation = location?:prevLocation // will never use prev

            val txtLat = findViewById<TextView>(R.id.txtLat)
            txtLat?.setText(prevLocation?.latitude.toString())

            val txtLong = findViewById<TextView>(R.id.txtLong)
            txtLong?.setText(prevLocation?.longitude.toString())

            val iteration = splitViewModel.yasso.value?.size ?: 0
            val index = stepViewModel.steps.value?.size ?: 0
            val latitude:Double = prevLocation?.latitude ?: 0.0
            val longitude:Double = prevLocation?.longitude ?: 0.0
            val step = Step(iteration, index, getRunType(iteration), System.currentTimeMillis(), latitude, longitude)
            stepViewModel.insert(step, dis.toDouble())


            // query total distance
            /*
             * if total distance >= 800meter -> next state
             *  a. sprint, jog or done
             */
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



    /*
     * Pause everything (timer, distance measure, etc) ... let user cheat.
     */
    fun onClickPause()
    {
        // must be sprint / jog
    }

    /*
     * If Stopped, clear data
     */
    fun onClickClear()
    {
        // must be paused / error / done
    }

    fun onClickNext()
    {
        when(stateMachine.state) {
            RunState.Done -> gotoNextActivity()
            RunState.Pause -> gotoNextActivity()
        }
    }

    fun gotoNextActivity() {
        val intent = Intent(this.applicationContext, ResultActivity::class.java)
        startActivity(intent)
    }
}
