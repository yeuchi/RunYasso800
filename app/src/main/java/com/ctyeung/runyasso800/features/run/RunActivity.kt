package com.ctyeung.runyasso800.features.run

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ctyeung.runyasso800.BaseActivity
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.databinding.ActivityRunBinding
import com.ctyeung.runyasso800.features.run.stateMachine.RunStates

class RunActivity : BaseActivity() {
    lateinit var binding: ActivityRunBinding
    lateinit var stepViewModel:StepViewModel
    lateinit var splitViewModel: SplitViewModel
    val refresh : () -> Unit = {binding?.invalidateAll()}
    lateinit var activity: RunActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_run)
        binding.run = this
        activity = this

        stepViewModel = ViewModelProvider(this).get(StepViewModel::class.java)
        splitViewModel = ViewModelProvider(this).get(SplitViewModel::class.java)

        stepViewModel.steps.observe(this, Observer { steps ->
            steps?.let {
                onHandleLocationUpdate()
            }
        })

        splitViewModel.splits.observe(this, Observer { splits ->
            // Update the cached copy of the words in the adapter.
            splits?.let {
                onHandleLocationUpdate()
            }
        })

        if (shouldAskPermissions())
            askPermissions()
    }


    protected fun shouldAskPermissions(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

    @RequiresApi(Build.VERSION_CODES.M)
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

    // State machine callback -- data update
    fun onHandleLocationUpdate() {
        splitViewModel.updateData(refresh)
        refresh()
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

    }

    /*
     * ONLY when SPRINT or JOG state
     * - SPRINT / JOG -> goto PAUSE
     *
     * Pause everything (timer, distance measure, etc) ... let user cheat.
     */
    fun onClickPause()
    {

    }

    /*
     * Only when PAUSE state
     * -> goto CLEAR -> IDLE
     */
    fun onClickClear()
    {

    }

    /*
     * when DONE or PAUSE (quit early?)
     * -> goto next Activity
     */
    fun onClickNext()
    {

    }

    fun changeFabState(state: RunStates) {

        when (state) {
            is RunStates.Sprint,
            is RunStates.Jog,
            is RunStates.Resume -> {
                binding.apply {
                    btnPause.show()
                    btnStart.hide()
                    btnClear.hide()
                    btnNext.hide()
                }
            }

            is RunStates.Done,
            is RunStates.Pause -> {
                binding.apply {
                    btnStart.show()
                    btnClear.show()
                    btnNext.show()
                    btnPause.hide()
                }
            }

            is RunStates.Idle,
            is RunStates.Clear -> {
                binding.apply {
                    btnStart.show()
                    btnClear.hide()
                    btnNext.hide()
                    btnPause.hide()
                }
            }
        }
    }
}