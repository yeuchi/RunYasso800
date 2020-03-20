package com.ctyeung.runyasso800

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.databinding.DataBindingUtil
import com.ctyeung.runyasso800.databinding.ActivityGoalBinding
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import android.widget.TimePicker
import android.app.TimePickerDialog
import android.content.Intent
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.ctyeung.runyasso800.utilities.LocationUtils
import com.ctyeung.runyasso800.utilities.TimeFormatter
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import kotlinx.android.synthetic.main.activity_goal.*
import java.text.DateFormat
import java.util.*


class GoalActivity : AppCompatActivity() {
    lateinit var binding: ActivityGoalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_goal)
        binding?.listener = this

        handleTextChange()
    }

    /*
     * Training Event name
     */
    fun handleTextChange() {
        txtRunName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable)
            {
               SharedPrefUtility.setName(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {}
        })
    }

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

                // sprint goal
                val sprintGoal = findViewById<TextView>(R.id.txt_sprint_goal)
                sprintGoal.setText(hourOfDay.toString() + "minutes "+minute+"seconds")
                val sprintMilliSec = TimeFormatter.convertHHmmss(0, hourOfDay, minute)
                SharedPrefUtility.setGoal(SharedPrefUtility.keySprintGoal, sprintMilliSec)

            }, hour, minute, true
        )
        timePickerDialog.show()
    }

    fun onClickDone()
    {
        val intent = Intent(this.applicationContext, RunActivity::class.java)
        startActivity(intent)
    }
}
