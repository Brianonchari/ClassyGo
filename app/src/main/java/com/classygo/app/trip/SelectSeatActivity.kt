package com.classygo.app.trip

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.classygo.app.R
import com.classygo.app.model.SeatItem
import com.classygo.app.utils.DefaultCallback
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import kotlinx.android.synthetic.main.activity_select_seat.*
import kotlinx.android.synthetic.main.activity_select_seat.toolbar
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class SelectSeatActivity : AppCompatActivity(), View.OnClickListener {

    private var feedItems = ArrayList<SeatItem>()
    private var selectedSeats = ArrayList<SeatItem>()
    private var baseAdapter: FeedAdapter? = null
    //Flutterwave
    private val amount = 1500.0
    private val fName=""
    private val lName=""
    private var email =""
    private var narration ="Payment for buss ticket"
    private var txRef:String?=null
    private var country ="KE"
    private var currency= "KES"
    private val publicKey="PUBLIC_KEY_GOES_HERE"
    val encryptionKey = "YOUR_ENCRYPTION_KEY_GOES_HERE"
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
            //openPaymentActivity.launch(Intent(this, PaymentMethods::class.java))
            makePayment(amount)
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

    private fun makePayment(amount:Double){
/*
        Create instance of RavePayManager
         */
        RaveUiManager(this).setAmount(amount)
            .setCountry(country)
            .setCurrency(currency)
            .setEmail(email)
            .setfName(fName)
            .setlName(lName)
            .setNarration(narration)
            .setPublicKey(publicKey)
            .setEncryptionKey(encryptionKey)
            .setTxRef(txRef)
            .acceptAccountPayments(true)
            .acceptCardPayments(
                true
            )
            .acceptMpesaPayments(true)
            .onStagingEnv(false).allowSaveCardFeature(true)
            .initialize()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            val message = data.getStringExtra("response")
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, "SUCCESS $message", Toast.LENGTH_SHORT).show()
            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR $message", Toast.LENGTH_SHORT).show()
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "CANCELLED $message", Toast.LENGTH_SHORT).show()
            }
        }
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