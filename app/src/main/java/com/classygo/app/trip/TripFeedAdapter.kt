package com.classygo.app.trip

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.classygo.app.R
import com.classygo.app.model.Trip
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso


/**
 * Created by Monarchy on 17/10/2020.
 */

class TripFeedAdapter(private val activity: Activity, private var items: List<Trip>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val constant = 100

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(viewGroup.context)
        when (viewType) {
            constant -> {
                val viewHolderItem = inflater.inflate(R.layout.trip_card_item, viewGroup, false)
                viewHolder = TripViewHolder(viewHolderItem)
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            constant -> {
                val viewHolderMallBanner = holder as TripViewHolder
                configureTripViewHolder(viewHolderMallBanner, items[position], activity)
            }
        }
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            items[position] is Trip -> constant
            else -> -1
        }
    }

    // MARK: configure the device
    private fun configureTripViewHolder(
        viewHolder: TripViewHolder,
        data: Trip,
        activity: Activity
    ) {
        val parent = viewHolder.itemView
        val imageViewTrip = viewHolder.imageViewTrip

        if (data.image?.isNotEmpty()!!) {
            Picasso.get().load(data.image).into(imageViewTrip)
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
