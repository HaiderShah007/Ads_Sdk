package com.example.adssdk.remote_config

import android.app.Activity
import android.util.Log
import com.example.adssdk.callbacks.RemoteConfigCallback
import com.example.adssdk.constants.Constant.ads_json
import com.example.adssdk.constants.Constant.getFetchInterval
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.testads.sdk.R

class RemoteConfiguration(private val activity: Activity) {
    private var remoteConfig: FirebaseRemoteConfig? = null

    init {
        remoteConfig = FirebaseRemoteConfig.getInstance()
    }

    fun initializeConfig(callback: RemoteConfigCallback) {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = getFetchInterval()
        }
        remoteConfig?.setConfigSettingsAsync(configSettings)
        remoteConfig?.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig?.fetchAndActivate()?.addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                val updated = task.result
                Log.e("RemoteConfig", "Config params updated: $updated")
                val adsJson = remoteConfig!![ads_json].asString()
                callback.onSuccess(adsJson)

            } else {
                task.exception?.let { callback.onFailure(it) }
                Log.e("RemoteConfig", "Fetch failed  ${task.exception}")
            }
        }
    }
}