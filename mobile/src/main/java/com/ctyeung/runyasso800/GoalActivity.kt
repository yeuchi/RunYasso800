package com.ctyeung.runyasso800

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ctyeung.runyasso800.databinding.ActivityGoalBinding
import com.ctyeung.runyasso800.utilities.TimeFormatter
import com.ctyeung.runyasso800.viewModels.GoalViewModel
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import kotlinx.android.synthetic.main.activity_goal.*
import java.util.*

/*
 * To do:
 * 1. automatically navigate to run-activity ?
 * 2. refactor handlers into viewModel ?
 *
 * Description:
 * Activity for naming this training event and selecting a race goal time.
 * -> Calculate the target Sprint time
 * automatically go to RunActivity ? or let user review the sprint goal ?
 */
class GoalActivity : BaseActivity() {
    lateinit var binding: ActivityGoalBinding
    lateinit var model:GoalViewModel

    companion object : ICompanion {

        override fun isAvailable(): Boolean {
            return true
        }

        /*
         * Are we completed here ?
         */
        override fun isCompleted():Boolean {
            val goal = SharedPrefUtility.getGoal(SharedPrefUtility.keyRaceGoal)
            if(goal > 0)
                return true

            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_goal)
        binding.goal = this

        model = ViewModelProvider(this).get(GoalViewModel::class.java)
        model.setInitValues()
        handleTextChange()
    }

    /*
     * Training Event name
     */
    fun handleTextChange() {
        txtRunName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable)
            {
                var name:String? = s.toString()
                if(null==name || 0==name.length) {
                    // use default if invalid
                    name = resources.getString(R.string.run_yasso_800)
                }

                model.persistName(name!!)
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
                model.setRaceGoal(hourOfDay, minute)
                model.setSprintGoal(hourOfDay, minute)
                binding.invalidateAll()
                initActionBar()

                if(0==hourOfDay)
                    Toast.makeText(this, resources.getString(R.string.goal_unreal), Toast.LENGTH_LONG).show()

                // go to next activity automatically
                //if(hasName && hasRaceGoal)
                //    onClickNext()

            }, hour, minute, true
        )
        timePickerDialog.show()
    }

    /*
     * Next -> RunActivity
     */
    fun onClickNext()
    {
        if(isCompleted())
            gotoActivity(RunActivity::class.java)

        else
            Toast.makeText(this, resources.getString(R.string.missing_data), Toast.LENGTH_LONG).show()
    }
}
