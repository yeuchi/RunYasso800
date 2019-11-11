package com.ctyeung.runyasso800

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.databinding.DataBindingUtil
import com.ctyeung.runyasso800.databinding.ActivityTimeBinding

class TimeActivity : AppCompatActivity() {
    lateinit var binding: ActivityTimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_time)
        binding?.listener = this
    }
}
