package com.ctyeung.runyasso800
import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.PowerManager
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ctyeung.runyasso800.dagger.DaggerComponent
import com.ctyeung.runyasso800.databinding.ActivityRunBinding
import com.ctyeung.runyasso800.stateMachine.*
import com.ctyeung.runyasso800.utilities.LocationUpdateService
import com.ctyeung.runyasso800.viewModels.*
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Named


/*
 * TODO:
 *  1. support Landscape rotation
 *  2. check availability of GPS
 *  3. improve handling of 'Pause'
 *  4. persist state so we can leave and return to this activity when PAUSED.
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
    lateinit var runViewModel:RunViewModel
    lateinit var stepViewModel:StepViewModel
    lateinit var activity: RunActivity
    lateinit var wakeLock:PowerManager.WakeLock

    val refresh : () -> Unit = {binding?.invalidateAll()}

    companion object {
        var self:RunActivity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        self = this

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_run)
        binding.run = this
        activity = this

        stepViewModel = ViewModelProvider(this).get(StepViewModel::class.java)
        runViewModel = ViewModelProvider(this).get(RunViewModel::class.java)

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
            mService?.startStateMachine()

            if(isPermitted)
                mService?.requestLocationUpdates()

            mBound = true
            registerReceiver()
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

            val code = intent.extras.get(LocationUpdateService.EXTRA_UPDATE_CODE) as UpdateCode
            if(self!=null) {

                when (code) {
                    UpdateCode.CHANGE_SPLIT -> {
                        self!!.onChangedSplit()
                    }

                    UpdateCode.DONE -> {
                        self!!.onHandleYassoDone()
                    }

                    UpdateCode.LOCATION_UPDATE -> {
                        self!!.onHandleLocationUpdate()
                    }
                }
            }
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

    override fun onResume() {
        super.onResume()
        if(mServiceConnection != null && mBound == false) {
            var intent = Intent(this, LocationUpdateService.javaClass)
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (mBound) {
            unbindService(mServiceConnection)
        }
    }

    private fun registerReceiver() {
        myReceiver = MyReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            myReceiver!!,
            IntentFilter(LocationUpdateService.ACTION_BROADCAST)
        )
    }

    private fun unregisterReceiver() {
        if(myReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver!!)
            myReceiver = null
        }
    }

    override fun gotoActivity(classType:Class<*>) {
        RemoveLocation()
        unregisterReceiver()
        super.gotoActivity(classType)
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
        refresh()
        fab.changeState(StateDone::class.java)
    }

    // State machine callback -- background update, vibrate, beep
    override fun onChangedSplit() {
        splitContainer.updateSupport()
        runViewModel.updateType()
        refresh()
    }

    // State machine callback -- data update
    override fun onHandleLocationUpdate() {
        runViewModel.updateData(refresh)
        refresh()
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
        if(mService != null) {
            mService!!.requestLocationUpdates()

            when (runState) {
                StateIdle::class.java -> {
                    mService!!.runStart()
                    fab.changeState(StateResume::class.java)
                }

                StatePause::class.java -> {
                    mService!!.runPause()
                    fab.changeState(StateResume::class.java)
                }
                else -> toastErrorState(runState)
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
        if(mService != null) {

            // must be sprint / jog
            when (runState) {
                StateJog::class.java,
                StateSprint::class.java -> {
                    mService!!.runPause()
                    fab.changeState(StatePause::class.java)
                }
                else -> toastErrorState(runState)
            }
        }
    }

    /*
     * Only when PAUSE state
     * -> goto CLEAR -> IDLE
     */
    fun onClickClear()
    {
        if(mService != null) {

            // must be paused / error / done
            when (runState) {
                StateIdle::class.java,
                StatePause::class.java,
                StateError::class.java,
                StateDone::class.java -> {
                    mService!!.runClear()
                    fab.changeState(StateClear::class.java)
                    splitContainer.updateBackgroundColor()
                }

                else -> toastErrorState(runState)
            }
        }
    }

    /*
     * when DONE or PAUSE (quit early?)
     * -> goto next Activity
     */
    fun onClickNext()
    {
        when(runState){
            StateDone::class.java,
            StateIdle::class.java,
            StatePause::class.java -> {
                gotoActivity(ResultActivity::class.java)
            }

            else -> {}
        }
    }

    private val runState:Type
     get() {
        return SharedPrefUtility.get(SharedPrefUtility.keyRunState, StateError::class.java)
    }

    private fun toastErrorState(type:Type=StateError::class.java) {
        Toast.makeText(this, "ErrState:${type}", Toast.LENGTH_LONG).show()
    }
}
