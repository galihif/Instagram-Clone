package com.giftech.instagramclone.core.ui

import android.app.Activity
import android.app.AlertDialog
import com.giftech.instagramclone.databinding.FragmentLoadingDialogBinding

class LoadingDialog(activity: Activity, isCancelable: Boolean) {

    private var dialog: AlertDialog? = null

    init {
        val binding = FragmentLoadingDialogBinding.inflate(activity.layoutInflater)
        val builder = AlertDialog.Builder(activity)
        builder.setView(binding.root)
        builder.setCancelable(isCancelable)
        dialog = builder.create()
    }

    fun show() {
        dialog!!.show()
    }

    fun dismiss() {
        dialog!!.dismiss()
    }
}