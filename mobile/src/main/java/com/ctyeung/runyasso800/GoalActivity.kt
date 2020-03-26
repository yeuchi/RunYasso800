package com.ctyeung.runyasso800

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.ctyeung.runyasso800.databinding.ActivityGoalBinding
import com.ctyeung.runyasso800.utilities.TimeFormatter
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import kotlinx.android.synthetic.main.activity_goal.*
import java.util.*

/*
 * To do:
 * 1. automatically navigate to run-activity ?
 * 2. Render Goal on action bar ?
 *
 * Description:
 * Activity for naming this training event and selecting a race goal time.
 * -> Calculate the target Sprint time
 * automatically go to RunActivity ? or let user review the sprint goal ?
 */
class GoalActivity : BaseActivity() {
    lateinit var binding: ActivityGoalBinding

    companion object : ICompanion {
        var hasName: Boolean = false
        var hasRaceGoal: Boolean = false

        override fun isAvailable(): Boolean {
            return true
        }

        /*
         * Are we completed here ?
         */
        override fun isCompleted():Boolean {
            if(hasName && hasRaceGoal)
                return true

            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_goal)
        binding.listener = this
        handleTextChange()
    }

    /*
     * Training Event name
     */
    fun handleTextChange() {
        txtRunName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable)
            {
                var name = s.toString()
                if(!validateName(name)) {
                    // use default if invalid
                    name = resources.getString(R.string.run_yasso_800)
                }

                SharedPrefUtility.setName(name)
                initActionBar()

                // go to next activity automatically
                //if (hasName && hasRaceGoal)
                //    onClickNext()
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    /*
     * Qualify name
     */
    private fun validateName(str:String?):Boolean {
        if(null!=str && str.length>0)
            hasName = true
        else
            hasName = false

        return hasName
    }

    /*
     * Select Race Goal time in hours and minutes
     */
    fun onClickTime()
    {
        // Get Current time
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this@GoalActivity,
            TimePickerDialog.OnTimeSetListener{ view, hourOfDay, minute ->

                // marathon goal
                val raceInSeconds = TimeFormatter.convertHHmmss(hourOfDay, minute, 0)
                btn_race_goal.text = TimeFormatter.printTime(raceInSeconds)
                SharedPrefUtility.setGoal(SharedPrefUtility.keyRaceGoal, raceInSeconds)

                // sprint goal
                val sprintInSeconds = TimeFormatter.convertHHmmss(0, hourOfDay, minute)
                txt_sprint_goal.text = TimeFormatter.printTime(sprintInSeconds)
                SharedPrefUtility.setGoal(SharedPrefUtility.keySprintGoal, sprintInSeconds)

                if(validateRaceGoal(hourOfDay, minute))
                    initActionBar()

                // go to next activity automatically
                //if(hasName && hasRaceGoal)
                //    onClickNext()

            }, hour, minute, true
        )
        timePickerDialog.show()
    }

    /*
     * Qualify race goal time
     */
    private fun validateRaceGoal(hours:Int, minutes:Int):Boolean {
        // no human can run marathon under an hour yet..
        if(hours > 0)
            hasRaceGoal = true
        else
            hasRaceGoal = false

        return hasRaceGoal
    }

    /*
     * Next -> RunActivity
     */
    fun onClickNext()
    {
        if(hasName && hasRaceGoal)
            gotoActivity(RunActivity::class.java)

        else
            Toast.makeText(this, resources.getString(R.string.missing_data), Toast.LENGTH_LONG).show()
    }
}
