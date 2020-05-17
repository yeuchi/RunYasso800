package com.ctyeung.runyasso800.viewModels

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.StrictMode
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.dagger.DaggerComponent
import com.ctyeung.runyasso800.room.YassoDatabase
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.splits.SplitRepository
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.room.steps.StepRepository
import com.ctyeung.runyasso800.utilities.TimeFormatter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_persist.*
import java.lang.StringBuilder
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Named

class PersistViewModel (application: Application) : AndroidViewModel(application){
    @Inject
    @field:Named("split") lateinit var splitRepos:SplitRepository
    @Inject
    @field:Named("step") lateinit var stepRepos:StepRepository

    var splits: LiveData<List<Split>>
    var steps: LiveData<List<Step>>

    var totalRunTime:Long = 0
    var totalJogTime:Long = 0
    var totalRunDis:Double = 0.0
    var totalJogDis:Double = 0.0

    init {
        DaggerComponent.create().injectPersistViewModel(this)
        splits = splitRepos.splits
        steps = stepRepos.steps
    }

    private fun getResource(id:Int):String {
        return MainApplication.applicationContext().resources.getString(id)
    }

    /*
     * print a summary of marathon training
     */
    fun buildYassoMsg(header:String, footer:String):String {
        var sb = StringBuilder()
        sb.appendln(header)
        sb.appendln()

        sb.appendln(getRaceGoal())
        sb.appendln()

        sb.appendln(getSprintGoal())
        sb.appendln()

        val str = getPerformance()
        sb.appendln("${getResource(R.string.total_sprint_dis)} ${totalRunDis}m time: ${TimeFormatter.printTime(totalRunTime/1000)}")
        sb.appendln("${getResource(R.string.total_jog_dis)} ${totalJogDis}m time: ${TimeFormatter.printTime(totalJogTime/1000)}")
        sb.appendln()

        sb.appendln(str)
        sb.appendln()

        sb.appendln(getSplits())
        sb.appendln()

        sb.append(getSteps())
        sb.appendln()

        sb.appendln(footer)
        return sb.toString()
    }

    fun getRaceGoal():String {
        val seconds:Long = SharedPrefUtility.get(SharedPrefUtility.keyRaceGoal, 0L)
        val str = TimeFormatter.printTime(seconds)
        return getResource(R.string.race_goal) + str
    }

    fun getSprintGoal():String {
        val seconds:Long = SharedPrefUtility.get(SharedPrefUtility.keySprintGoal, 0L)
        val str = TimeFormatter.printTime(seconds)
        return getResource(R.string.sprint_goal) + str
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
        val splits:List<Split>? = splits.value
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
        val splits:List<Split>? = splits.value
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
        val steps:List<Step>? = steps.value
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

    fun buildIntent(header:String, footer:String):Intent {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        //val uriRight = SharedPrefUtility.getImageUri(this.applicationContext)

       // val uris = ArrayList<Uri>()
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
        val name = SharedPrefUtility.get(SharedPrefUtility.keyName, MainApplication.applicationContext().resources.getString(R.string.run_yasso_800))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, name)

        // need to insert image in the middle ...
        emailIntent.putExtra(Intent.EXTRA_TEXT, buildYassoMsg(header, footer))

        // load image
        //     emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        return emailIntent
    }
}