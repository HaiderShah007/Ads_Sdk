package com.example.adssdk.callbacks

import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd

interface NativeListener {
    fun nativeAdLoaded(currentNativeAd: NativeAd?)
    fun nativeAdFailed(loadAdError: LoadAdError)
    fun nativeAdValidate(string: String)
}