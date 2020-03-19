package com.ctyeung.runyasso800

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

class ResultActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var activity: Activity
    private lateinit var mMap: GoogleMap
    lateinit var splitViewModel: SplitViewModel
    lateinit var stepViewModel: StepViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
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
                // 1st time
                drawSteps()
            }
        })

        splitViewModel.yasso.observe(this, Observer { yasso ->
            // Update the cached copy of the words in the adapter.
            yasso?.let {
                // database update success ..
                drawSplitMarkers()
            }
        })
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
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

    override fun onMapReady(googleMap: GoogleMap) {
        /*
         * load markers from splits
         */
        mMap = googleMap
        mMap.getUiSettings().setZoomControlsEnabled(true)
        mMap.setOnMarkerClickListener(this)
    }

    /*
     * Draw marker for each sprint and jog segment
     */
    private fun drawSplitMarkers() {
        val splits:List<Split>? = splitViewModel.yasso.value?:null
        if(null!=splits) {

            for(split in splits){
                val s = LatLng(split.startLat, split.startLong)
                val name:String = split.run_type + split.splitIndex
                mMap.addMarker(MarkerOptions().position(s).title(name))
            }
        }
    }

    /*
     * Need at least 2 steps to draw a line
     */
    private fun drawSteps() {
        val steps:List<Step>? = stepViewModel.steps.value?:null

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
            }
            val stt = LatLng(steps[0].latitude, steps[0].longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stt, 12.0f))
        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return false
    }
}
