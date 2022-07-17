package com.ctyeung.runyasso800.features.persist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.databinding.ActivityPersistBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersistActivity : AppCompatActivity() {
    lateinit var binding:ActivityPersistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_persist)
    }

    fun onClickNext() {

    }

    fun onClickShare() {

    }
}