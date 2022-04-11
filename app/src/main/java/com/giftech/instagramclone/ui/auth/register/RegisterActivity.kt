package com.giftech.instagramclone.ui.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.giftech.instagramclone.core.data.model.User
import com.giftech.instagramclone.core.ui.LoadingDialog
import com.giftech.instagramclone.core.utils.AppUtils
import com.giftech.instagramclone.core.viewmodel.ViewModelFactory
import com.giftech.instagramclone.databinding.ActivityRegisterBinding
import com.giftech.instagramclone.ui.auth.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideActionBar()

        setupViewmodel()
        setupLoading()

        binding.btnLogin.setOnClickListener {
            moveToLogin()
        }

        binding.btnSignup.setOnClickListener {
            if(isFormValid()){
                registerUser()
            }
        }

        viewModel.loading.observe(this){loading ->
            if (loading) loadingDialog.show() else loadingDialog.dismiss()
        }

    }

    private fun setupLoading() {
        loadingDialog = LoadingDialog(this,false)
    }

    private fun registerUser() {
        val user = User()
        with(binding){
            user.username = etUsername.text.toString()
            user.email = etEmail.text.toString()
            user.password = etPassword.text.toString()
        }
        viewModel.register(user).observe(this, {isSuccess ->
            if(isSuccess){
                AppUtils.showToast(this, "Register Success, Please Log In")
                moveToLogin()
            }
        })
    }

    private fun setupViewmodel() {
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this,factory)[RegisterViewModel::class.java]
    }

    private fun isFormValid(): Boolean {
        with(binding){
            if (etUsername.text.isNullOrEmpty()){
                etUsername.error = "Username must be filled"
                return false
            }
            if(etPassword.text.isNullOrEmpty()){
                etPassword.error = "Password must be filled"
                return false
            }
            if(etPassword.text!!.length < 6){
                etPassword.error = "Password minimum 6 characters"
                return false
            }
        }
        return true
    }

    private fun moveToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    override fun onPause() {
        super.onPause()
        viewModel.loading.removeObservers(this)
    }

}