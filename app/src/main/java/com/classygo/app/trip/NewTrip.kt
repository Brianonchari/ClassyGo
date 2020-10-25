package com.classygo.app.trip

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.classygo.app.R
import com.classygo.app.model.Trip
import com.classygo.app.model.TripLocation
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_new_trip.*
import java.util.*


class NewTrip : AppCompatActivity() {
    private var startPlace: Place? = null
    private var endPlace: Place? = null
    private var filePath: Uri? = null
    private var fileUrl: String = ""

    companion object {
        private const val TAG = "TRIP"
    }

    private var storageReference: StorageReference? = FirebaseStorage.getInstance().reference
    private var fireStore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trip)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        //MARK: start start location picker
        tilStartLocation.setEndIconOnClickListener {
            openStartLocationActivity.launch(PlacePicker.IntentBuilder().build(this))
        }

        //MARK: start end location picker
        tilEndLocation.setEndIconOnClickListener {
            openEndLocationActivity.launch(PlacePicker.IntentBuilder().build(this))
        }
        //MARK: date of departure picker
        tilDateOfDeparture.setEndIconOnClickListener {

        }
        //MARK: date of departure
        tieDateOfDeparture.setOnClickListener {

        }

        imageViewTrip.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            openImagePickerActivity.launch(intent)
        }

        mbPostTrip.setOnClickListener {
            saveTrip()
        }
    }

    //MARK: save the trip t the database
    private fun saveTrip() {
        if (startPlace == null || endPlace == null) {
            return
        }
        val numberOfPaxAllowed = tieTripNumberOfPax.text?.trim().toString()
        val tripLocation = TripLocation(
            startPlace!!.name.toString(),
            startPlace!!.address.toString(),
            startPlace!!.latLng.latitude,
            startPlace!!.latLng.longitude,
            endPlace!!.name.toString(),
            endPlace!!.address.toString(),
            endPlace!!.latLng.latitude,
            endPlace!!.latLng.longitude
        )
        val trip = Trip(
            Calendar.getInstance().timeInMillis.toString(),
            tripLocation,
            auth.currentUser?.uid,
            fileUrl,
            Date(),
            Date(),
            numberOfPaxAllowed.toInt(),
            0
        )
        mbPostTrip.isEnabled = false
        fireStore.collection("trips")
            .add(trip)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
            .addOnCompleteListener {
                mbPostTrip.isEnabled = true
            }

    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions: Map<String, Boolean> ->
            // Do something if some permissions granted or denied
            permissions.entries.forEach {
                // Do checking here
            }
        }

    // UploadImage method
    private fun uploadImage() {
        filePath?.let {
            mbPostTrip.isEnabled = false
            // Defining the child of storageReference
            val ref = storageReference?.child("images/" + UUID.randomUUID().toString())
            ref?.putFile(it)?.addOnSuccessListener { fileUploaded ->
                fileUrl = fileUploaded.uploadSessionUri?.path.toString()
                mbPostTrip.isEnabled = true
            }?.addOnFailureListener { e ->
                mbPostTrip.isEnabled = true
                Toast.makeText(this, getString(R.string.image_upload_failed), Toast.LENGTH_SHORT)
                    .show()
            }?.addOnProgressListener { taskSnapshot ->
                // percentage on the dialog box
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)

            }
        }
    }

    private val openStartLocationActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                startPlace = PlacePicker.getPlace(this, result.data)
                tieTripStartLocation.setText(startPlace?.name)
            }
        }

    private val openEndLocationActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                endPlace = PlacePicker.getPlace(this, result.data)
                tieTripEndLocation.setText(startPlace?.name)
            }
        }

    private val openImagePickerActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageFile = result.data
            }
        }
}