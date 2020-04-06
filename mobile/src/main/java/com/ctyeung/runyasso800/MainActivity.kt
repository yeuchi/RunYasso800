package com.ctyeung.runyasso800

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ctyeung.runyasso800.databinding.ActivityMainBinding
import com.ctyeung.runyasso800.dialogs.NumberPickerFragment
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.utilities.LocationUtils
import com.ctyeung.runyasso800.viewModels.MainViewModel
import com.ctyeung.runyasso800.viewModels.YassoViewModel
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility

/*
 * To do:
 * 1. enable/disable selection base on data already entered.
 * 2. layout landscape mode ?
 * 3. create a separate IntentService (thread) to manage db access instead of Split/Step ViewModels ?
 * 4. unit tests for all
 * 5. refactor database tables; use join instead of duplicate time, lat/long
 *
 * https://www.verywellfit.com/how-to-do-yasso-800s-2911888
 *
 * 1. Take your marathon goal time in hours and minutes and convert this to minutes and seconds. For example, if your marathon goal is 3 hours and 10 minutes then convert that to 3 minutes and 10 seconds.
 *
 * 2. First, do an easy warm-up of 5 to 10 minutes jogging and a few warm-up exercises.
 *
 * 3. Next, try to run 800 meters (approximately 1/2 mile) at your converted time (3:10 in this example).
 *
 * 4. Recover after each 800 by jogging or walking for the same amount of time (again, 3:10 in this example).
 *
 * 5. Repeat #3 10 times: Start with three or four repetitions per workout in the first week.
 *
 * 6. Don't forget to cool down with 5 minutes of easy running or walking, followed by stretching.
 *
 * Description:
 * Main Activity
 * 1. sub-activities : goal, run, result, persist
 * 2. menu items configuration : sprint-distance      // default 800meter
 *                               jog-distance         // default 800meter
 *                               number of iteration  // default 10x (sprint/jog)
 *                               GPS sampling rate    // default 10 seconds
 *
 */
class MainActivity : AppCompatActivity(), NumberPickerFragment.OnDialogOKListener {
    lateinit var binding: ActivityMainBinding
    lateinit var model:MainViewModel

    private var dlg:NumberPickerFragment=NumberPickerFragment()


    override fun onNumberDialogOKClick(id: String, value: Int) {
        dlg.dismiss()

        when(id.toLowerCase()) {
            this.resources.getString(R.string.id_sprint) -> {
                model.setSprintDistance(value)
            }
            this.resources.getString(R.string.id_jog) -> {
                model.setJogDistance(value)
            }
            this.resources.getString(R.string.id_iteration) -> {
                model.setIterations(value)
            }
            this.resources.getString(R.string.id_gps) -> {
                model.setSampleRate(value.toLong())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPrefUtility.initDefaults()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.listener = this

        model = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tab, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean {

        var max:Int = Split.DEFAULT_SPLIT_DISTANCE.toInt()
        var min:Int = 0
        var value:Int
        var id:String
        var title:String

        when(item.toString()) {
            this.resources.getString(R.string.sprint_distance) -> {
                value = model.getSprintDistance()
                id= this.resources.getString(R.string.id_sprint)
                title = this.resources.getString(R.string.sprint_distance)
            }

            this.resources.getString(R.string.jog_distance) -> {
                value = model.getJogDistance()
                id= this.resources.getString(R.string.id_jog)
                title = this.resources.getString(R.string.jog_distance)
            }

            this.resources.getString(R.string.num_iterations) -> {
                value = model.getIterations()
                id= this.resources.getString(R.string.id_iteration)
                title = this.resources.getString(R.string.num_iterations)
                max = Split.DEFAULT_SPLIT_ITERATIONS
            }

            this.resources.getString(R.string.gps_sample_rate) -> {
                value = model.getSampleRate().toInt()
                id= this.resources.getString(R.string.id_gps)
                title = this.resources.getString(R.string.gps_sample_rate)
                max = LocationUtils.DEFAULT_SAMPLE_RATE.toInt()
                min = LocationUtils.MIN_SAMPLE_RATE.toInt()
            }
            else -> return false
        }
        dlg.setParams(this, id, min, max, value)
        dlg.show(getSupportFragmentManager(), title)
        return true
    }

    fun onClickGoal()
    {
        navigate2Activity(GoalActivity::class.java)
    }

    fun onClickRun()
    {
        if(RunActivity.isAvailable()) {
            navigate2Activity(RunActivity::class.java)
        }
    }

    fun onClickResult()
    {
        if(ResultActivity.isAvailable()) {
            navigate2Activity(ResultActivity::class.java)
        }
    }

    fun onClickPersist()
    {
        if(PersistActivity.isAvailable()) {
            navigate2Activity(PersistActivity::class.java)
        }
    }

    fun navigate2Activity(classType:Class<*>)
    {
        val intent = Intent(this.applicationContext, classType)
        startActivity(intent)
    }
}
