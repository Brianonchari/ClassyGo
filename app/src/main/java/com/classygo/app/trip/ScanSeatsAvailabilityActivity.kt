package com.classygo.app.trip

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.classygo.app.R
import com.classygo.app.utils.launchActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_scan_available_seats.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class ScanSeatsAvailabilityActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_available_seats)
        codeScanner = CodeScanner(this, scannerView)
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val result  = it.text
                Toast.makeText(this, "Scan Result $result", Toast.LENGTH_SHORT).show()
                if(result.isNotEmpty()){
                    startProgressBar()
                    launchActivity<SelectSeatActivity> ()
                }else{
                    progressBarSecondary.visibility = View.GONE
                }

            }
        }
        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                startProgressBar()
                Toast.makeText(this, "Camera Initialization Erro ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        checkPermission()
        scannerView.setOnClickListener {
            codeScanner.releaseResources()
        }
    }

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 10)
        } else {
            codeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 10 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            codeScanner.startPreview()
        } else {
            Toast.makeText(this, "Give permissions", Toast.LENGTH_SHORT).show()
        }
    }

    fun startProgressBar() {
        val visibility = if (progressBarSecondary.visibility == View.GONE) View.VISIBLE else View.GONE
        progressBarSecondary.visibility = visibility
        val textVisibility = if(check_availability.visibility ==View.GONE) View.VISIBLE else View.GONE
        check_availability.visibility = textVisibility
    }

    override fun onStart() {
        super.onStart()
        checkPermission()
    }

    override fun onPause() {
        super.onPause()
        codeScanner.releaseResources()
    }
}