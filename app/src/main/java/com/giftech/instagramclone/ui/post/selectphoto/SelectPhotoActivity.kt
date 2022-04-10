package com.giftech.instagramclone.ui.post.selectphoto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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

    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun setupViewModel(){
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this,factory)[SelectPhotoViewModel::class.java]
    }
}