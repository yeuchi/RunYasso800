package com.ctyeung.runyasso800

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ctyeung.runyasso800.databinding.ActivityMainBinding
import com.ctyeung.runyasso800.dialogs.NumberPickerFragment
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import java.lang.reflect.Type


/*
 * Let me develop a Yasso 800 application for phone.
 * ... then try to get it into a watch next.
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
 * Features:
 *
 * a. Time
 * b. Map / Location - GPS
 * c. Save/Load results - xml files
 * d. Share data / Drive / email
 * e. Execution of Yasso800
 * f. Vibrate/Sound (voice recording, beep) for start/end/rest time
 */
class MainActivity : AppCompatActivity(), NumberPickerFragment.OnDialogOKListener {
    lateinit var binding: ActivityMainBinding
    private var dlg:NumberPickerFragment=NumberPickerFragment()


    override fun onNumberDialogOKClick(id: String, value: Int) {
        dlg.dismiss()

        var key:String = ""
        when(id.toLowerCase()) {
            "sprint" -> {
                SharedPrefUtility.setDistance(SharedPrefUtility.keySprintDis, value)
            }
            "jog" -> {
                SharedPrefUtility.setDistance(SharedPrefUtility.keyJogDis, value)
            }
            "iterations" -> {
                SharedPrefUtility.setNumIterations(value)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.listener = this
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tab, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean {

        var max:Int = Split.DEFAULT_SPLIT_DISTANCE.toInt()
        var value:Int = Split.DEFAULT_SPLIT_DISTANCE.toInt()
        var id:String = ""
        var title:String = ""
        when(item.toString()) {
            "Sprint Distance" -> {
                value = SharedPrefUtility.getDistance(SharedPrefUtility.keySprintDis)
                id="sprint"
                title ="Sprint Distance"
            }

            "Jog Distance" -> {
                value = SharedPrefUtility.getDistance(SharedPrefUtility.keyJogDis)
                id="jog"
                title = "Jog Distance"
            }

            "Num Iterations" -> {
                value = SharedPrefUtility.getNumIterations()
                id="iteration"
                title = "Num Iterations"
                max = 20
            }
            else -> return false
        }
        dlg.setParams(this, id, 0, max, value)
        dlg.show(getSupportFragmentManager(), title)
        return true
    }

    fun onClickTime()
    {
        navigate2Activity(GoalActivity::class.java)
    }

    fun onClickRun()
    {
        navigate2Activity(RunActivity::class.java)
    }

    fun onClickResult()
    {
        navigate2Activity(ResultActivity::class.java)
    }

    fun onClickPersist()
    {
        navigate2Activity(PersistActivity::class.java)
    }

    fun navigate2Activity(classType:Class<*>)
    {
        val intent = Intent(this.applicationContext, classType)
        startActivity(intent)
    }
}
