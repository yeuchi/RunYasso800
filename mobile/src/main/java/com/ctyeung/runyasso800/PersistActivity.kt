package com.ctyeung.runyasso800

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import androidx.databinding.DataBindingUtil
import com.ctyeung.runyasso800.databinding.ActivityPersistBinding

/*
 * - Persist (share) data to facebook, email or drive
 * - option to delete entries in db
 */
class PersistActivity : AppCompatActivity() {
    lateinit var binding:ActivityPersistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_persist)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_persist)
        binding?.listener = this
    }

    fun onClickDone()
    {
        // if name is ok in db

        val intent = Intent(this.applicationContext, GoalActivity::class.java)
        startActivity(intent)
    }
}
