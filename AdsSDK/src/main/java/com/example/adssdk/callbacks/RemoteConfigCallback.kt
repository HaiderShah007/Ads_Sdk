package com.example.adssdk.callbacks

import java.lang.Exception

interface RemoteConfigCallback {
    fun onSuccess(json:String)
    fun onFailure(exception: Exception)
}