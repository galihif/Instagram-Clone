package com.giftech.instagramclone.core.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.giftech.instagramclone.core.data.model.Post
import com.giftech.instagramclone.core.utils.AppUtils
import com.giftech.instagramclone.core.utils.AppUtils.loadImage
import com.giftech.instagramclone.databinding.ItemPostBinding
import com.giftech.instagramclone.ui.post.detail.DetailActivity

class PostAdapter:PagingDataAdapter<Post, PostAdapter.PostViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        if (post != null) {
            holder.bind(post)
        }
    }

    inner class PostViewHolder(private val binding: ItemPostBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(post:Post){
            with(binding){
                header.tvUsername.text = post.username
                ivImage.loadImage(post.photo)
                tvDesc.text = post.caption
                val location = AppUtils.getCity(itemView.context, post.lat, post.long)
                header.tvLocation.text = location
            }
            itemView.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivImage, "photo"),
                        Pair(binding.header.tvUsername, "username"),
                        Pair(binding.tvDesc, "desc"),
                    )

                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_POST, post)
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}