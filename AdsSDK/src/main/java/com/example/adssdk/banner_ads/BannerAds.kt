package com.example.adssdk.banner_ads

import android.accounts.NetworkErrorException
import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.example.adssdk.billing.BillingUtil
import com.example.adssdk.callbacks.BannerAdCallbacks
import com.example.adssdk.constants.Constant.getHeightAndWidth
import com.example.adssdk.constants.Constant.getTestIds
import com.example.adssdk.constants.Constant.isDebug
import com.example.adssdk.constants.Constant.isNetworkAvailable
import com.example.adssdk.constants.Constant.regexPattern
import com.example.adssdk.types.AdTypes

/**
 * Created by
 * @Author: Haider Shah ,
 * @Company: The IT Zone ,
 * @Email: haiders902@gmail.com ,
 * on 11/5/2021 , Fri .
 *
 *
 */

class BannerAds(
    internal val activity: Activity?,
    internal val view: ViewGroup,
    internal var bannerProductionId: String?,
    internal val adSize: String
) {
    internal val bannerLogs = "bannerLogs"
    internal var bannerTestId: String =
        "ca-app-pub-3940256099942544/6300978111"  //ca-app-pub-3423026258455073/7842863205   38
    private var viewHeight: Int = 0
    private var viewWidth: Int = 0

    init {
        val regex = Regex(regexPattern)
        val isValid = regex.matches(bannerProductionId.toString())

        if (!isValid) {
            throw IllegalArgumentException("Your Ad Id is not correct: Please add in valid pattern e.g: ca-app-pub-3940256099942544/6300978111")
        }
        if (isDebug()) {
            if (bannerProductionId == "") {
                bannerProductionId = bannerTestId
            }
        } else {
            if (bannerProductionId == "") {
                throw IllegalArgumentException("Ad Id is not valid")

            } else if (getTestIds().contains(bannerProductionId)) {
                throw IllegalArgumentException("Please add production Ad id in release version")
            }

        }

        viewHeight = getHeightAndWidth(view).first
        viewWidth = getHeightAndWidth(view).second

    }

    /**
     * User AdSizes class for getting adsize
     *
     * **/
    fun loadBanner(callbacks: BannerAdCallbacks?) {
        //TODO: In app purchase check, remote config check, payment check
        if (isNetworkAvailable(activity) && !BillingUtil(activity ?: return,
                object : BillingUtil.SuccessPurchase {
                    override fun onSuccessPurchase() {
                    }

                }).checkPurchased(
                activity
            )
        ) {
            Log.d(bannerLogs, "loadAdaptiveBanner : bannerId : $bannerProductionId")
            val viewTreeObserver: ViewTreeObserver = view.viewTreeObserver
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val density: Float? = activity.resources?.displayMetrics?.density
                        val dpHeight: Float = view.measuredHeight / density!!
                        val dpWidth: Float = view.measuredWidth / density
                        viewHeight = dpHeight.toInt()
                        viewWidth = dpWidth.toInt()

                        Log.e(bannerLogs, viewHeight.toString())
                        Log.e(bannerLogs, viewWidth.toString())
                        when (adSize) {
                            AdTypes.BANNER.toString() -> {
                                if (viewHeight < 50 || viewWidth < 320) {
                                    if (isDebug()) {
                                        throw IllegalStateException("Incorrect view size, view height size must be greater or equal to 50dp and width must be 350dp")
                                    }
                                } else {
                                    createBannerAd(callbacks)
                                }
                            }
                            AdTypes.MEDIUM_RECTANGLE.toString() -> {
                                if (viewHeight < 250 || viewWidth < 300) {
                                    if (isDebug()) {
                                        throw IllegalStateException("Incorrect view size, view height size must be greater or equal to 250dp and width must be 300dp")
                                    }
                                } else {
                                    createMediumRectangleBannerAd(callbacks)
                                }
                            }
                            AdTypes.Collapse.toString() -> {
                                if (viewHeight < 50) {
                                    if (isDebug()) {
                                        throw IllegalStateException("Incorrect view size, view size must be greater or equal to 50dp")
                                    }
                                } else {
                                    loadCollapsibleBanner(callbacks)
                                }
                            }
                            AdTypes.Adaptive.toString() -> {
                                createAdaptiveAd(callbacks)
                            }
                            else -> {
                                if (isDebug()) {
                                    throw IllegalStateException("Incorrect Ad Size, Use AdSizes.BANNER or AdSizes.MEDIUM_RECTANGLE only")
                                }
                            }
                        }
                        if (viewHeight != 0) view.viewTreeObserver
                            .removeOnGlobalLayoutListener(this)
                    }
                })
            }

        } else {
            if (isDebug()) {
                throw NetworkErrorException("There is no internet connection available")
            }
        }
    }
}