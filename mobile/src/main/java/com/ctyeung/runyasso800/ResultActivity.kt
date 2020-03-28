package com.ctyeung.runyasso800

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.viewModels.SplitViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import androidx.databinding.DataBindingUtil
import com.ctyeung.runyasso800.databinding.ActivityResultBinding

/*
 * To do:
 * 1. custom markers - sprint/jog/index
 * 2. calculate and zoom to appropriate magnification
 * 3. add pop-up with split details (all sprint/jog or just selected individual?)
 * 4. screenshots and store as image files for persist-activity
 * 5. Use Dagger for models loading between states and activity ?
 * 6. support landscape and rotation
 * 7. check availability of map
 * 8. new viewModel with join entries
 *
 * Description:
 * Display performance result as follows
 * 1. Map path of sprints and jogs
 * 2. selectable sprint / jog detail in term of splits
 */
class ResultActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var binding:ActivityResultBinding
    private lateinit var activity: Activity
    private lateinit var mMap: GoogleMap
    lateinit var splitViewModel: SplitViewModel
    lateinit var stepViewModel: StepViewModel
    var lines:ArrayList<Polyline> = ArrayList<Polyline>()

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

        stepViewModel = ViewModelProvider(this).get(StepViewModel::class.java)
        splitViewModel = ViewModelProvider(this).get(SplitViewModel::class.java)

        stepViewModel.steps.observe(this, Observer { steps ->
            steps?.let {
                // 1st time only ?
                drawSteps()
            }
        })

        splitViewModel.yasso.observe(this, Observer { yasso ->
            // Update the cached copy of the words in the adapter.
            yasso?.let {
                // 1st time only ?
                drawSplitMarkers()
            }
        })
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
        mMap.setOnMarkerClickListener(this)
    }

    /*
     * Draw marker for each sprint and jog segment
     */
    private fun drawSplitMarkers() {
        val splits:List<Split>? = splitViewModel.yasso.value
        if(null!=splits) {

            for(split in splits){
                val s = LatLng(split.startLat, split.startLong)
                val name:String = split.run_type + split.splitIndex
                val markerOption = MarkerOptions()
                                    .position(s)
                                    .title(name)
                                    .icon(BitmapDescriptorFactory.defaultMarker(getIconColor(split.run_type)))
                mMap.addMarker(markerOption)
            }
        }
    }

    private fun getIconColor(runType:String):Float {
        when (runType) {
            Split.RUN_TYPE_JOG -> return BitmapDescriptorFactory.HUE_CYAN
            Split.RUN_TYPE_SPRINT -> return BitmapDescriptorFactory.HUE_GREEN
            else -> return BitmapDescriptorFactory.HUE_VIOLET
        }
    }

    /*
     * Need at least 2 steps to draw a line
     */
    private fun drawSteps() {
        lines.clear()
        val steps:List<Step>? = stepViewModel.steps.value

        if(null!=steps && steps.size > 1) {
            val max = steps.size -1
            for(i in 1..max) {

                val stt = LatLng(steps[i-1].latitude, steps[i-1].longitude)
                val end = LatLng(steps[i].latitude, steps[i].longitude)
                val line: Polyline = mMap.addPolyline(
                                            PolylineOptions()
                                                .add(stt, end)
                                                .width(5f)
                                                .color(Color.RED))
                lines.add(line)
            }
            val stt = LatLng(steps[0].latitude, steps[0].longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stt, 12.0f))

            if(lines.size>0)
                hasRendered = true
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return false
    }

    /*
     * Go to next activity
     */
    fun onClickNext() {
        gotoActivity (PersistActivity::class.java)
    }
}
