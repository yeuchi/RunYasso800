package com.ctyeung.runyasso800
import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.PowerManager
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ctyeung.runyasso800.databinding.ActivityRunBinding
import com.ctyeung.runyasso800.stateMachine.*
import com.ctyeung.runyasso800.utilities.LocationUpdateService
import com.ctyeung.runyasso800.viewModels.*


/*
 * To do:
 * 1. Use Dagger dependency injection !!!
 * 2. check availability of GPS
 * 3. refactor room split (no time, lat/long) and step
 * 4. persist state so we can leave and return to this activity when PAUSED.
 *
 * Distance icon credit to Freepike from Flaticon
 * https://www.flaticon.com/free-icon/distance-to-travel-between-two-points_55212#term=distance&page=1&position=4
 *
 * Description:
 * User performs sprint, jog in this activity.
 * Collect performance data with GPS and persist in db.
 */
class RunActivity : BaseActivity(),IRunStatsCallBack {
    lateinit var binding:ActivityRunBinding
    lateinit var fab:RunFloatingActionButtons
    lateinit var splitContainer:SplitContainer
    lateinit var runViewModel:RunViewModel
    lateinit var stepViewModel:StepViewModel
    lateinit var activity: RunActivity
    lateinit var wakeLock:PowerManager.WakeLock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_run)
        binding.run = this
        activity = this

        stepViewModel = ViewModelProvider(this).get(StepViewModel::class.java)
        runViewModel = ViewModelProvider(this).get(RunViewModel::class.java)
        StateMachine.initialize(this)

        fab = RunFloatingActionButtons(this)
        splitContainer = SplitContainer(this, stepViewModel, runViewModel)

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
        StateMachine.interruptClear()

        if (shouldAskPermissions())
            askPermissions()
    }

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private var myReceiver: MyReceiver? = null

    // A reference to the service used to get location updates.
    private var mService: LocationUpdateService? = null

    // Tracks the bound state of the service.
    private var mBound = false

    // Monitors the state of the connection to the service.
    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder: LocationUpdateService.LocalBinder = service as LocationUpdateService.LocalBinder
            mService = binder.getService()

            if(isPermitted)
                mService!!.requestLocationUpdates()

            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            mBound = false
        }
    }

    /**
     * Receiver for broadcasts sent by [LocationUpdatesService].
     */
    private class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

                // receive message here !
        }
    }

    override fun onStart() {
        super.onStart()
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "myapp:MyWakelockTag")

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(
            Intent(this, LocationUpdateService::class.java), mServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    /*
     * To DO:
     * Return message from LocationService StateMachine
     */
    override fun onResume() {
        super.onResume()
        myReceiver = MyReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            myReceiver!!,
            IntentFilter(LocationUpdateService.ACTION_BROADCAST)
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver!!)
        super.onPause()
    }

    override fun onStop() {
        super.onStop()

        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection)
            mBound = false
        }
    }

    private fun RemoveLocation() {
        if(mService!=null)
            mService!!.removeLocationUpdates()

        if(wakeLock.isHeld)
            wakeLock.release()
    }

    /*
     * Need to Change this to use Intent and Receiver
     * Use Coroutine to execute in viewModel directly.
     */

    // State machine callback
    override fun onHandleYassoDone() {
        runViewModel.updateType()
        binding.invalidateAll()
        fab.changeState(StateDone::class.java)
        RemoveLocation()
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

    var isPermitted:Boolean = false
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
        isPermitted = true
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
        wakeLock.acquire()
        if(mService != null)
            mService!!.requestLocationUpdates()

        when(StateMachine.current){
            StateIdle::class.java -> {
                StateMachine.interruptStart()
                fab.changeState(StateResume::class.java)
            }

            StatePause::class.java -> {
                StateMachine.interruptPause()
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
        when(StateMachine.current){
            StateJog::class.java,
            StateSprint::class.java -> {
                StateMachine.interruptPause()
                fab.changeState(StatePause::class.java)
            }
            else -> {}
        }
        RemoveLocation()
    }

    /*
     * Only when PAUSE state
     * -> goto CLEAR -> IDLE
     */
    fun onClickClear()
    {
        // must be paused / error / done
        when(StateMachine.current){
            StateIdle::class.java,
            StatePause::class.java,
            StateError::class.java,
            StateDone::class.java -> {
                StateMachine.interruptClear()
                fab.changeState(StateClear::class.java)
                splitContainer.updateBackgroundColor()
            }
            else -> {
                // error condition
            }
        }
    }

    /*
     * when DONE or PAUSE (quit early?)
     * -> goto next Activity
     */
    fun onClickNext()
    {
        when(StateMachine.current){
            StateDone::class.java,
            StatePause::class.java -> {
                gotoActivity(ResultActivity::class.java)
            }
        }
    }
}
