package com.ctyeung.runyasso800

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Toast

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ctyeung.runyasso800.viewModels.RunViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel
import androidx.lifecycle.Observer
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.utilities.TimeFormatter
import com.ctyeung.runyasso800.viewModels.PersistViewModel
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_persist.*
import java.lang.StringBuilder
import java.util.*
import com.ctyeung.runyasso800.databinding.ActivityPersistBinding

/*
 * To do:
 * 1. send image(s) from Result ?
 * 2. user can delete all data
 *
 * Description:
 * - Persist (share) data to facebook, email or drive
 * - option to delete entries in db
 */
class PersistActivity : BaseActivity() {
    lateinit var model:PersistViewModel
    lateinit var binding:ActivityPersistBinding

    companion object : ICompanion {
        private var hasSteps:Boolean = false
        private var hasSplits:Boolean = false
        private var hasSendEmail:Boolean = false
        // Check if images and data is available for sending
        override fun isAvailable(): Boolean {
            // need to check db for available data
            return true
        }

        override fun isCompleted():Boolean {
            if(hasSendEmail)
                return true

            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hasSendEmail = false

        binding = DataBindingUtil.setContentView(this, R.layout.activity_persist)
        binding.listener = this

        model = ViewModelProvider(this).get(PersistViewModel::class.java)

        model.steps.observe(this, Observer { steps ->
            steps?.let {
                if(steps.size>0)
                    hasSteps = true
            }
        })

        model.splits.observe(this, Observer { splits ->
            // Update the cached copy of the words in the adapter.
            splits?.let {
                if(splits.size>0)
                    hasSplits = true
            }
        })
    }

    /*
     * Return to MainActivity
     */
    fun onClickNext() {
        gotoActivity(MainActivity::class.java)
    }

    /*
     * Share summary via email, drive, facebook ,etc
     */
    fun onClickShare() {
        if (hasSplits && hasSteps) {
            sendEmail()
            return
        }
        Toast.makeText(this, resources.getString(R.string.no_models), Toast.LENGTH_LONG).show()
    }

    fun sendEmail() {
        try {
            val emailIntent = model.buildIntent(txtHeader.text.toString(), txtFooter.text.toString())

            if (emailIntent.resolveActivity(MainApplication.applicationContext().getPackageManager()) != null) {
                val send_title = "some title"
                startActivity(Intent.createChooser(emailIntent, send_title))
            }
            hasSendEmail = true
        }
        catch (e: Exception) {
            val msg = "some failure msg"

            Toast.makeText(this,
                msg,
                Toast.LENGTH_SHORT
            ).show()

            Log.e(msg, e.message, e)
        }
    }
}
