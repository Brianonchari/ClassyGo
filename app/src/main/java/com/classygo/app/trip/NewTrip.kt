package com.classygo.app.trip

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.classygo.app.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.activity_new_trip.*

class NewTrip : AppCompatActivity() {
    private val startPaceRequestCode = 100
    private val endPaceRequestCode = 200
    private var startPlace: Place? = null
    private var endPlace: Place? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trip)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        tilStartLocation.setEndIconOnClickListener {
            startLocationPicker(startPaceRequestCode)
        }

        tilEndLocation.setEndIconOnClickListener {
            startLocationPicker(endPaceRequestCode)
        }

    }

    private fun startLocationPicker(placeRequestCode: Int) {
        if (isGooglePlayServicesAvailable(this)) {
            val builder = PlacePicker.IntentBuilder()
            startActivityForResult(builder.build(this), placeRequestCode)
        } else {
            Toast.makeText(
                this,
                getText(R.string.play_service_not_available),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // MARK: check the availability of google play
    private fun isGooglePlayServicesAvailable(activity: Activity): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(activity)
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                // googleApiAvailability.getErrorDialog(activity, status, 2404).show()
                return false
            }
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == startPaceRequestCode) {
                startPlace = PlacePicker.getPlace(this, data)
                tieTripStartLocation.setText(startPlace?.name)
            }
            if (requestCode == endPaceRequestCode) {
                endPlace = PlacePicker.getPlace(this, data)
                tieTripEndLocation.setText(endPlace?.name)
            }
        }
    }
}