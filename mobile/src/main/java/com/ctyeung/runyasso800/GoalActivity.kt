package com.ctyeung.runyasso800

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.databinding.DataBindingUtil
import com.ctyeung.runyasso800.databinding.ActivityGoalBinding
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import android.widget.TimePicker
import android.app.TimePickerDialog
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.text.DateFormat
import java.util.*


class GoalActivity : AppCompatActivity() {
    lateinit var binding: ActivityGoalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_goal)
        binding?.listener = this
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

                // sprint goal
                val sprintGoal = findViewById<TextView>(R.id.txt_sprint_goal)
                sprintGoal.setText(hourOfDay.toString() + "minutes "+minute+"seconds")

            }, hour, minute, true
        )
        timePickerDialog.show()
    }
}
