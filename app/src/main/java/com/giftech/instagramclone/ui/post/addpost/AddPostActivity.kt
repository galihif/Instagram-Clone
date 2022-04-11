package com.giftech.instagramclone.ui.post.addpost

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.giftech.instagramclone.core.viewmodel.ViewModelFactory
import com.giftech.instagramclone.databinding.ActivityAddPostBinding
import java.io.File


class AddPostActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_PHOTO_FILE = "EXTRA_PHOTO_FILE"
    }

    private lateinit var binding: ActivityAddPostBinding
    private lateinit var viewModel: AddPostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideActionBar()

        setupViewmodel()

        getPhotoFile()

        binding.header.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun getPhotoFile() {
        val extras = intent.extras
        val pictureFile: File? = intent.extras!![EXTRA_PHOTO_FILE] as File?
        val uri = Uri.fromFile(pictureFile)
        binding.ivPreview.setImageURI(uri)
    }

    private fun setupViewmodel() {
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this,factory)[AddPostViewModel::class.java]
    }
}