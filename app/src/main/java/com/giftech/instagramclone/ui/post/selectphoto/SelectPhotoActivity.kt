package com.giftech.instagramclone.ui.post.selectphoto

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.giftech.instagramclone.core.utils.AppUtils.uriToFile
import com.giftech.instagramclone.core.viewmodel.ViewModelFactory
import com.giftech.instagramclone.databinding.ActivitySelectPhotoBinding

class SelectPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectPhotoBinding
    private lateinit var viewModel: SelectPhotoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideActionBar()

        setupViewModel()

        binding.containerHeader.btnClose.setOnClickListener {
            onBackPressed()
        }

        binding.btnGallery.setOnClickListener {
            openGallery()
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
            binding.ivImage.setImageURI(selectedImg)
        }
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun setupViewModel(){
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this,factory)[SelectPhotoViewModel::class.java]
    }
}