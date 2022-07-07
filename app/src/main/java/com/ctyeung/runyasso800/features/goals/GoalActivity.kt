package com.ctyeung.runyasso800.features.goals

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ctyeung.runyasso800.BaseActivity
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.databinding.ActivityGoalBinding
import com.ctyeung.runyasso800.features.run.RunActivity
import com.ctyeung.runyasso800.storage.SharedPrefUtility
import java.util.*

class GoalActivity : BaseActivity() {
    lateinit var binding:ActivityGoalBinding
    lateinit var model:GoalViewModel

    companion object : ICompanion {

        override fun isAvailable(): Boolean {
            return true
        }

        /*
         * Are we completed here ?
         */
        override fun isCompleted():Boolean {
            val goal = SharedPrefUtility.get(SharedPrefUtility.keyRaceGoal, 0L)
            if(goal > 0)
                return true

            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal)
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
        binding.txtRunName.addTextChangedListener(object : TextWatcher {

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