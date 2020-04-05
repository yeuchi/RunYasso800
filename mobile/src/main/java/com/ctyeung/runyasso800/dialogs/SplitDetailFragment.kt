package com.ctyeung.runyasso800.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
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
        model.txtDetailIndex.text = "Split: ${split.splitIndex}"
        model.txtDetailType.text = "Type: ${split.run_type}"
        val duration = split.endTime - split.startTime
        model.txtDetailTime.text = "Duration: ${TimeFormatter.printTime(duration)}"
        model.txtDetailDistance.text = "Distance: ${Math.round(split.dis)} m"
    }

    fun onClickOk() {
        this.dismiss()
    }
}