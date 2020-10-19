package com.classygo.app.trip

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.classygo.app.R
import com.classygo.app.model.Trip
import com.classygo.app.model.TripLocation
import kotlinx.android.synthetic.main.activity_all_trips.*
import java.util.*
import kotlin.collections.ArrayList

class AllTripsActivity : AppCompatActivity() {

    private val feedItems = ArrayList<Trip>()
    private var baseAdapter: TripFeedAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_trips)

        setSupportActionBar(toolbarHome as Toolbar?)
        supportActionBar?.title = ""

        setUpRecyclerView()

        fabNewTrip.setOnClickListener {
            startActivity(Intent(this, NewTrip::class.java))
        }
    }


    //MARK: set the recycler view layouts
    private fun setUpRecyclerView() {
        baseAdapter = TripFeedAdapter(feedItems)
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = true
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = baseAdapter

        addDummyData()
    }

    private fun addDummyData() {
        val route = TripLocation()
        val trip = Trip(
            "",
            route,
            "",
            "https://li1.modland.net/euro-truck-simulator-2/cars-bus/ets2_20200318_104918_00_ModLandNet.png",
            "12 Hours",
            Date(),
            Date()
        )

        feedItems.add(trip)
        feedItems.add(trip)
        feedItems.add(trip)

        baseAdapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}