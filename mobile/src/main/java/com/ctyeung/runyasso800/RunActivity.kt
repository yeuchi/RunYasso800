package com.ctyeung.runyasso800
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ctyeung.runyasso800.databinding.ActivityRunBinding
import com.ctyeung.runyasso800.stateMachine.*
import com.ctyeung.runyasso800.utilities.LocationUtils
import com.ctyeung.runyasso800.viewModels.*


/*
 * To do:
 * 1. Use Dagger dependency injection !!!
 * 2. check availability of GPS
 * 3. refactor room split (no time, lat/long) and step
 * 4. persist state so we can leave and return to this activity.
 *
 * GPS noise is a big problem and need to be address before this can ever be of value.
 * There is a Kalman filter in C example to try; linear regression is an alternative.
 *
 * Distance icon credit to Freepike from Flaticon
 * https://www.flaticon.com/free-icon/distance-to-travel-between-two-points_55212#term=distance&page=1&position=4
 *
 * Description:
 * User performs sprint, jog in this activity.
 * Collect performance data with GPS and persist in db.
 */
class RunActivity : BaseActivity(), IRunStatsCallBack {
    lateinit var binding:ActivityRunBinding
    lateinit var fab:RunFloatingActionButtons
    lateinit var splitContainer:SplitContainer
    lateinit var stateMachine:StateMachine
    lateinit var runViewModel:RunViewModel
    lateinit var stepViewModel:StepViewModel
    lateinit var activity: RunActivity
    lateinit var wakeLock:PowerManager.WakeLock

    companion object : ICompanion {
        private var isDone:Boolean = false

        override fun isAvailable(): Boolean {
           /*
            val goal = SharedPrefUtility.getGoal(SharedPrefUtility.keySprintGoal)
            if(goal>0)
                return true

            return false
            */
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

        stepViewModel = ViewModelProvider(this).get(StepViewModel::class.java)
        runViewModel = ViewModelProvider(this).get(RunViewModel::class.java)
        stateMachine = StateMachine(this, runViewModel, stepViewModel)
        runViewModel.setStateMachine(stateMachine)

        fab = RunFloatingActionButtons(this, stateMachine)
        splitContainer = SplitContainer(this, stateMachine, stepViewModel, runViewModel)

        stepViewModel.steps.observe(this, Observer { steps ->
            steps?.let {
                onHandleLocationUpdate()
            }
        })

        runViewModel.splits.observe(this, Observer { splits ->
            // Update the cached copy of the words in the adapter.
            splits?.let {
                onHandleLocationUpdate()
            }
        })

        // start with a clean slate
        stateMachine.interruptClear()
        isDone = false

        initWakeLock()

        if (shouldAskPermissions())
            askPermissions()
    }

    /*
     * wake lock to keep activity from sleeping.
     */
    fun initWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "myapp:MyWakelockTag")
    }

    fun releaseWakeLock() {
        if(wakeLock.isHeld)
            wakeLock.release()
    }

    // State machine callback
    override fun onHandleYassoDone() {
        isDone = true
        runViewModel.updateType()
        binding.invalidateAll()
        fab.changeState(StateDone::class.java)
        releaseWakeLock()
    }

    // State machine callback -- background update, vibrate, beep
    override fun onChangedSplit() {
        splitContainer.updateSupport()
        runViewModel.updateType()
        binding.invalidateAll()
    }

    // State machine callback -- data update
    override fun onHandleLocationUpdate() {
        runViewModel.updateData()
        binding.invalidateAll()
    }

    /*
     * Stop by Android; user kills app; error
     * - do we persist data ?
     */
    override fun onStop() {
        super.onStop()
    }

    protected fun shouldAskPermissions(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

    protected fun askPermissions() {
        val permissions = arrayOf(
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.WAKE_LOCK"
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
        wakeLock.acquire()
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
            else -> {}
        }
        releaseWakeLock()
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
                splitContainer.updateBackgroundColor()
            }
            else -> {
                // error condition
            }
        }
        isDone = false
    }

    /*
     * when DONE or PAUSE (quit early?)
     * -> goto next Activity
     */
    fun onClickNext()
    {
        when(stateMachine.current){
            StateDone::class.java,
            StatePause::class.java -> {
                gotoActivity(ResultActivity::class.java)
            }
        }
    }
}
