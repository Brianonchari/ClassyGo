package com.classygo.app.trip

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.classygo.app.R
import com.classygo.app.model.Trip
import com.classygo.app.model.TripLocation
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import khronos.toString
import kotlinx.android.synthetic.main.activity_new_trip.*
import java.util.*


class NewTripActivity : AppCompatActivity() {
    private var startPlace: Place? = null
    private var endPlace: Place? = null
    private var filePath: Uri? = null
    private var fileUrl: String = ""
    private var date = Calendar.getInstance()

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
        textViewTitle.text = getString(R.string.title_new_trip)

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
            showDateTimePicker()
        }
        //MARK: date of departure
        tieDateOfDeparture.setOnClickListener {
            showDateTimePicker()
        }
        //MARK: allow to pick image
        imageViewTrip.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED

            ) {
                requestStoragePermission()

            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                openImagePickerActivity.launch(intent)
            }
        }

        mbPostTrip.setOnClickListener {
            saveTrip()
        }
        //MARK: ask permission
        requestStoragePermission()
    }

    private fun requestStoragePermission() {
        //MARK: ask permission
        requestMultiplePermissions.launch(
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }


    //MARK: save the trip t the database
    private fun saveTrip() {
        if (startPlace == null || endPlace == null) {
            Toast.makeText(this, "Provide a start and an end location", Toast.LENGTH_SHORT).show()
            return
        }
        if (fileUrl.isEmpty()) {
            Toast.makeText(this, "Provide an image of your bus and proceed", Toast.LENGTH_SHORT)
                .show()
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
            Date(date.timeInMillis),
            null,
            numberOfPaxAllowed.toInt(),
            0
        )
        mbPostTrip.isEnabled = false
        progressBar.visibility = View.VISIBLE
        fireStore.collection("trips")
            .add(trip)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
                BottomSuccessPage.newInstance(trip).show(supportFragmentManager, "")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
            .addOnCompleteListener {
                mbPostTrip.isEnabled = true
                progressBar.visibility = View.INVISIBLE
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
            progressBar.visibility = View.VISIBLE
            // Defining the child of storageReference
            val ref = storageReference?.child("images/" + UUID.randomUUID().toString())
            ref?.putFile(it)?.addOnSuccessListener { fileUploaded ->
                fileUrl = ref.downloadUrl.toString()
                Log.e("URL", fileUrl)
                mbPostTrip.isEnabled = true
                progressBar.visibility = View.INVISIBLE
            }?.addOnFailureListener { e ->
                mbPostTrip.isEnabled = true
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(this, getString(R.string.image_upload_failed), Toast.LENGTH_SHORT)
                    .show()
            }?.addOnProgressListener { taskSnapshot ->
                // percentage on the dialog box
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                Log.e("PROGRESS", progress.toString())
            }
        }
    }

    //MARK: handle start location things
    private val openStartLocationActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                startPlace = PlacePicker.getPlace(this, result.data)
                Log.e("LOC", startPlace.toString())
                tieTripStartLocation.setText(startPlace?.name)
            }
        }

    //MARK: handle end location things
    private val openEndLocationActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                endPlace = PlacePicker.getPlace(this, result.data)
                tieTripEndLocation.setText(startPlace?.name)
            }
        }

    //MARK: handle image picker
    private val openImagePickerActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                filePath = result.data?.data
                imageViewTrip.setImageURI(filePath)
                uploadImage()
            }
        }

    //MARK: present the data picker
    private fun showDateTimePicker() {
        val currentDate = Calendar.getInstance()
        DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(
                    this,
                    { view, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)
                        tieDateOfDeparture.setText(Date(date.timeInMillis).toString("dd/MM/yyyy',' hh:mm:ss a"))
                    }, currentDate[Calendar.HOUR_OF_DAY], currentDate[Calendar.MINUTE], false
                ).show()
            }, currentDate[Calendar.YEAR], currentDate[Calendar.MONTH], currentDate[Calendar.DATE]
        ).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}