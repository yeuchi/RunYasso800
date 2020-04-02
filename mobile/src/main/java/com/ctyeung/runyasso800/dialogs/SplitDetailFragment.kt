package com.ctyeung.runyasso800.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ctyeung.runyasso800.R

public class SplitDetailFragment : DialogFragment {

    private var mRoot: View? = null
    private var listener:IDialogListener

    constructor(listener: IDialogListener) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRoot = inflater.inflate(R.layout.fragment_split_detail, container, false);
        return mRoot
       // return super.onCreateView(inflater, container, savedInstanceState)
    }
}