package com.ctyeung.runyasso800

import android.app.Activity
import android.graphics.*
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ctyeung.runyasso800.databinding.ActivityResultBinding
import com.ctyeung.runyasso800.dialogs.SplitDetailFragment
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.viewModels.RunViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


/*
 * To do:
 * 4. screenshots and store as image files for persist-activity
 * 5. Use Dagger for models loading between states and activity ?
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
    private lateinit var mMap: GoogleMap
    lateinit var runViewModel: RunViewModel
    lateinit var stepViewModel: StepViewModel
    var lines:ArrayList<Polyline> = ArrayList<Polyline>()
    var markerIndexes = HashMap<Marker, Int>()

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
        runViewModel = ViewModelProvider(this).get(RunViewModel::class.java)

        stepViewModel.steps.observe(this, Observer { steps ->
            steps?.let {
                // 1st time only ?
                drawSteps()
            }
        })

        runViewModel.splits.observe(this, Observer { splits ->
            // Update the cached copy of the words in the adapter.
            splits?.let {
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
    }

    /*
     * Draw marker for each sprint and jog segment
     */
    private fun drawSplitMarkers() {
        val FONT_SIZE:Float = 80f
        val splits:List<Split>? = runViewModel.splits.value
        if(null!=splits && splits.size>0) {
            var i = 0
            var builder:LatLngBounds.Builder = LatLngBounds.Builder()

            for(split in splits){
                val s = LatLng(split.startLat, split.startLong)
                builder.include(s)

                val id = getMarkerId(split.run_type)
                val bmp:Bitmap = drawTextToBitmap(id, FONT_SIZE, split.splitIndex)
                val markerOption = createMarker(s, bmp, split.run_type)
                val marker = mMap.addMarker(markerOption)
                markerIndexes.put(marker, i++)
            }
            zoomCamera(builder)
            mMap.setOnMarkerClickListener(this)
        }
    }

    private fun zoomCamera(builder:LatLngBounds.Builder) {
        val width = MainApplication.applicationContext().resources.displayMetrics.widthPixels
        val height = MainApplication.applicationContext().resources.displayMetrics.heightPixels
        val padding = (width * 0.05).toInt()
        val bounds = builder.build()
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
        mMap.animateCamera(cu)
    }

    private fun drawTextToBitmap(gResId:Int, textSize:Float, index: Int): Bitmap {
        val ICON_LEN:Int = 160
        val bitmap = getDrawable(gResId)!!.toBitmap(ICON_LEN, ICON_LEN)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = textSize
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.drawText(index.toString(), 60f, 90f, paint)

        return bitmap
    }

    private fun createMarker(s:LatLng, bmp:Bitmap?, runType: String):MarkerOptions {
        if(null!=bmp) {
            return MarkerOptions()
                    .position(s)
                    .icon(BitmapDescriptorFactory.fromBitmap(bmp))
        }
        else {
            return MarkerOptions()
                .position(s)
                .icon(BitmapDescriptorFactory.defaultMarker(getIconColor(runType)))
        }
    }

    private fun getIconColor(runType:String):Float {
        when (runType) {
            Split.RUN_TYPE_JOG -> return BitmapDescriptorFactory.HUE_CYAN
            Split.RUN_TYPE_SPRINT -> return BitmapDescriptorFactory.HUE_GREEN
            else -> return BitmapDescriptorFactory.HUE_VIOLET
        }
    }

    private fun getMarkerId(runType: String):Int {
        when (runType) {
            Split.RUN_TYPE_JOG -> return R.drawable.ic_place_cyan
            Split.RUN_TYPE_SPRINT -> return R.drawable.ic_place_green
            else -> return R.drawable.ic_place_purple
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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stt, 14.0f))

            if(lines.size>0)
                hasRendered = true
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        val INVALID_INDEX = -1
        val i:Int = markerIndexes[p0]?:INVALID_INDEX
        val splits: List<Split>? = runViewModel.splits.value

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
