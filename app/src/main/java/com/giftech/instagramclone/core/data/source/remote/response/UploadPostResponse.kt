package com.giftech.instagramclone.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UploadPostResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
