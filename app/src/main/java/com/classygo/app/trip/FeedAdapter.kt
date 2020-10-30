package com.classygo.app.trip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.classygo.app.R
import com.classygo.app.model.NotificationItem
import com.classygo.app.model.PaymentMethodItem
import com.classygo.app.model.SeatItem
import com.classygo.app.model.Trip
import com.classygo.app.utils.DefaultCallback
import com.classygo.app.utils.PaymentTypes
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import khronos.toString


/**
 * Created by Monarchy on 17/10/2020.
 */

class FeedAdapter(private var items: List<Any>, private var callback: DefaultCallback? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val constant = 100
    private val notificationConstant = 200
    private val paymentMethodConstant = 300
    private val seatConstant = 400

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(viewGroup.context)
        when (viewType) {
            constant -> {
                val viewHolderItem = inflater.inflate(R.layout.trip_card_item, viewGroup, false)
                viewHolder = TripViewHolder(viewHolderItem)
            }
            notificationConstant -> {
                val viewHolderItem =
                    inflater.inflate(R.layout.notification_card_item, viewGroup, false)
                viewHolder = NotificationViewHolder(viewHolderItem)
            }
            paymentMethodConstant -> {
                val viewHolderItem =
                    inflater.inflate(R.layout.payment_method_card_item, viewGroup, false)
                viewHolder = PaymentMethodViewHolder(viewHolderItem)
            }
            seatConstant -> {
                val viewHolderItem =
                    inflater.inflate(R.layout.seat_item, viewGroup, false)
                viewHolder = SeatViewHolder(viewHolderItem)
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            constant -> {
                configureTripViewHolder(
                    holder as TripViewHolder,
                    items[position] as Trip,
                    callback = callback
                )
            }
            seatConstant -> {
                configureSeatViewHolder(
                    holder as SeatViewHolder,
                    items[position] as SeatItem,
                    callback = callback
                )
            }
            notificationConstant -> {
                configureNotificationViewHolder(
                    holder as NotificationViewHolder,
                    items[position] as NotificationItem
                )
            }
            paymentMethodConstant -> {
                configurePaymentMethodViewHolder(
                    holder as PaymentMethodViewHolder,
                    items[position] as PaymentMethodItem,
                    callback
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            items[position] is Trip -> constant
            items[position] is NotificationItem -> notificationConstant
            items[position] is PaymentMethodItem -> paymentMethodConstant
            items[position] is SeatItem -> seatConstant
            else -> -1
        }
    }

    // MARK: configure the trip
    private fun configureTripViewHolder(
        viewHolder: TripViewHolder,
        data: Trip, callback: DefaultCallback?
    ) {
        val imageViewTrip = viewHolder.imageViewTrip
        val textViewTitle = viewHolder.textViewTitle
        val textViewEtaInfo = viewHolder.textViewEtaInfo
        val buttonJoinTrip = viewHolder.buttonJoinTrip
        textViewTitle?.text = "${data.route?.startLocationName} - ${data.route?.endLocationName}"
        textViewEtaInfo?.text = data.startDateAndTime?.toString("dd/MM/yyyy',' hh:mm:ss a")
        data.busImage?.let {
            if (it.isNotEmpty()) {
                Picasso.get().load(data.busImage).into(imageViewTrip)
            }
        }

        buttonJoinTrip?.setOnClickListener {
            callback?.onActionPerformed(data)
        }
    }

    // MARK: configure the seat
    private fun configureSeatViewHolder(
        viewHolder: SeatViewHolder,
        data: SeatItem, callback: DefaultCallback?
    ) {
        val imageViewSeatImage = viewHolder.imageViewSeatImage
        val textViewSeatNumber = viewHolder.textViewSeatNumber
        textViewSeatNumber?.text = data.number
        data.isSelected?.let {
            if (it) {
                imageViewSeatImage?.setImageResource(R.drawable.ic_car_seat_booked)
            } else {
                imageViewSeatImage?.setImageResource(R.drawable.ic_car_seat_available)
            }
        }

        viewHolder.itemView.setOnClickListener {
            callback?.onActionPerformed(viewHolder.adapterPosition)
        }
    }

    // MARK: configure the notification
    private fun configureNotificationViewHolder(
        viewHolder: NotificationViewHolder,
        data: NotificationItem
    ) {
        val textViewTitle = viewHolder.textViewTitle
        val textViewMessage = viewHolder.textViewMessage
        textViewTitle?.text = data.title
        textViewMessage?.text = data.date?.toString("dd/MM/yyyy',' hh:mm:ss a")
    }

    // MARK: configure the payment method
    private fun configurePaymentMethodViewHolder(
        viewHolder: PaymentMethodViewHolder,
        data: PaymentMethodItem, callback: DefaultCallback?
    ) {
        val textViewTitle = viewHolder.textViewTitle
        val textViewContent = viewHolder.textViewContent
        val circleImageView = viewHolder.circleImageView
        textViewTitle?.text = data.name
        textViewContent?.text = data.pan

        when (data.type) {
            PaymentTypes.AMEX_CARD.name -> {
                circleImageView?.setImageResource(R.drawable.ic_amex)
            }
            PaymentTypes.VISA.name -> {
                circleImageView?.setImageResource(R.drawable.ic_visa)
            }
            PaymentTypes.MASTER_CARD.name -> {
                circleImageView?.setImageResource(R.drawable.ic_mastercard)
            }
        }

        viewHolder.itemView.setOnClickListener {
            callback?.onActionPerformed(data)
        }
    }
}

//MARK: view holder of the trip
class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imageViewTrip: ImageView? = null
    var textViewTitle: TextView? = null
    var textViewEtaInfo: TextView? = null
    var buttonJoinTrip: MaterialButton? = null

    init {
        imageViewTrip = itemView.findViewById(R.id.imageViewTrip)
        textViewTitle = itemView.findViewById(R.id.textViewTitle)
        textViewEtaInfo = itemView.findViewById(R.id.textViewEtaInfo)
        buttonJoinTrip = itemView.findViewById(R.id.buttonJoinTrip)
    }
}

//MARK: view holder of the notification
class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textViewTitle: TextView? = null
    var textViewMessage: TextView? = null

    init {
        textViewTitle = itemView.findViewById(R.id.textViewTitle)
        textViewMessage = itemView.findViewById(R.id.textViewMessage)
    }
}

//MARK: view holder of the payment method item
class PaymentMethodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textViewTitle: TextView? = null
    var textViewContent: TextView? = null
    var circleImageView: CircleImageView? = null

    init {
        textViewTitle = itemView.findViewById(R.id.textViewTitle)
        textViewContent = itemView.findViewById(R.id.textViewContent)
        circleImageView = itemView.findViewById(R.id.circleImageView)
    }
}

//MARK: view holder of the seat item
class SeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textViewSeatNumber: TextView? = null
    var imageViewSeatImage: ImageView? = null

    init {
        textViewSeatNumber = itemView.findViewById(R.id.textViewSeatNumber)
        imageViewSeatImage = itemView.findViewById(R.id.imageViewSeatImage)
    }
}


