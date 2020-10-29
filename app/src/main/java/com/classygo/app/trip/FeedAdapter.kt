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
import com.classygo.app.model.Trip
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import khronos.toString


/**
 * Created by Monarchy on 17/10/2020.
 */

class FeedAdapter(private var items: List<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val constant = 100
    private val notificationConstant = 200
    private val paymentMethodConstant = 300

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
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            constant -> {
                configureTripViewHolder(holder as TripViewHolder, items[position] as Trip)
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
                    items[position] as PaymentMethodItem
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
            else -> -1
        }
    }

    // MARK: configure the trip
    private fun configureTripViewHolder(
        viewHolder: TripViewHolder,
        data: Trip
    ) {
        val imageViewTrip = viewHolder.imageViewTrip
        val textViewTitle = viewHolder.textViewTitle
        val textViewEtaInfo = viewHolder.textViewEtaInfo
        textViewTitle?.text = "${data.route?.startLocationName} - ${data.route?.endLocationName}"
        textViewEtaInfo?.text = data.startDateAndTime?.toString("dd/MM/yyyy',' hh:mm:ss a")
        data.busImage?.let {
            if (it.isNotEmpty()) {
                Picasso.get().load(data.busImage).into(imageViewTrip)
            }
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
        data: PaymentMethodItem
    ) {
        val textViewTitle = viewHolder.textViewTitle
        val textViewContent = viewHolder.textViewContent
        val circleImageView = viewHolder.circleImageView
        textViewTitle?.text = data.name
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

