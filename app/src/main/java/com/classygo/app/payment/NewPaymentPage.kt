package com.classygo.app.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.classygo.app.R
import com.classygo.app.trip.RoundedBottomSheetDialogFragment
import com.classygo.app.utils.DefaultCallback
import kotlinx.android.synthetic.main.bottom_sheet_add_payment_method.*


class NewPaymentPage : RoundedBottomSheetDialogFragment() {
    private var callback: DefaultCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_add_payment_method, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mbDone.setOnClickListener {

            callback?.onActionPerformed()
            dismiss()
        }
    }

    companion object {
        fun newInstance(callback: DefaultCallback) =
            NewPaymentPage().apply {
                this.callback = callback
            }
    }

}