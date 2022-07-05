package com.ctyeung.runyasso800.features.goals

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.ctyeung.runyasso800.BaseActivity
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.databinding.ActivityGoalBinding
import com.ctyeung.runyasso800.features.run.RunActivity
import com.ctyeung.runyasso800.storage.SharedPrefUtility

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
    }

    /*
    * Select Race Goal time in hours and minutes
    */
    fun onClickTime()
    {}

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