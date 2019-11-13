package com.ctyeung.runyasso800

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.databinding.DataBindingUtil
import com.ctyeung.runyasso800.databinding.ActivityPersistBinding
import com.ctyeung.runyasso800.utilities.SharedPrefUtility
import java.util.ArrayList

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

    fun onClickShare()
    {
        try {
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())

            val uriLeft = SharedPrefUtility.getImageUri(this.applicationContext)
            val uriRight = SharedPrefUtility.getImageUri(this.applicationContext)

            val uris = ArrayList<Uri>()
            uris.add(uriLeft)
            uris.add(uriRight)

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
            val header = "message header"
            val footer = "message footer"
            emailIntent.putExtra(Intent.EXTRA_TEXT, header + "\n\n" + footer)

            // load image
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)

            if (emailIntent.resolveActivity(this.applicationContext.getPackageManager()) != null) {
                val send_title = "some title"
                this.applicationContext.startActivity(Intent.createChooser(emailIntent, send_title))
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
