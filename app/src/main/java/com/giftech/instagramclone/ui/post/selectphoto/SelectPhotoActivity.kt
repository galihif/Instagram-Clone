package com.giftech.instagramclone.ui.post.selectphoto

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.giftech.instagramclone.core.utils.AppUtils
import com.giftech.instagramclone.core.utils.AppUtils.rotateBitmap
import com.giftech.instagramclone.core.utils.AppUtils.uriToFile
import com.giftech.instagramclone.databinding.ActivitySelectPhotoBinding
import com.giftech.instagramclone.ui.post.addpost.AddPostActivity
import com.giftech.instagramclone.ui.post.camera.CameraActivity
import java.io.File

class SelectPhotoActivity : AppCompatActivity() {

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    private lateinit var binding: ActivitySelectPhotoBinding

    private var photoFile:File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideActionBar()

        requestPermission()

        binding.containerHeader.btnClose.setOnClickListener {
            onBackPressed()
        }

        binding.containerHeader.btnProceed.setOnClickListener {
            if(photoFile!=null){
                moveToAddPost()
            } else{
                AppUtils.showToast(this, "You have to choose a photo")
            }
        }

        binding.btnCamera.setOnClickListener {
            openCameraX()
        }

        binding.btnGallery.setOnClickListener {
            openGallery()
        }

    }

    private fun moveToAddPost() {
        val intent = Intent(this, AddPostActivity::class.java)
        intent.putExtra(AddPostActivity.EXTRA_PHOTO_FILE, photoFile)
        startActivity(intent)
    }

    private fun requestPermission() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)

            photoFile = myFile

            binding.ivImage.setImageURI(selectedImg)
        }
    }

    private fun openCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            photoFile = myFile

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.ivImage.setImageBitmap(result)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }
}