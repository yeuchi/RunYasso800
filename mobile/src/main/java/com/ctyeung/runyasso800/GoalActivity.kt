package com.ctyeung.runyasso800

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ctyeung.runyasso800.databinding.ActivityGoalBinding
import com.ctyeung.runyasso800.utilities.TimeFormatter
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import kotlinx.android.synthetic.main.activity_goal.*
import java.util.*

/*
 * To do:
 * 1. add home button -> MainActivity
 *
 * Description:
 * Activity for naming this training event and selecting a race goal time.
 * -> Calculate the target Sprint time
 * automatically go to RunActivity ? or let user review the sprint goal ?
 */
class GoalActivity : BaseActivity() {
    lateinit var binding: ActivityGoalBinding
    var hasName:Boolean = false
    var hasRaceGoal:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_goal)
        binding.listener = this

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
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
                getSupportActionBar()?.title = name

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
                val raceGoal = findViewById<Button>(R.id.btn_race_goal)
                raceGoal.setText(hourOfDay.toString() + "hours " + minute + "minutes")
                val raceMilliSec = TimeFormatter.convertHHmmss(hourOfDay, minute, 0)
                SharedPrefUtility.setGoal(SharedPrefUtility.keyRaceGoal, raceMilliSec)

                validateRaceGoal(hourOfDay, minute)

                // sprint goal
                val sprintGoal = findViewById<TextView>(R.id.txt_sprint_goal)
                sprintGoal.setText(hourOfDay.toString() + "minutes "+minute+"seconds")
                val sprintMilliSec = TimeFormatter.convertHHmmss(0, hourOfDay, minute)
                SharedPrefUtility.setGoal(SharedPrefUtility.keySprintGoal, sprintMilliSec)

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
    private fun validateRaceGoal(hours:Int, minutes:Int) {
        // no human can run marathon under an hour yet..
        if(hours > 0)
            hasRaceGoal = true
        else
            hasRaceGoal = false
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
