package com.giftech.instagramclone.ui.post.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.giftech.instagramclone.core.data.model.Post
import com.giftech.instagramclone.core.utils.AppUtils.loadImage
import com.giftech.instagramclone.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_POST = "EXTRA_POST"
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideActionBar()

        val extras = intent.extras
        if(extras!=null){
            val post = extras.getParcelable<Post>(EXTRA_POST)
            populateView(post)
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun populateView(post: Post?) {
        with(binding){
            ivImage.loadImage(post?.photo)
            tvUsername.text = post?.username
            tvDesc.text = post?.caption
        }
    }
}