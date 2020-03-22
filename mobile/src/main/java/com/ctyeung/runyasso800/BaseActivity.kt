package com.ctyeung.runyasso800

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainApplication.lastSubActivity = this.javaClass
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.title = SharedPrefUtility.getName()
    }

    /*
     * Navigate to XXX Activity
     */
    fun gotoActivity(classType:Class<*>) {
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