package com.ctyeung.runyasso800.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.ctyeung.runyasso800.databinding.FragmentAboutBinding
import com.ctyeung.runyasso800.viewModels.AboutViewModel

class AboutDialogFragment : DialogFragment() {

    lateinit var binding:FragmentAboutBinding
    lateinit var model:AboutViewModel
    private var listener:FactoryResetListener? = null

    interface FactoryResetListener{
        fun onFactoryReset()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setParams(listener:FactoryResetListener){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = ViewModelProvider(this).get(AboutViewModel::class.java)
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        binding.about = this
        return binding.root
    }

    fun onClickOk() {
        this.dismiss()
    }

    fun onClickFactoryReset() {
        model.factoryReset()
        this.dismiss()
        listener?.onFactoryReset()
    }
}