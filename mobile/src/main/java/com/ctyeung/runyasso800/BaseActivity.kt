package com.ctyeung.runyasso800

import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility

open class BaseActivity : AppCompatActivity() {

    /*
     * MainActivity check if sub-activity is available to user
     */

    open fun isAvailable():Boolean {
        return false
    }

    open fun isCompleted():Boolean {
        return false
    }

    /*
     * Navigate to XXX Activity
     */
    fun gotoActivity(classType:Class<*>) {
        val intent = Intent(this.applicationContext, classType)
        startActivity(intent)
    }

    /*
     * Home button -> MainActivity
     */
    fun initActionBar() {
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.title = SharedPrefUtility.getName()
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