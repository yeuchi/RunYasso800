package com.ctyeung.runyasso800.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.NumberPicker.OnValueChangeListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.ctyeung.runyasso800.R
import java.lang.reflect.Type

class NumberPickerFragment : DialogFragment() {
    private var valueChangeListener: OnValueChangeListener? = null
    private var mListener: INumberPickerListener? = null
    private var numberPicker: NumberPicker? = null
    private var id:String = "Jog"
    private var mMin = 20
    private var mMax = 60
    private var mValue = 40

    /*
     * parent call back listener
     */
    interface INumberPickerListener {
        fun onNumberDialogOKClick(id:String, value: Int)
    }

    private fun setNumberValues() {
        if (null != numberPicker) {
            numberPicker!!.minValue = mMin
            numberPicker!!.maxValue = mMax
            numberPicker!!.value = mValue
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity: FragmentActivity? = getActivity()
        val context = activity?.baseContext
        val ok = context?.resources?.getString(R.string.ok)
        val cancel = context?.resources?.getString(R.string.btn_cancel)
        val chooseValue = context?.resources?.getString(R.string.choose_number)
        numberPicker = NumberPicker(activity)
        setNumberValues()
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(chooseValue)
        builder.setPositiveButton(
            ok
        ) { dialog, which ->
            val value = numberPicker!!.value
            mListener!!.onNumberDialogOKClick(id, value)

            // valueChangeListener.onValueChange(numberPicker,
            //        numberPicker.getValue(), numberPicker.getValue());
        }
        builder.setNegativeButton(cancel) { dialog, which ->
            /*  valueChangeListener!!.onValueChange(
                  numberPicker,
                  numberPicker!!.value, numberPicker!!.value
              ) */
            dismiss()
        }
        builder.setView(numberPicker)
        return builder.create()
    }

    fun setParams(
        listener: INumberPickerListener?,
        id: String,
        min: Int,
        max: Int,
        value: Int
    ) {
        mListener = listener
        this.id = id
        mMin = min
        mMax = max
        mValue = value

        setNumberValues()
    }
}