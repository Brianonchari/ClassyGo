package com.classygo.app.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.classygo.app.R
import com.classygo.app.model.PaymentMethodItem
import com.classygo.app.trip.RoundedBottomSheetDialogFragment
import com.classygo.app.utils.DefaultCallback
import kotlinx.android.synthetic.main.bottom_sheet_add_payment_method.*
import java.util.*


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
            val name = tiePaymentName.text.toString().trim()
            val pan = tiePaymentPan.text.toString().trim()
            val expiry = tieExpiry.text.toString().trim()
            val cvv = tieCVV.text.toString().trim()

            if (name.isEmpty() || pan.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
                Toast.makeText(activity, "Provide information and proceed", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            val paymentMethodItem = PaymentMethodItem(
                Calendar.getInstance().timeInMillis.toString(),
                name,
                "",
                pan,
                expiry,
                cvv
            )

            callback?.onActionPerformed(paymentMethodItem)
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