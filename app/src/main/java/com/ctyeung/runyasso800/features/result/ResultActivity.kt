package com.ctyeung.runyasso800.features.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ctyeung.runyasso800.BaseActivity
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.databinding.ActivityResultBinding
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity : BaseActivity()  {
    lateinit var binding:ActivityResultBinding
//    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_result)
//        binding.res = this
    }

    /*
 * Initialize map
 */
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//        mMap.getUiSettings().setZoomControlsEnabled(true)
//    }

    fun onClickNext() {

    }

}