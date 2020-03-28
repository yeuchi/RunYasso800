package com.ctyeung.runyasso800.viewModels

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.RunActivity
import kotlinx.android.synthetic.main.activity_run.*
import java.lang.reflect.Type
import android.media.AudioManager
import android.media.ToneGenerator
import com.ctyeung.runyasso800.stateMachine.*
import com.ctyeung.runyasso800.utilities.TimeFormatter
import kotlin.math.roundToInt

/*
 * To do:
 * 1. use dagger for dependencies
 * 2. use hash to reduce code complexity
 */
class SplitContainer {
    private var run:RunActivity
    private var stateMachine:StateMachine
    private var stepViewModel:StepViewModel
    private var splitViewModel:SplitViewModel

    constructor(activity: RunActivity,
                stateMachine:StateMachine,
                stepViewModel: StepViewModel,
                splitViewModel: SplitViewModel) {
        this.run = activity
        this.stateMachine = stateMachine
        this.stepViewModel = stepViewModel
        this.splitViewModel = splitViewModel

        init()
    }

    fun init() {
        run.txtTotalSplits.text = "${MainApplication.applicationContext().resources.getString(R.string.total)}: ${SharedPrefUtility.getNumIterations()}"
    }

    fun updateData() {
        run.txtLat.text = stateMachine.prevLocation?.latitude.toString()
        run.txtLong.text = stateMachine.prevLocation?.longitude.toString()

        // distance in current split
        run.txtStepDistance.text = "Step: ${stepViewModel.totalDistance.roundToInt()}m";
        // distance total
        run.txtTotalDistance.text = "Total: ${splitViewModel.totalDistance.roundToInt()}m"

        // split index
        run.txtSplitIndex.text =  "Split: ${(splitViewModel.index+1)}"

        updateType()

        // split time (sprint or jog)
        run.txtSplitTime.text = "Time: ${TimeFormatter.printDateTime(stepViewModel.elapsedTime)}"
        run.txtTotalTime.text = "Total: ${TimeFormatter.printDateTime(splitViewModel.elapsedTime)}"
    }

    fun updateType() {
        /*
         * Use a hash here to reduce the code
         */
        val str = getString(stateMachine.current)
        run.txtSplitType.text = str
    }

    private fun getString(type:Type):String {
        var id = R.string.idle
        when(type) {
            StateIdle::class.java -> {/*default*/}
            StateJog::class.java,
            StateSprint::class.java -> {
                return splitViewModel.typeString
            }
            StatePause::class.java -> {id = R.string.pause}
            StateDone::class.java -> {id = R.string.done}
            else -> {id = R.string.dunno}
        }
        return MainApplication.applicationContext().resources.getString(id)
    }

    /*
     * update background, vibrate, beep
     */
    fun updateSupport() {
        val type = stateMachine.current
        updateBackgroundColor(type)
        vibrate(type)
        beep(type)
    }

    fun updateBackgroundColor(type:Type = stateMachine.current) {
        run.dataContainer.background = getViewBackground(type)
    }

    private fun getViewBackground(state: Type): Drawable {
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
        return MainApplication.applicationContext().resources.getDrawable(id)
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

        val vibrator = MainApplication.applicationContext().getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
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

}