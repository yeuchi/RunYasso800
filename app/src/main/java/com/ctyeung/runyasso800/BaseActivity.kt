package com.ctyeung.runyasso800

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ctyeung.runyasso800.utilities.SharedPrefUtility
import com.ctyeung.runyasso800.utilities.TimeFormatter
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.R

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainApplication.lastSubActivity = this.javaClass
        initActionBar()
    }

    fun initActionBar() {
        getSupportActionBar()?.title = getActionBarTitle()

        if(this::class.java != MainActivity::class.java)
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
    }

    /*
     * Common code -- refactor !!!
     */
    fun getActionBarTitle():String {
        val name = SharedPrefUtility.get(SharedPrefUtility.keyName, MainApplication.applicationContext().resources.getString(R.string.run_yasso_800))
        val time:Long = SharedPrefUtility.get(SharedPrefUtility.keySprintGoal, 0L)

        if(time>0) {
            val goal = resources.getString(R.string.split_goal)
            return "${name}  ${goal} ${TimeFormatter.printTime(time)}"
        }
        return name
    }

    /*
     * Navigate to XXX Activity
     */
    open fun gotoActivity(classType:Class<*>) {
        val intent = Intent(this.applicationContext, classType)
        startActivity(intent)
    }

    /*
     * Action / menu bar selection - back button
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> gotoActivity(MainActivity::class.java)
        }
        return true
    }
}