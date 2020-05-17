package com.ctyeung.runyasso800.viewModels

import android.app.Application
import android.graphics.*
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
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
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

/*
 * Yasso Session data
 */
class ResultViewModel  (application: Application) : AndroidViewModel(application){
    @Inject
    @field:Named("split") lateinit var splitRepos:SplitRepository
    @Inject
    @field:Named("step") lateinit var stepRepos:StepRepository

    var splits: LiveData<List<Split>>
    var steps:LiveData<List<Step>>
    var markerIndexes = HashMap<Marker, Int>()
    var lines:ArrayList<Polyline> = ArrayList<Polyline>()

    init {
        DaggerComponent.create().injectResultViewModel(this)
        splits = splitRepos.splits
        steps = stepRepos.steps
    }

    /*
    * Draw marker for each sprint and jog segment
    */
    fun drawSplitMarkers(mMap: GoogleMap):CameraUpdate {
        val FONT_SIZE: Float = 80f
        val list: List<Split>? = splits.value
        var builder: LatLngBounds.Builder = LatLngBounds.Builder()

        if(null!=list && list.size>0) {
            val max = list.size - 1
            for (i in 0..max) {
                val split = list[i]
                val s = LatLng(split.startLat, split.startLong)
                builder.include(s)

                val id = getMarkerId(split.run_type, split.meetGoal)
                val bmp: Bitmap = drawTextToBitmap(id, FONT_SIZE, split.splitIndex)
                val markerOption = createMarker(s, bmp, split.run_type)
                val marker = mMap.addMarker(markerOption)
                markerIndexes.put(marker, i)
            }
        }
        return zoomCamera(builder)
    }

    private fun zoomCamera(builder:LatLngBounds.Builder):CameraUpdate {
        val width = MainApplication.applicationContext().resources.displayMetrics.widthPixels
        val height = MainApplication.applicationContext().resources.displayMetrics.heightPixels
        val padding = (width * 0.05).toInt()
        val bounds = builder.build()
        val cu:CameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
        return cu
    }

    private fun getMarkerId(runType: String, meetGoal:Boolean):Int {
        if(!meetGoal)
            return R.drawable.ic_place_red

        when (runType) {
            Split.RUN_TYPE_JOG -> return R.drawable.ic_place_cyan
            Split.RUN_TYPE_SPRINT -> { return R.drawable.ic_place_green }
            else -> return R.drawable.ic_place_purple
        }
    }

    private fun drawTextToBitmap(gResId:Int, textSize:Float, index: Int): Bitmap {
        val ICON_LEN:Int = 160
        val bitmap = MainApplication.applicationContext().resources.getDrawable(gResId)!!.toBitmap(ICON_LEN, ICON_LEN)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = textSize
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.drawText(index.toString(), 60f, 90f, paint)
        return bitmap
    }

    private fun createMarker(s:LatLng, bmp:Bitmap?, runType: String): MarkerOptions {
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

    /*
     * Need at least 2 steps to draw a line
     */
    fun drawSteps(mMap: GoogleMap) {
        lines.clear()
        val list: List<Step>? = steps.value
        if(null!=list && list.size>0) {
            val max = list.size - 1
            for (i in 1..max) {

                val stt = LatLng(list[i - 1].latitude, list[i - 1].longitude)
                val end = LatLng(list[i].latitude, list[i].longitude)
                val line: Polyline = mMap.addPolyline(
                    PolylineOptions()
                        .add(stt, end)
                        .width(5f)
                        .color(Color.RED)
                )
                lines.add(line)
            }
            val stt = LatLng(list[0].latitude, list[0].longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stt, 14.0f))
        }
    }
}