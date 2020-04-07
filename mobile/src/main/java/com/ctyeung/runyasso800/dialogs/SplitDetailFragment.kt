package com.ctyeung.runyasso800.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.utilities.TimeFormatter
import kotlinx.android.synthetic.main.fragment_split_detail.*
import com.ctyeung.runyasso800.databinding.FragmentSplitDetailBinding

public class SplitDetailFragment : DialogFragment {

    private var split:Split
    private lateinit var binding:FragmentSplitDetailBinding

    constructor(split:Split) {
        this.split = split
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,
                                container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        binding = FragmentSplitDetailBinding.inflate(inflater, container, false)
        binding.detail = this
        //mRoot = inflater.inflate(R.layout.fragment_split_detail, container, false);
        initText(this.binding)
        return binding.root
    }

    fun initText(model:FragmentSplitDetailBinding) {
        model.txtDetailIndex.text = "${getResourceString(R.string.detail_split)} ${split.splitIndex}"
        model.txtDetailType.text = "${getResourceString(R.string.detail_type)} ${split.run_type}"
        val duration = (split.endTime - split.startTime)/1000
        model.txtDetailTime.text = "${getResourceString(R.string.detail_duration)}: ${TimeFormatter.printTime(duration)}"
        model.txtDetailDistance.text = "${getResourceString(R.string.detail_distance)}: ${Math.round(split.dis)} m"
    }

    fun onClickOk() {
        this.dismiss()
    }

    fun getResourceString(id:Int):String {
        return MainApplication.applicationContext().resources.getString(id)
    }
}