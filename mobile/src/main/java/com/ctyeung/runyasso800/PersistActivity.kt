package com.ctyeung.runyasso800

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ctyeung.runyasso800.databinding.ActivityPersistBinding
import com.ctyeung.runyasso800.viewModels.SplitViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel
import java.util.ArrayList
import androidx.lifecycle.Observer
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.steps.Step
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_persist.*
import java.lang.StringBuilder

/*
 * - Persist (share) data to facebook, email or drive
 * - option to delete entries in db
 */
class PersistActivity : AppCompatActivity() {
    lateinit var splitViewModel:SplitViewModel
    lateinit var stepViewModel:StepViewModel
    lateinit var binding:ActivityPersistBinding
    lateinit var context: Context
    var hasSteps:Boolean = false
    var hasSplits:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_persist)
        context = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_persist)
        binding?.listener = this

        stepViewModel = ViewModelProvider(this).get(StepViewModel::class.java)
        splitViewModel = ViewModelProvider(this).get(SplitViewModel::class.java)

        stepViewModel.steps.observe(this, Observer { steps ->
            steps?.let {
                hasSteps = true
            }
        })

        splitViewModel.yasso.observe(this, Observer { yasso ->
            // Update the cached copy of the words in the adapter.
            yasso?.let {
                hasSplits = true
            }
        })
    }

    /*
     * Should my data be in JSON ?
     */
    private fun buildYassoMsg():String {
        var sb = StringBuilder()
        sb.appendln(txtHeader.text)
        sb.appendln()

        sb.appendln(getSplits())
        sb.appendln()

        sb.append(getSteps())
        sb.appendln()

        sb.appendln(txtFooter.text)
        return sb.toString()
    }

    /*
     * Should I use Gson library ?
     */
    private fun getSplits():String {
        var sb = StringBuilder()
        val splits:List<Split>? = splitViewModel.yasso.value?:null
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

    private fun getSteps():String {
        var sb = StringBuilder()
        val steps:List<Step>? = stepViewModel.steps.value?:null
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

    fun onClickShare()
    {
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
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject")

            // need to insert image in the middle ...
            emailIntent.putExtra(Intent.EXTRA_TEXT, buildYassoMsg())

            // load image
       //     emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)

            if (emailIntent.resolveActivity(context.getPackageManager()) != null) {
                val send_title = "some title"
                context.startActivity(Intent.createChooser(emailIntent, send_title))
            }

        } catch (e: Exception) {
            val msg = "some failure msg"

            Toast.makeText(this,
                msg,
                Toast.LENGTH_SHORT
            ).show()

            Log.e(msg, e.message, e)
        }

    }
}
