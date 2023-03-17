package com.example.adssdk.banner_ads

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.adssdk.callbacks.BannerAdCallbacks
import com.example.adssdk.constants.Constant
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*

internal fun BannerAds.createBannerAd(callbacks: BannerAdCallbacks?) {

    if (activity == null) return
    if (!Constant.isBannerAdLoading) {
        Constant.isBannerAdLoading = true
        val adViewAdaptive = AdView(activity.applicationContext)
        view.addView(adViewAdaptive)
        adViewAdaptive.adUnitId =
            if (Constant.isDebug()) bannerTestId else bannerProductionId ?: bannerTestId
        adViewAdaptive.setAdSize(AdSize.BANNER)
        val adRequest = AdRequest.Builder().build()
        adViewAdaptive.loadAd(adRequest)
        adViewAdaptive.adListener = object : AdListener() {
            override fun onAdClicked() {
                Log.d(bannerLogs, "AdaptiveBanner : onAdClicked")
                callbacks?.onAdClicked()
                super.onAdClicked()
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.d(bannerLogs, "AdaptiveBanner : onAdFailedToLoad ${loadAdError.message}")
                callbacks?.onAdFailedToLoad(loadAdError.message)
                Constant.isBannerAdLoading = false
                super.onAdFailedToLoad(loadAdError)
            }

            override fun onAdImpression() {
                Log.d(bannerLogs, "AdaptiveBanner : onAdImpression")
                callbacks?.onAdImpression()
                super.onAdImpression()
            }

            override fun onAdLoaded() {
                Log.d(bannerLogs, "AdaptiveBanner : onAdLoaded")
                Constant.isBannerAdLoading = false
                callbacks?.onAdLoaded()
                super.onAdLoaded()
            }

            override fun onAdOpened() {
                Log.d(bannerLogs, "AdaptiveBanner : onAdOpened")
                callbacks?.onAdOpened()
                super.onAdOpened()
            }
        }
    } else {
        if (Constant.isDebug()) {
            throw IllegalStateException("Ad already in loading process")
        }
    }

}


internal fun BannerAds.createMediumRectangleBannerAd(callbacks: BannerAdCallbacks?) {

    if (activity == null) return
    if (!Constant.isMediumAdLoading) {
        Constant.isMediumAdLoading = true
        val adViewAdaptive = AdView(activity.applicationContext)
        view.addView(adViewAdaptive)
        adViewAdaptive.adUnitId =
            if (Constant.isDebug()) bannerTestId else bannerProductionId ?: bannerTestId
        adViewAdaptive.setAdSize(AdSize.MEDIUM_RECTANGLE)
        val adRequest = AdRequest.Builder().build()
        adViewAdaptive.loadAd(adRequest)
        adViewAdaptive.adListener = object : AdListener() {
            override fun onAdClicked() {
                Log.d(bannerLogs, "AdaptiveBanner : onAdClicked")
                callbacks?.onAdClicked()
                super.onAdClicked()
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.d(bannerLogs, "AdaptiveBanner : onAdFailedToLoad ${loadAdError.message}")
                callbacks?.onAdFailedToLoad(loadAdError.message)
                Constant.isMediumAdLoading = false
                super.onAdFailedToLoad(loadAdError)
            }

            override fun onAdImpression() {
                Log.d(bannerLogs, "AdaptiveBanner : onAdImpression")
                callbacks?.onAdImpression()
                super.onAdImpression()
            }

            override fun onAdLoaded() {
                Log.d(bannerLogs, "AdaptiveBanner : onAdLoaded")
                Constant.isMediumAdLoading = false
                callbacks?.onAdLoaded()
                super.onAdLoaded()
            }

            override fun onAdOpened() {
                Log.d(bannerLogs, "AdaptiveBanner : onAdOpened")
                callbacks?.onAdOpened()
                super.onAdOpened()
            }
        }
    } else {
        if (Constant.isDebug()) {
            throw IllegalStateException("Ad already in loading process")
        }
    }

}

internal fun BannerAds.createAdaptiveAd(callbacks: BannerAdCallbacks?) {

    if (activity == null) return
    if (!Constant.isAdaptiveAdLoading) {
        Constant.isAdaptiveAdLoading = true
        val adViewAdaptive = AdView(activity.applicationContext)
        view.addView(adViewAdaptive)
        adViewAdaptive.adUnitId =
            if (Constant.isDebug()) bannerTestId else bannerProductionId ?: bannerTestId
        adViewAdaptive.setAdSize(Constant.adSize(activity, view))
        val adRequest = AdRequest.Builder().build()
        adViewAdaptive.loadAd(adRequest)
        adViewAdaptive.adListener = object : AdListener() {
            override fun onAdClicked() {
                Log.d(bannerLogs, "AdaptiveBanner : onAdClicked")
                callbacks?.onAdClicked()
                super.onAdClicked()
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.d(bannerLogs, "AdaptiveBanner : onAdFailedToLoad ${loadAdError.message}")
                callbacks?.onAdFailedToLoad(loadAdError.message)
                Constant.isAdaptiveAdLoading = false
                super.onAdFailedToLoad(loadAdError)
            }

            override fun onAdImpression() {
                Log.d(bannerLogs, "AdaptiveBanner : onAdImpression")
                callbacks?.onAdImpression()
                super.onAdImpression()
            }

            override fun onAdLoaded() {
                Log.d(bannerLogs, "AdaptiveBanner : onAdLoaded")
                Constant.isAdaptiveAdLoading = false
                callbacks?.onAdLoaded()
                super.onAdLoaded()
            }

            override fun onAdOpened() {
                Log.d(bannerLogs, "AdaptiveBanner : onAdOpened")
                callbacks?.onAdOpened()
                super.onAdOpened()
            }
        }
    } else {
        if (Constant.isDebug()) {
            throw IllegalStateException("Ad already in loading process")
        }
    }

}


internal fun BannerAds.loadCollapsibleBanner(callbacks: BannerAdCallbacks?) {
    if (activity == null) return
    if (!Constant.isBannerCollapseAdLoading) {
        Constant.isBannerCollapseAdLoading = true
        Log.d(bannerLogs, "loadAdaptiveBanner : bannerId : $bannerProductionId")
        val extras = Bundle()
        extras.putString("collapsible", "bottom")
        view.removeAllViews()

        val adView = AdView(activity)
        view.addView(adView)
        adView.adUnitId =
            if (Constant.isDebug()) bannerTestId else bannerProductionId ?: bannerTestId
        adView.setAdSize(Constant.adSize(activity, view))
        val adRequest =
            AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter::class.java, extras).build()
        adView.loadAd(adRequest)

        adView.adListener = object : AdListener() {
            override fun onAdClicked() {
                Log.d(bannerLogs, "BannerWithSize : onAdClicked")
                callbacks?.onAdClicked()
                super.onAdClicked()
            }

            override fun onAdClosed() {
                Log.d(bannerLogs, "BannerWithSize : onAdClosed")
                callbacks?.onAdClosed()
                super.onAdClosed()
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                Constant.isBannerCollapseAdLoading = false
                Log.d(bannerLogs, "BannerWithSize : onAdFailedToLoad ${p0.message}")
                callbacks?.onAdFailedToLoad(p0.message)
                super.onAdFailedToLoad(p0)
            }

            override fun onAdImpression() {
                Log.d(bannerLogs, "BannerWithSize : onAdImpression")
                callbacks?.onAdImpression()
                super.onAdImpression()
            }

            override fun onAdLoaded() {
                Log.d(bannerLogs, "BannerWithSize : onAdLoaded")
                Constant.isBannerCollapseAdLoading = false
                callbacks?.onAdLoaded()
                super.onAdLoaded()
            }

            override fun onAdOpened() {
                Log.d(bannerLogs, "BannerWithSize : onAdOpened")
                callbacks?.onAdOpened()
                super.onAdOpened()
            }
        }
    }
}