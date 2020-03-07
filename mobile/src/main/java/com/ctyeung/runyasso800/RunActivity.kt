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
import com.ctyeung.runyasso800.stateMachine.RunState
import com.ctyeung.runyasso800.stateMachine.StateJog
import com.ctyeung.runyasso800.stateMachine.StateMachine
import com.ctyeung.runyasso800.stateMachine.StateSprint
import com.ctyeung.runyasso800.utilities.LocationUtils
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import com.ctyeung.runyasso800.viewModels.RunFloatingActionButtons
import com.ctyeung.runyasso800.viewModels.SplitViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel
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
    lateinit var activity: RunActivity
    lateinit var splitViewModel:SplitViewModel
    lateinit var stepViewModel:StepViewModel
    lateinit var fab:RunFloatingActionButtons

    /*
     * callback from State machine
     * -> update view model and UI
     */
    override fun onHandleLocationUpdate(location:Location,
                                        SplitIndex:Int,
                                        StepIndex:Int) {

        txtLat.text = StateMachine.prevLocation?.latitude.toString()
        txtLong.text = StateMachine.prevLocation?.longitude.toString()


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

        // data
        stepViewModel = ViewModelProvider(this).get(StepViewModel::class.java)
        // observe when total steps delta >= 800meter ?

        splitViewModel = ViewModelProvider(this).get(SplitViewModel::class.java)
        initStateMachine()
/*
        splitViewModel.yasso.observe(this, Observer { yasso ->
            // Update the cached copy of the words in the adapter.
            yasso?.let {
                // database update success ..
            }
        })*/
        if (shouldAskPermissions())
            askPermissions()

        binding.invalidateAll()
    }

    private fun initStateMachine() {
        /*
         * Not sure this is a good idea ... want to decouple
         *  ... also potential lifecycle issues
         * use dependency injection -- dagger ?
         */
        StateSprint.actListener = this
        StateSprint.splitViewModel = splitViewModel
        StateSprint.stepViewModel = stepViewModel

        StateJog.actListener = this
        StateJog.splitViewModel = splitViewModel
        StateJog.stepViewModel = stepViewModel
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
        StateMachine.observe(this, this)
    }

    /*
     * ONLY when IDLE state
     * -> goto SPRINT state
     */
    fun onClickStart()
    {
        // must be in Idle
        if(RunState.Idle == StateMachine.getStateEnum()) {
            StateMachine.interruptStart()
            fab.changeState(RunState.Resume)
        }
    }

    /*
     * ONLY when SPRINT or JOG or PAUSE state
     * - SPRINT / JOG -> goto PAUSE
     * - PAUSE -> goto RESUME
     *
     * Pause everything (timer, distance measure, etc) ... let user cheat.
     * Resume back to sprint / jog
     */
    fun onClickPause()
    {
        // must be sprint / jog
        when(StateMachine.getStateEnum()){
            RunState.Jog,
            RunState.Sprint -> {
                StateMachine.interruptPause()
                fab.changeState(RunState.Pause)
            }
            RunState.Pause -> {
                StateMachine.interruptPause()
                fab.changeState(RunState.Resume)
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
        when(StateMachine.getStateEnum()){
            RunState.Pause,
            RunState.Error,
            RunState.Done -> {
                StateMachine.interruptClear()
                fab.changeState(RunState.Clear)
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
        if(StateMachine.getStateEnum()==RunState.Done)
            gotoNextActivity()
    }

    fun gotoNextActivity() {
        val intent = Intent(this.applicationContext, ResultActivity::class.java)
        startActivity(intent)
    }
}
