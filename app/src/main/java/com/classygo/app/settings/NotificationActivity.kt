package com.classygo.app.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.classygo.app.R
import com.classygo.app.model.NotificationItem
import com.classygo.app.trip.FeedAdapter
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.activity_notification.toolbar
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class NotificationActivity : AppCompatActivity() {
    private val feedItems = ArrayList<NotificationItem>()
    private var baseAdapter: FeedAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        textViewTitle.text = getString(R.string.title_notification)


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
        val notificationItem = NotificationItem("", "", "", Date())
        feedItems.add(notificationItem)
        feedItems.add(notificationItem)
        feedItems.add(notificationItem)

        baseAdapter?.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}