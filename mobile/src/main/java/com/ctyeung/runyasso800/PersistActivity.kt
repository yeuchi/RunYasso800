package com.ctyeung.runyasso800

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Toast

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ctyeung.runyasso800.databinding.ActivityPersistBinding
import com.ctyeung.runyasso800.viewModels.RunViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel
import androidx.lifecycle.Observer
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.utilities.TimeFormatter
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_persist.*
import java.lang.StringBuilder
import java.util.*

/*
 * To do:
 * 1. send image(s) from Result ?
 * 2. user can delete all data
 * 3. Use Dagger for models loading between states and activity ?
 *
 * Description:
 * - Persist (share) data to facebook, email or drive
 * - option to delete entries in db
 */
class PersistActivity : BaseActivity() {
    lateinit var runViewModel:RunViewModel
    lateinit var stepViewModel:StepViewModel
    lateinit var binding:ActivityPersistBinding
    var totalRunTime:Long = 0
    var totalJogTime:Long = 0
    var totalRunDis:Double = 0.0
    var totalJogDis:Double = 0.0

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

        stepViewModel = ViewModelProvider(this).get(StepViewModel::class.java)
        runViewModel = ViewModelProvider(this).get(RunViewModel::class.java)

        stepViewModel.steps.observe(this, Observer { steps ->
            steps?.let {
                hasSteps = true
            }
        })

        runViewModel.splits.observe(this, Observer { splits ->
            // Update the cached copy of the words in the adapter.
            splits?.let {
                hasSplits = true
            }
        })
    }

    /*
     * print a summary of marathon training
     */
    private fun buildYassoMsg():String {
        var sb = StringBuilder()
        sb.appendln(txtHeader.text)
        sb.appendln()

        sb.appendln(getRaceGoal())
        sb.appendln()

        sb.appendln(getSprintGoal())
        sb.appendln()

        val str = getPerformance()
        sb.appendln("Total Run distance: ${totalRunDis}m time: ${TimeFormatter.printTime(totalRunTime/1000)}")
        sb.appendln("Total Jog distance: ${totalJogDis}m time: ${TimeFormatter.printTime(totalJogTime/1000)}")
        sb.appendln()

        sb.appendln(str)
        sb.appendln()

        sb.appendln(getSplits())
        sb.appendln()

        sb.append(getSteps())
        sb.appendln()

        sb.appendln(txtFooter.text)
        return sb.toString()
    }

    fun getRaceGoal():String {
        val seconds = SharedPrefUtility.getGoal(SharedPrefUtility.keyRaceGoal)
        val str = TimeFormatter.printTime(seconds)
        return resources.getString(R.string.race_goal) + str
    }

    fun getSprintGoal():String {
        val seconds = SharedPrefUtility.getGoal(SharedPrefUtility.keySprintGoal)
        val str = TimeFormatter.printTime(seconds)
        return resources.getString(R.string.sprint_goal) + str
    }

    private fun incrementTime(type:String, time:Long, dis:Double) {
        when(type) {
            Split.RUN_TYPE_SPRINT -> {
                totalRunTime += time
                totalRunDis += dis
            }

            Split.RUN_TYPE_JOG -> {
                totalJogTime += time
                totalJogDis += dis
            }
        }
    }

    /*
     * Build a summary of distances, time-elapsed on splits
     */
    private fun getPerformance():String {
        totalRunTime = 0
        totalJogTime = 0
        totalRunDis = 0.0
        totalJogDis = 0.0
        var sb = StringBuilder()
        val splits:List<Split>? = runViewModel.splits.value
        if(null!=splits) {
            sb.appendln("{\"Performance\":[")
            val size = splits.size
            var i = 0
            for (split in splits) {
                sb.append( "{\"split\": "+ split.splitIndex +", ")
                sb.append( "{\"type\": \""+ split.run_type +"\", ")
                sb.append("\"distance\":"+split.dis +", ")
                val timeDiff:Long = split.endTime - split.startTime
                incrementTime(split.run_type, timeDiff, split.dis)

                val str = TimeFormatter.printTime(timeDiff/1000)
                sb.append("\"elapsed\":\""+str+"\"}")

                if(++i >= size)
                    sb.appendln("]")
                else
                    sb.appendln(", ")
            }
        }
        return sb.toString()
    }

    /*
     * Raw split data dump
     */
    private fun getSplits():String {
        var sb = StringBuilder()
        val splits:List<Split>? = runViewModel.splits.value
        if(null!=splits) {
            sb.appendln("{\"Splits\":[")
            val size = splits.size
            var gson = Gson()
            var i = 0
            for (split in splits) {
                val str = gson.toJson(split)
                i++

                if(i >= size)
                    sb.appendln(str +"]")
                else
                    sb.appendln(str + ", ")
            }
        }
        return sb.toString()
    }

    /*
     * Raw step data dump
     */
    private fun getSteps():String {
        var sb = StringBuilder()
        val steps:List<Step>? = stepViewModel.steps.value
        if(null!=steps) {
            sb.appendln("{\"Steps\":[")
            var size = steps.size
            var gson = Gson()
            var i = 0
            for(step in steps) {
                val str = gson.toJson(step)
                i++
                if(i >= size)
                    sb.appendln(str +"]")
                else
                    sb.appendln(str + ", ")
            }
        }
        return sb.toString()
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
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())

            //val uriRight = SharedPrefUtility.getImageUri(this.applicationContext)

            val uris = ArrayList<Uri>()
         //   uris.add(uriRight)

            val emailIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            emailIntent.type = "image/*"

            /*
            if (null == mTuple) {
                Toast.makeText(
                    this,
                    "some failure message",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }*/

            // Subject
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, SharedPrefUtility.getName())

            // need to insert image in the middle ...
            emailIntent.putExtra(Intent.EXTRA_TEXT, buildYassoMsg())

            // load image
       //     emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)

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
