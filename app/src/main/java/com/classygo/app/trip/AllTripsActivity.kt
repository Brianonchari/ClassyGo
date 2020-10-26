package com.classygo.app.trip

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.classygo.app.R
import com.classygo.app.model.Trip
import com.classygo.app.model.TripLocation
import com.classygo.app.settings.NotificationActivity
import com.classygo.app.settings.ProfileActivity
import com.classygo.app.utils.launchActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_all_trips.*
import kotlinx.android.synthetic.main.home_toolbar.*
import java.util.*
import kotlin.collections.ArrayList

class AllTripsActivity : AppCompatActivity() {

    private val feedItems = ArrayList<Trip>()
    private var baseAdapter: FeedAdapter? = null
    private var firebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        private const val TAG = "ALL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_trips)

        setSupportActionBar(toolbarHome as Toolbar?)
        supportActionBar?.title = ""

        setUpRecyclerView()

        imageViewIcon.setOnClickListener {
            launchActivity<ProfileActivity>()
        }

        fabNewTrip.setOnClickListener {
            launchActivity<NewTripActivity>()
        }

        getAllTrips()
    }

    private fun getAllTrips() {
        firebaseFirestore.collection("trips")
            .get()
            .addOnSuccessListener { result ->
                result.forEach {
                    val data = it.data
                    val route = TripLocation()
                    val trip = Trip(
                        it.id,
                        route,
                        "",
                        "https://li1.modland.net/euro-truck-simulator-2/cars-bus/ets2_20200318_104918_00_ModLandNet.png",
                        Date(),
                        Date()
                    )
                    feedItems.add(trip)
                }
                baseAdapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents.", exception)
            }
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
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionNotification) {
            launchActivity<NotificationActivity>()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}