package com.classygo.app.trip

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.classygo.app.R
import com.classygo.app.model.Trip
import com.classygo.app.utils.DefaultCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import khronos.toString
import kotlinx.android.synthetic.main.bottom_sheet_trip_created.*
import javax.security.auth.callback.CallbackHandler


class BottomSuccessPage : RoundedBottomSheetDialogFragment() {
    private var trip: Trip? = null
    private var callback: DefaultCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_trip_created, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trip?.let {
            textViewLocations.text = "${it.route?.startLocationName}\n${it.route?.endLocationName}"
            textViewDateTime.text = it.startDateAndTime?.toString("dd/MM/yyyy',' hh:mm:ss a")

            //imageViewCode.setImageBitmap()
        }

        mbTripDone.setOnClickListener {
            callback?.onActionPerformed()
            dismiss()
        }
    }

    companion object {
        fun newInstance(trip: Trip, callback: DefaultCallback) =
            BottomSuccessPage().apply {
                this.trip = trip
                this.callback = callback
            }
    }

}

open class RoundedBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog: DialogInterface ->
            val dialogIn = dialog as BottomSheetDialog
            val bottomSheet =
                dialogIn.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val bottomSheetBehavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
            bottomSheetBehavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        return bottomSheetDialog
    }
}
