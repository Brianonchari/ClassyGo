package com.classygo.app.trip

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.classygo.app.R
import com.classygo.app.model.SeatItem
import com.classygo.app.payment.PaymentMethods
import com.classygo.app.utils.DefaultCallback
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.activity_new_trip.*
import kotlinx.android.synthetic.main.activity_select_seat.*
import kotlinx.android.synthetic.main.activity_select_seat.toolbar
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.textViewTitle
import java.util.*

class SelectSeatActivity : AppCompatActivity(), View.OnClickListener {

    private var feedItems = ArrayList<SeatItem>()
    private var selectedSeats = ArrayList<SeatItem>()
    private var baseAdapter: FeedAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_seat)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        textViewTitle.text = getString(R.string.select_seat)

        baseAdapter = FeedAdapter(feedItems, callback = object : DefaultCallback {
            override fun onActionPerformed(data: Any?) {
                data?.let {
                    val selectedPosition = data as Int
                    feedItems[selectedPosition].isSelected =
                        !feedItems[selectedPosition].isSelected!!
                    baseAdapter?.notifyItemChanged(selectedPosition)
                }

            }
        })

        setUpRecyclerView()

        val numberAllowed = intent.getIntExtra("NUMBER", 0)
        val tripName = intent.getStringExtra("NAME")
        textViewBusName.text = tripName
        for (x in 1..numberAllowed) {
            val seat = SeatItem(x.toString(), x.toString(), false)
            feedItems.add(seat)
        }
        baseAdapter?.notifyDataSetChanged()

        buttonPay.setOnClickListener {
            openPaymentActivity.launch(Intent(this, PaymentMethods::class.java))
        }
    }

    //MARK: handle end location things
    private val openPaymentActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            setResult(Activity.RESULT_OK)
            finish()
        }

    //MARK: set the recycler view layouts
    private fun setUpRecyclerView() {
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = true
        val linearLayoutManager = GridLayoutManager(this, 4)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = baseAdapter
    }


    override fun onClick(view: View?) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}