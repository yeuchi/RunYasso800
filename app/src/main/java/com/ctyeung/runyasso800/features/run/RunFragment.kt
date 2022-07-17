package com.ctyeung.runyasso800.features.run

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.data.room.splits.Split
import com.ctyeung.runyasso800.data.room.steps.Step
import com.ctyeung.runyasso800.databinding.FragmentRunBinding
import com.ctyeung.runyasso800.features.run.stateMachine.RunStates

class RunFragment : Fragment() {
    lateinit var binding: FragmentRunBinding
    private val viewModel by activityViewModels<RunViewModel>()
    val refresh: () -> Unit = { binding?.invalidateAll() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_run, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (shouldAskPermissions())
            askPermissions()

//        viewModel.steps.observe(viewLifecycleOwner, Observer(::onHandleStepUpdate))
//        viewModel.splits.observe(viewLifecycleOwner, Observer(::onHandleSplitsUpdate))
    }

    fun onHandleStepUpdate(steps: List<Step>) {
        updateDisplayMetrics()
    }

    fun onHandleSplitsUpdate(splits: List<Split>) {
        updateDisplayMetrics()
    }

    private fun updateDisplayMetrics() {
        binding.apply {
            txtLat.text = viewModel.txtLat
            txtLong.text = viewModel.txtLong
            txtSplitIndex.text = viewModel.txtSplitIndex
            txtTotalSplits.text = viewModel.txtTotalSplits
            txtSplitType.text = viewModel.txtSplitType
            txtSplitTime.text = viewModel.txtSplitTime
            txtTotalTime.text = viewModel.txtTotalTime
            txtStepDistance.text = viewModel.txtStepDistance
            txtTotalDistance.text = viewModel.txtTotalDistance
        }
    }

    fun shouldAskPermissions(): Boolean {
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

    var isPermitted: Boolean = false

    /*
     * User permission request -> result
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permission: String in permissions) {
            // result:0 is ok
            this.activity?.applicationContext?.apply {
                val result = ContextCompat.checkSelfPermission(this, permission)
                if (0 != result) {
                    // not permitted to save or read -- !!! data-binding refactor
                    return
                }
            }
        }
        isPermitted = true
    }

    // State machine callback -- data update
    fun onHandleLocationUpdate() {
//        splitViewModel.updateData(refresh)
        refresh()
    }

    /*
     * when IDLE state
     * -> goto SPRINT state
     *
     * when PAUSE state
     * -> goto RESUME state
     */
    fun onClickStart() {

    }

    /*
     * ONLY when SPRINT or JOG state
     * - SPRINT / JOG -> goto PAUSE
     *
     * Pause everything (timer, distance measure, etc) ... let user cheat.
     */
    fun onClickPause() {

    }

    /*
     * Only when PAUSE state
     * -> goto CLEAR -> IDLE
     */
    fun onClickClear() {

    }

    /*
     * when DONE or PAUSE (quit early?)
     * -> goto next Activity
     */
    fun onClickNext() {

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