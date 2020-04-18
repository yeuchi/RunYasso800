package com.ctyeung.runyasso800

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ctyeung.runyasso800.databinding.ActivityResultBinding
import com.ctyeung.runyasso800.dialogs.SplitDetailFragment
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.viewModels.ResultViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.lang.Exception


/*
 * To do:
 * 4. screenshots and store as image files for persist-activity
 * 7. check availability of map
 * 8. new viewModel with join entries
 *
 * Description:
 * Display performance result as follows
 * 1. Map path of sprints and jogs
 * 2. selectable sprint / jog detail in term of splits
 */
class ResultActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private var dlg:SplitDetailFragment? = null
    private lateinit var binding:ActivityResultBinding
    private lateinit var activity: Activity
    lateinit var model: ResultViewModel
    private lateinit var mMap: GoogleMap

    companion object : ICompanion {
        private var hasRendered:Boolean = false
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        override fun isAvailable(): Boolean {
            // need to check db for available data
            return true
        }

        /*
         * Are we completed rendering here ?
         */
        override fun isCompleted():Boolean {
            if(hasRendered)
                return true

            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hasRendered = false
        binding = DataBindingUtil.setContentView(this, R.layout.activity_result)
        binding.res = this

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        activity = this

        if(shouldAskPermissions())
            askPermissions()

        model = ViewModelProvider(this).get(ResultViewModel::class.java)

        model.steps.observe(this, Observer { steps ->
            steps?.let {
                // 1st time only ?
                if(steps.size > 1) {
                    try {
                        model.drawSteps(mMap)
                        if (model.lines.size > 0)
                            hasRendered = true
                    }
                    catch (ex:Exception){
                        displayErrorMsg(ex.toString())
                    }
                }
            }
        })

        model.splits.observe(this, Observer { splits ->
            // Update the cached copy of the words in the adapter.
            splits?.let {
                // 1st time only ?
                if(splits.size>0) {
                    try {
                        val cu = model.drawSplitMarkers(mMap)
                        mMap.animateCamera(cu)
                        mMap.setOnMarkerClickListener(this)
                    }
                    catch (ex:Exception){
                        displayErrorMsg(ex.toString())
                    }
                }
            }
        })
    }

    fun displayErrorMsg(str:String) {
        Toast.makeText(this.getApplication(), str, Toast.LENGTH_LONG).show()
    }

    protected fun shouldAskPermissions(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

    protected fun askPermissions() {
        val permissions = arrayOf(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"
        )
        val requestCode = 200
        requestPermissions(permissions, requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray)
    {
        for(permission:String in permissions)
        {
            // result:0 is ok
            val result = ContextCompat.checkSelfPermission(activity, permission)
            if (0!=result)
            {
                // not permitted to save or read -- !!! data-binding refactor
                return
            }
        }
    }

    /*
     * Initialize map
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.getUiSettings().setZoomControlsEnabled(true)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        val INVALID_INDEX = -1
        val i:Int = model.markerIndexes[p0]?:INVALID_INDEX
        val splits: List<Split>? = model.splits.value

        if(i>INVALID_INDEX && null!=splits) {
            val split = splits[i]
            dlg = SplitDetailFragment(split)
            dlg?.show(supportFragmentManager, "Details")
            return true
        }
        return false
    }

    /*
     * Go to next activity
     */
    fun onClickNext() {
        gotoActivity (PersistActivity::class.java)
    }
}
