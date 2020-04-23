package com.ctyeung.runyasso800.dialogs

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.utilities.TimeFormatter
import kotlinx.android.synthetic.main.fragment_split_detail.*
import com.ctyeung.runyasso800.databinding.FragmentSplitDetailBinding
import com.ctyeung.runyasso800.viewModels.SplitDetailViewModel

public class SplitDetailFragment : DialogFragment {

    private var split:Split
    private lateinit var binding:FragmentSplitDetailBinding
    lateinit var model:SplitDetailViewModel

    constructor(split:Split) {
        this.split = split
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,
                                container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        model = ViewModelProvider(this).get(SplitDetailViewModel::class.java)
        model.render(split.splitIndex, split.run_type, (split.endTime-split.startTime)/1000, split.dis)
        binding = FragmentSplitDetailBinding.inflate(inflater, container, false)
        binding.detail = this
        //mRoot = inflater.inflate(R.layout.fragment_split_detail, container, false);
        return binding.root
    }

    fun onClickOk() {
        this.dismiss()
    }
}