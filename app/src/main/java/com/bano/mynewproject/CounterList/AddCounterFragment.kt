package com.bano.mynewproject.CounterList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bano.mynewproject.R
import com.bano.mynewproject.SalaryInformation

class AddCounterFragment : DialogFragment() {

    private var listener: OnCounterAddedListener? = null

    interface OnCounterAddedListener {
        fun onCounterAdded(salaryInformation: SalaryInformation, counter: CounterListItem)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_counter, container, false)

        val counterNameET: EditText = view.findViewById(R.id.counterNameET)
        val salaryAmountET: EditText = view.findViewById(R.id.salaryAmountET)
        val hoursDailyET: EditText = view.findViewById(R.id.hoursDailyET)
        val daysTotalET: EditText = view.findViewById(R.id.daysTotalET)
        val addButton: Button = view.findViewById(R.id.SaveButton)

        addButton.setOnClickListener {
            val counterName = counterNameET.text.toString()
            val salaryAmount = salaryAmountET.text.toString()
            val hoursDaily = hoursDailyET.text.toString()
            val daysTotal = daysTotalET.text.toString()

            if (counterName.isNotEmpty() && salaryAmount.isNotEmpty() && hoursDaily.isNotEmpty() && daysTotal.isNotEmpty()) {
                val salaryInfo =
                    SalaryInformation(salaryAmount.toInt(), hoursDaily.toInt(), daysTotal.toInt())
                val newCounter = CounterListItem(counterName, salaryInfo.toString())
                listener?.onCounterAdded(salaryInfo, newCounter)
                dismiss()
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.warningMissingInformation),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCounterAddedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnCounterAddedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


}