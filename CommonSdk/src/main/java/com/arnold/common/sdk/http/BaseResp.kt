package com.arnold.common.sdk.http

import com.google.gson.annotations.SerializedName

data class BaseResp<T>(
    @SerializedName("errormsg") var errorMsg: String = "",
    var msg: String = "",
    var result: Boolean = false,
    @SerializedName("resultcode") var resultCode: Int = 0,
    var data: T? = null
)