package com.example.adssdk.constants

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.testads.sdk.BuildConfig

object Constant {
    fun isDebug(): Boolean {
        return BuildConfig.BUILD_TYPE == "debug"
    }

    private val testAddArray:ArrayList<String> = ArrayList()
    const val KEY_IN_APP = "inApp"
    const val def_value = "IT_ZONE_SDK"
    const val ads_json = "ads_json"
    var isOpenAdNotShow: Boolean = false
    var isNativeLoading: Boolean = false
    val regexPattern = "ca-app-pub-\\d{16}/\\d{10}"
    var isBannerAdLoading: Boolean = false
    var isMediumAdLoading: Boolean = false
    var isAdaptiveAdLoading: Boolean = false
    var isBannerCollapseAdLoading: Boolean = false
    var isAdLoading: Boolean = false
    var mInterstitialAd: InterstitialAd? = null
    var isShowingInterAd = false
    var nativeCounter = 0
    var isOpenNative = false
    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    fun adSize(context: Activity, view: ViewGroup): AdSize {
        val display = context.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val density = outMetrics.density
        var adWidthPixels = view.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }
        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
    }

    fun getHeightAndWidth(view: ViewGroup): Pair<Int, Int> {
        var viewHeight = 0
        var viewWidth = 0
        val viewTreeObserver: ViewTreeObserver = view.getViewTreeObserver()
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {

                    viewHeight = view.getHeight()
                    viewWidth = view.getWidth()
                    if (viewHeight != 0) view.getViewTreeObserver()
                        .removeOnGlobalLayoutListener(this)
                }
            })
        }
        return Pair(viewHeight, viewWidth)
    }

    fun getFetchInterval(): Long {
        return if (isDebug()) {
            3
        } else {
            720
        }
    }

    fun getTestIds():ArrayList<String>{
        testAddArray.add("ca-app-pub-3940256099942544/3419835294")
        testAddArray.add("ca-app-pub-3940256099942544/6300978111")
        testAddArray.add("ca-app-pub-3940256099942544/1033173712")
        testAddArray.add("ca-app-pub-3940256099942544/8691691433")
        testAddArray.add("ca-app-pub-3940256099942544/5224354917")
        testAddArray.add("ca-app-pub-3940256099942544/5354046379")
        testAddArray.add("ca-app-pub-3940256099942544/2247696110")
        testAddArray.add("ca-app-pub-3940256099942544/1044960115")
        testAddArray.add("ca-app-pub-3940256099942544/2014213617")
        return testAddArray
    }
}