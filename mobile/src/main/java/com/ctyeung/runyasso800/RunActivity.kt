package com.ctyeung.runyasso800

import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ctyeung.runyasso800.databinding.ActivityRunBinding
import com.ctyeung.runyasso800.stateMachine.*
import com.ctyeung.runyasso800.utilities.LocationUtils
import com.ctyeung.runyasso800.viewModels.*
import kotlinx.android.synthetic.main.activity_run.*
import java.lang.reflect.Type


/*
 * To do:
 * 1. Use Dagger for models loading between states and activity ?
 * 2. check availability of GPS
 *
 * GPS noise is a big problem and need to be address before this can ever be of value.
 * There is a Kalman filter in C example to try; linear regression is an alternative.
 *
 * Distance icon credit to Freepike from Flaticon
 * https://www.flaticon.com/free-icon/distance-to-travel-between-two-points_55212#term=distance&page=1&position=4
 *
 * tutorial on fusedLocationProviderClient
 * https://medium.com/@droidbyme/get-current-location-using-fusedlocationproviderclient-in-android-cb7ebf5ab88e
 *
 * Description:
 * User performs sprint, jog in this activity.
 * Collect performance data with GPS and persist in db.
 */
class RunActivity : BaseActivity(), IRunStatsCallBack {
    lateinit var binding:ActivityRunBinding
    lateinit var fab:RunFloatingActionButtons
    lateinit var stateMachine:StateMachine
    lateinit var splitViewModel:SplitViewModel
    lateinit var stepViewModel:StepViewModel
    lateinit var activity: RunActivity

    companion object : ICompanion {
        private var isDone:Boolean = false

        override fun isAvailable(): Boolean {
            return true
        }

        /*
         * Are we completed here ?
         */
        override fun isCompleted():Boolean {
            if(isDone)
                return true

            return false
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_run)
        binding.run = this
        activity = this

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
        isDone = false

        if (shouldAskPermissions())
            askPermissions()

        txtTotalSplits.text = "${resources.getString(R.string.total)}: ${SharedPrefUtility.getNumIterations()}"
        binding.invalidateAll()
    }

    override fun onHandleYassoDone() {
        isDone = true

        // this should be done in viewModel -- need to handle clear/Pause
        txtSplitType.text = resources.getString(R.string.done)
        binding.invalidateAll()

        fab.changeState(StateDone::class.java)
    }

    /*
     * let runner knows split is done, change sprint or jog
     */
    override fun onChangedSplit() {
        val type = stateMachine.current
        dataContainer.background = getViewBackground(type)
        vibrate(type)
        beep(type)
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

    /*
     * Vibrate when split change (sprint/jog/done)
     * - so runner knows without needing to look at screen
     */
    private fun vibrate(state:Type) {
        var duration:Long = 2000

        when(state) {
            StateSprint::class.java -> {
                duration = 1000
            }
            StateJog::class.java -> {
                duration = 500
            }
        }

        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(duration)
        }
    }

     /*
      * Beep when split change (sprint/jog/done)
      * - so runner knows without needing to look at screen
      */
    private fun beep(state:Type) {
        var tone:Int = ToneGenerator.TONE_CDMA_ABBR_ALERT
        var duration:Int = 3000

        when(state) {
            StateSprint::class.java -> {
                tone = ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD
                duration = 2000
            }
            StateJog::class.java -> {
                tone = ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_NORMAL
                duration = 1000
            }
        }
        val tg = ToneGenerator(AudioManager.STREAM_ALARM, 1000)
        tg.startTone(tone, duration)
    }

    private fun getViewBackground(state:Type):Drawable {
        var id:Int = R.drawable.round_corners
        when(state) {
            StateJog::class.java -> {
                id = R.drawable.jog_round_corners
            }
            StateSprint::class.java -> {
                id = R.drawable.sprint_round_corners
            }
            StatePause::class.java -> {
                id = R.drawable.pause_round_corners
            }
        }
        return resources.getDrawable(id)
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
        isDone = false
    }

    /*
     * Only when DONE state
     * -> goto next Activity
     */
    fun onClickNext()
    {
        if(StateDone::class.java == stateMachine.current)
            gotoActivity(ResultActivity::class.java)
    }
}
