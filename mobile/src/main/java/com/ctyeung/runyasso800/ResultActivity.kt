package com.ctyeung.runyasso800

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ctyeung.runyasso800.databinding.ActivityResultBinding

/*
 * Review results after run !
 * - fragment1 : show map with GPS co-ordinates
 * - fragment2 : show recyclerview of splits
 */
class ResultActivity : AppCompatActivity() {
    lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_result)
        binding?.listener = this
    }
}
