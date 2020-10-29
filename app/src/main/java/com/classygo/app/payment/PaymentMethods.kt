package com.classygo.app.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.classygo.app.R
import com.classygo.app.model.NotificationItem
import com.classygo.app.model.PaymentMethodItem
import com.classygo.app.trip.FeedAdapter
import com.classygo.app.utils.DefaultCallback
import com.classygo.app.utils.PaymentTypes
import kotlinx.android.synthetic.main.activity_payment_methods.*
import kotlinx.android.synthetic.main.activity_payment_methods.recyclerView
import kotlinx.android.synthetic.main.activity_payment_methods.toolbar
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class PaymentMethods : AppCompatActivity() {
    private val feedItems = ArrayList<PaymentMethodItem>()
    private var baseAdapter: FeedAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_methods)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        textViewTitle.text = getString(R.string.title_payment_methods)

        setUpRecyclerView()
    }

    //MARK: set the recycler view layouts
    private fun setUpRecyclerView() {
        baseAdapter = FeedAdapter(feedItems)
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = true
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = baseAdapter

        addDummyData()
    }

    private fun addDummyData() {
        val item = PaymentMethodItem("34675", "Access Bank", PaymentTypes.VISA.name, "QR- 23424")
        val item2 =
            PaymentMethodItem("36336", "United Bank Pay", PaymentTypes.VISA.name, "VS - 78638")
        val item3 =
            PaymentMethodItem("9847", "Global Bank", PaymentTypes.MASTER_CARD.name, "MS - 77999")
        val item4 =
            PaymentMethodItem("0900", "United Bank", PaymentTypes.AMEX_CARD.name, "AM - 8365")
        feedItems.add(item)
        feedItems.add(item2)
        feedItems.add(item3)
        feedItems.add(item4)

        baseAdapter?.notifyDataSetChanged()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.payment_method_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        if (item.itemId == R.id.actionAdd) {
            addPaymentMethod()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //MARK: present the payment addition page
    private fun addPaymentMethod() {
        NewPaymentPage.newInstance(callback = object : DefaultCallback {
            override fun onActionPerformed(data: Any?) {
                data?.let {
                    feedItems.add(it as PaymentMethodItem)
                    baseAdapter?.notifyDataSetChanged()
                }
            }
        }).show(supportFragmentManager, "")
    }
}