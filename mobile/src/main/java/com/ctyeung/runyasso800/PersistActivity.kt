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
import com.ctyeung.runyasso800.databinding.ActivityPersistBinding
import java.util.ArrayList

/*
 * - Persist (share) data to facebook, email or drive
 * - option to delete entries in db
 */
class PersistActivity : AppCompatActivity() {
    lateinit var binding:ActivityPersistBinding
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_persist)
        context = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_persist)
        binding?.listener = this
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
            val header = findViewById<EditText>(R.id.txtHeader)
            val footer = findViewById<EditText>(R.id.txtFooter)
            val msg = header.text.toString() + "\n\n" + footer.text
            emailIntent.putExtra(Intent.EXTRA_TEXT, msg)

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
