package com.ctyeung.runyasso800

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import androidx.databinding.DataBindingUtil
import com.ctyeung.runyasso800.databinding.ActivityConfigBinding
import kotlinx.android.synthetic.main.activity_config.*

/*
 * - Persist (share) data to facebook, email or drive
 * - option to delete entries in db
 */
class PersistActivity : AppCompatActivity() {
    lateinit var binding:ActivityConfigBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_config)
        binding?.listener = this
    }

    fun onClickDone()
    {
        // if name is ok in db

        val intent = Intent(this.applicationContext, TimeActivity::class.java)
        startActivity(intent)
    }
}
