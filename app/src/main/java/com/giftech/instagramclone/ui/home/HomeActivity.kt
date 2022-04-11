package com.giftech.instagramclone.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.giftech.instagramclone.R
import com.giftech.instagramclone.core.utils.AppUtils
import com.giftech.instagramclone.core.viewmodel.ViewModelFactory
import com.giftech.instagramclone.databinding.ActivityHomeBinding
import com.giftech.instagramclone.ui.auth.login.LoginActivity
import com.giftech.instagramclone.ui.post.selectphoto.SelectPhotoActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showActionBar()
        setupViewModel()

        getUserData()
        getAllPost()
    }

    private fun getAllPost() {
        viewModel.getAllPost().observe(this){
            it.forEach {
                Log.d("GALIH", it.photo)
            }
        }
    }

    private fun showActionBar() {
        supportActionBar?.title = ""
        supportActionBar?.setDisplayShowCustomEnabled(true)
        val inflater: LayoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.action_bar, null)
        supportActionBar?.customView = view
    }

    private fun getUserData() {
        val user = viewModel.getUser()
        AppUtils.showToast(this, "Welcome ${user.username}")
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this,factory)[HomeViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_add -> openSelectPhoto()
            R.id.item_logout -> logOutUser()
        }
        return true
    }

    private fun logOutUser() {
        viewModel.logout()
        moveToLogin()
        finish()
    }

    private fun moveToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun openSelectPhoto() {
        startActivity(Intent(this, SelectPhotoActivity::class.java))
    }
}