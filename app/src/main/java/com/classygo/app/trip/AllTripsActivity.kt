package com.classygo.app.trip

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
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

        // MARK: search section
        tilSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val content = tilSearch.text.toString().trim()
                handleSearch(content)
                true
            } else false
        }

        getAllTrips()
    }

    //MARK: handle search
    private fun handleSearch(keyWord: String) {
        if (keyWord.isNotEmpty()) {
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun getAllTrips() {
        progressBar.visibility = View.VISIBLE
        firebaseFirestore.collection("trips")
            .get()
            .addOnSuccessListener { result ->
                progressBar.visibility = View.INVISIBLE
                result.forEach {
                    val data = it.data
                    val route = TripLocation()
                    val trip = Trip(
                        it.id,
                        route,
                        "",
                        data["busImage"].toString(),
                        Date(),
                        Date()
                    )
                    feedItems.add(trip)
                }
                baseAdapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                progressBar.visibility = View.INVISIBLE
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