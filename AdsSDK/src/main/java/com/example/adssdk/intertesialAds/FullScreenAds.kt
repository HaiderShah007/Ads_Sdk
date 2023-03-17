package com.example.adssdk.intertesialAds

import android.accounts.NetworkErrorException
import android.app.Activity
import android.util.Log
import com.example.adssdk.billing.BillingUtil
import com.example.adssdk.constants.Constant
import com.example.adssdk.constants.Constant.isAdLoading
import com.example.adssdk.constants.Constant.isDebug
import com.example.adssdk.constants.Constant.isNetworkAvailable
import com.example.adssdk.constants.Constant.isOpenAdNotShow
import com.example.adssdk.constants.Constant.isShowingInterAd
import com.example.adssdk.constants.Constant.mInterstitialAd
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar

/**
 * Created by
 * @Author: Haider Shah ,
 * @Company: The IT Zone ,
 * @Email: haiders902@gmail.com ,
 * on 11/5/2021 , Fri .
 *
 *
 */
class FullScreenAds(
    internal val activity: Activity?,
    internal var interstitialProductionId: String
) {

    internal val fullScreenLogs = "fullScreenLogs"
    private var interstitialTestId: String =
        "ca-app-pub-3940256099942544/8691691433"


    init {
        val regex = Regex(Constant.regexPattern)
        val isValid = regex.matches(interstitialProductionId)

        if (!isValid) {
            throw IllegalArgumentException("Your Ad Id is not correct: Please add in valid pattern e.g: ca-app-pub-3940256099942544/6300978111")
        }
        if (isDebug()) {
            if (interstitialProductionId == "") {
                interstitialProductionId = interstitialTestId
            }
        } else {
            if (interstitialProductionId == "") {
                throw IllegalArgumentException("Ad Id is not valid")

            } else if (Constant.getTestIds().contains(interstitialProductionId)) {
                throw IllegalArgumentException("Please add production Ad id in release version")
            }
        }
    }
/** First call this for load add, May be on splash*/
    fun loadFullScreenAd(adsListener: FullScreenAdListener) {
        if (activity == null) return
        if (isNetworkAvailable(activity) && !BillingUtil(
                activity,
                object : BillingUtil.SuccessPurchase {
                    override fun onSuccessPurchase() {
                    }

                }).checkPurchased(
                activity
            )
        ) {
            Log.d(
                fullScreenLogs,
                "full_screen loadFullScreenAd: request with ${this.interstitialProductionId}"
            )
            if (mInterstitialAd == null && !isAdLoading) {
                try {
                    isAdLoading = true
                    val adRequest = AdRequest.Builder().build()

                    InterstitialAd.load(activity.applicationContext, interstitialProductionId,
                        adRequest, object : InterstitialAdLoadCallback() {
                            override fun onAdFailedToLoad(adError: LoadAdError) {
                                Log.d(
                                    fullScreenLogs,
                                    "full_screen loadFullScreenAd : ${adError.message}"
                                )
                                isOpenAdNotShow = false
                                adsListener.adFailed()
                                isAdLoading = false
                                mInterstitialAd = null
                            }

                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                Log.d(interstitialProductionId, "loadFullScreenAd : Ad was loaded.")
                                mInterstitialAd = interstitialAd
                                isAdLoading = false
                                adsListener.adLoaded()
                            }
                        })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                adsListener.adAlreadyLoaded()
                Log.d(
                    interstitialProductionId,
                    "loadFullScreenAd : having a AD. or loading precious"
                )
            }
        } else {
            if (isDebug()) {
                throw NetworkErrorException("There is no internet connection available")
            }
        }

    }


    /**
     * After Calling load call show function preview your add.
     * */
    fun showAndLoad(
        adMobAdListener: AdMobFullScreenListener,
        newAdListener: FullScreenAdListener
    ) {
        if (activity == null) return
        if (mInterstitialAd != null && !BillingUtil(activity, object : BillingUtil.SuccessPurchase {
                override fun onSuccessPurchase() {
                }

            }).checkPurchased(activity)) {
            isOpenAdNotShow = true
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(fullScreenLogs, "full_screen Callback : Ad was dismissed.")
                    adMobAdListener.fullScreenAdDismissed()

                    isOpenAdNotShow = false
                    isShowingInterAd = false
                    mInterstitialAd = null
                    loadFullScreenAd(newAdListener)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.d(fullScreenLogs, "full_screen Callback : Ad failed to show.")
                    mInterstitialAd = null
                    isShowingInterAd = false
                    if (isDebug()) {
                        Snackbar.make(
                            activity.window.decorView.rootView,
                            "AD Error : ${adError.message}", Snackbar.LENGTH_LONG
                        ).show()
                    }
                    adMobAdListener.fullScreenAdFailedToShow()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(fullScreenLogs, "full_screen Callback : Ad showed fullscreen content.")
                    adMobAdListener.fullScreenAdShow()
                    isOpenAdNotShow = true
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                }
            }
            isShowingInterAd = true
            mInterstitialAd?.show(activity)

        } else {
            isShowingInterAd = false
            isOpenAdNotShow = false
            adMobAdListener.fullScreenAdNotAvailable()
        }
    }
}