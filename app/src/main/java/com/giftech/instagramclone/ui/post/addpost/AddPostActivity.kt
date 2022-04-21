package com.giftech.instagramclone.ui.post.addpost

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.giftech.instagramclone.core.ui.LoadingDialog
import com.giftech.instagramclone.core.utils.AppUtils
import com.giftech.instagramclone.core.viewmodel.ViewModelFactory
import com.giftech.instagramclone.databinding.ActivityAddPostBinding
import com.giftech.instagramclone.ui.home.HomeActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File


class AddPostActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_PHOTO_FILE = "EXTRA_PHOTO_FILE"
    }

    private lateinit var binding: ActivityAddPostBinding
    private lateinit var viewModel: AddPostViewModel
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var photoFile:File? = null
    private var lat:Double? = null
    private var long:Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideActionBar()
        setupViewmodel()
        setupLoading()
        setupFusedLocation()

        getPhotoFile()
        getUserLocation()
        binding.header.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.header.btnProceed.setOnClickListener {
            if(photoFile!=null && lat!=null && long!=null){
                val file = AppUtils.reduceFileImage(photoFile!!)
                val desc = binding.etCaption.text.toString()
                viewModel.uploadPost(file, desc, lat!!, long!!).observe(this, { success ->
                    if(success){
                        moveToHome()
                    }
                })
            }
        }

        viewModel.loading.observe(this){loading ->
            if (loading) loadingDialog.show() else loadingDialog.dismiss()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getUserLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getUserLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun getUserLocation() {
        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    long = location.longitude
                    AppUtils.showToast(this, "Location Retrieved")
                } else {
                    Toast.makeText(
                        this,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
    private fun setupFusedLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }
    private fun setupLoading() {
        loadingDialog = LoadingDialog(this,false)
    }

    private fun moveToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun getPhotoFile() {
        val extras = intent.extras
        val pictureFile: File? = extras!![EXTRA_PHOTO_FILE] as File?
        photoFile = pictureFile
        val uri = Uri.fromFile(pictureFile)
        binding.ivPreview.setImageURI(uri)
    }

    private fun setupViewmodel() {
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this,factory)[AddPostViewModel::class.java]
    }
}