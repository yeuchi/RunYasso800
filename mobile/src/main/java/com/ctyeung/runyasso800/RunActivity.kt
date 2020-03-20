package com.ctyeung.runyasso800

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ctyeung.runyasso800.databinding.ActivityRunBinding
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.stateMachine.*
import com.ctyeung.runyasso800.utilities.LocationUtils
import com.ctyeung.runyasso800.viewModels.*
import kotlinx.android.synthetic.main.activity_run.*


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
class RunActivity : AppCompatActivity(), IRunStatsCallBack {
    lateinit var binding:ActivityRunBinding
    lateinit var fab:RunFloatingActionButtons
    lateinit var stateMachine:StateMachine
    lateinit var splitViewModel:SplitViewModel
    lateinit var stepViewModel:StepViewModel
    lateinit var activity: RunActivity

    override fun onHandleYassoDone() {
        fab.changeState(StateDone::class.java)
    }

    /*
     * callback from State machine
     * -> update view model and UI
     */
    override fun onHandleLocationUpdate() {

        txtLat.text = stateMachine.prevLocation?.latitude.toString()
        txtLong.text = stateMachine.prevLocation?.longitude.toString()

        // distance in current split
        txtStepDistance.text = stepViewModel.disTotalString
        // distance total
        txtTotalDistance.text = splitViewModel.disTotalString

        // split index
        txtSplitIndex.text = splitViewModel.indexString

        // split type (sprint or jog)
        txtSplitType.text = splitViewModel.typeString

        // split time (sprint or jog)
        txtSplitTime.text = stepViewModel.elapsedTimeString
        txtTotalTime.text = splitViewModel.elapsedTimeString

        binding.invalidateAll()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run)

        activity = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_run)
        binding.run = this

        fab = RunFloatingActionButtons(this)

        stepViewModel = ViewModelProvider(this).get(StepViewModel::class.java)
        splitViewModel = ViewModelProvider(this).get(SplitViewModel::class.java)
        stateMachine = StateMachine(this, splitViewModel, stepViewModel)

        stepViewModel.steps.observe(this, Observer { steps ->
            steps?.let {

            }
        })

        splitViewModel.yasso.observe(this, Observer { yasso ->
            // Update the cached copy of the words in the adapter.
            yasso?.let {
                // database update success ..
            }
        })

        // start with a clean slate
        stateMachine.interruptClear()

        if (shouldAskPermissions())
            askPermissions()

        txtTotalSplits.text = "Total: "+SharedPrefUtility.getNumIterations()
        binding.invalidateAll()
    }

    /*
     * Stop by Android; user kills app; error
     * - do we persist data ?
     */
    override fun onStop() {
        super.onStop()

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
        stateMachine.observe(this)
    }

    /*
     * when IDLE state
     * -> goto SPRINT state
     *
     * when PAUSE state
     * -> goto RESUME state
     */
    fun onClickStart()
    {
        when(stateMachine.current){
            StateIdle::class.java -> {
                stateMachine.interruptStart()
                fab.changeState(StateResume::class.java)
            }

            StatePause::class.java -> {
                stateMachine.interruptPause()
                fab.changeState(StateResume::class.java)
            }
        }
    }

    /*
     * ONLY when SPRINT or JOG state
     * - SPRINT / JOG -> goto PAUSE
     *
     * Pause everything (timer, distance measure, etc) ... let user cheat.
     */
    fun onClickPause()
    {
        // must be sprint / jog
        when(stateMachine.current){
            StateJog::class.java,
            StateSprint::class.java -> {
                stateMachine.interruptPause()
                fab.changeState(StatePause::class.java)
            }
            else -> {
                // error condition
            }
        }
    }

    /*
     * Only when PAUSE state
     * -> goto CLEAR -> IDLE
     */
    fun onClickClear()
    {
        // must be paused / error / done
        when(stateMachine.current){
            StateIdle::class.java,
            StatePause::class.java,
            StateError::class.java,
            StateDone::class.java -> {
                stateMachine.interruptClear()
                fab.changeState(StateClear::class.java)
            }
            else -> {
                // error condition
            }
        }
    }

    /*
     * Only when DONE state
     * -> goto next Activity
     */
    fun onClickNext()
    {
        if(StateDone::class.java == stateMachine.current)
            gotoNextActivity()
    }

    fun gotoNextActivity() {
        val intent = Intent(this.applicationContext, ResultActivity::class.java)
        startActivity(intent)
    }
}
