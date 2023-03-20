package com.example.adssdk.native_ad

import android.accounts.NetworkErrorException
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.adssdk.billing.BillingUtil
import com.example.adssdk.callbacks.NativeListener
import com.example.adssdk.callbacks.OnNativeView
import com.example.adssdk.constants.Constant
import com.example.adssdk.constants.Constant.isDebug
import com.example.adssdk.constants.Constant.isNativeLoading
import com.example.adssdk.constants.Constant.isNetworkAvailable
import com.example.adssdk.constants.Constant.isOpenNative
import com.example.adssdk.constants.Constant.nativeCounter
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.testads.sdk.R


class CreateNativeAds : LinearLayoutCompat {

    private lateinit var addAppIcon: ImageView
    private lateinit var cardViewNative: ConstraintLayout
    private lateinit var adMedia: MediaView
    private lateinit var textAdHeadline: TextView
    private lateinit var textAdAdvertiser: TextView
    private lateinit var textAdStore: TextView
    private lateinit var textAdPrice: TextView
    private lateinit var adStar: RatingBar
    private lateinit var textAd: TextView
    private lateinit var textAdBody: TextView
    private lateinit var btnAdCall: MaterialButton
    private val nativeLogs = "fullNative"
    private val nativeTestId = "ca-app-pub-3940256099942544/2247696110"

    lateinit var nativeAdView: NativeAdView
    var currentNativeAd: NativeAd? = null

    init {
        try {
            removeAllViewsInLayout()
             inflate(context, R.layout.native_ad_view, this)

            textAd = findViewById(R.id.ad)
            textAdBody = findViewById(R.id.ad_body)
            btnAdCall = findViewById(R.id.ad_call_to_action)
            addAppIcon = findViewById(R.id.ad_app_icon)
            cardViewNative = findViewById(R.id.cardViewNative)
            adMedia = findViewById(R.id.ad_media)
            textAdHeadline = findViewById(R.id.ad_headline)
            adStar = findViewById(R.id.ad_stars)
            textAdPrice = findViewById(R.id.ad_price)
            textAdStore = findViewById(R.id.ad_store)
            textAdAdvertiser = findViewById(R.id.ad_advertiser)
            nativeAdView = findViewById(R.id.nativeAdView)

        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }


    constructor(context: Context) : super(context)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /*fun setImageView(resource: Int) {
        imageView.setImageResource(resource)
    }

    fun setTextHeadLine(text: String) {
        textHeadline.text = text
    }

    fun setHeadLineTextFont(font: Int) {
        val typeface: Typeface? = ResourcesCompat.getFont(context, font)
        textHeadline.typeface = typeface
    }

    fun setTextAd(text: String) {
        textAd.text = text
    }

    fun setTextAdFont(font: Int) {
        val typeface: Typeface? = ResourcesCompat.getFont(context, font)
        textAd.typeface = typeface
    }

    fun setTextAdBody(text: String) {
        textAdBody.text = text
    }

    fun setTextAdBodyFont(font: Int) {
        val typeface: Typeface? = ResourcesCompat.getFont(context, font)
        textAdBody.typeface = typeface
    }

    fun setButtonBackground(resource: Int) {
        btnAdCall.setBackgroundResource(resource)
    }

    fun setButtonText(text: String) {
        btnAdCall.text = text
    }

    fun setButtonTextFont(font: Int) {
        val typeface: Typeface? = ResourcesCompat.getFont(context, font)
        btnAdCall.typeface = typeface
    }

    fun setButtonBackgroundColor(color: Int) {
        btnAdCall.setBackgroundColor(color)
    }

    fun setButtonBackgroundTint(color: ColorStateList) {
        btnAdCall.backgroundTintList = color
    }*/


    fun loadNativeAd(
        activity: Activity,
        nativeAdId: String,
        nativeListener: NativeListener,
        adHeight: Float
    ) {
        val regex = Regex(Constant.regexPattern)
        val isValid = regex.matches(nativeAdId)
        if (!isDebug()) {
            if (Constant.getTestIds().contains(nativeAdId)) {
                throw IllegalArgumentException("Please add production Ad id in release version")
            }
        }
        if (!isValid) {
            throw IllegalArgumentException("Your Ad Id is not correct: Please add in valid pattern e.g: ca-app-pub-3940256099942544/6300978111")
        } else {
            if (isNetworkAvailable(context) && !BillingUtil(
                    context as Activity,
                    object : BillingUtil.SuccessPurchase {
                        override fun onSuccessPurchase() {
                        }

                    }).checkPurchased(context)
            ) {

                if (isNativeLoading) {
                    Log.d(nativeLogs, "  Native Already loading Ad")
                    return
                }
                if (currentNativeAd != null && nativeCounter == 0) {
                    nativeListener.nativeAdLoaded(currentNativeAd)
                    nativeCounter += 1
                    Log.d(nativeLogs, " Native  Having loaded Ad")
                    return
                } else {
                    nativeCounter = 0
                }

                isNativeLoading = true
                val builder = AdLoader.Builder(
                    activity,
                    if (isDebug()) nativeTestId else nativeAdId
                )
                builder.forNativeAd { nativeAd ->

                    if (currentNativeAd != null) {
                        currentNativeAd?.destroy()
                    }
                    isNativeLoading = false
                    currentNativeAd = nativeAd
                    Log.d(nativeLogs, " Native  loaded native Ad")
                    nativeListener.nativeAdLoaded(currentNativeAd)
                }

                val videoOptions = VideoOptions.Builder()
                    .setStartMuted(true)
                    .build()

                val adOptions = NativeAdOptions.Builder()
                    .setVideoOptions(videoOptions)
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                    .build()
                builder.withNativeAdOptions(adOptions)

                val adLoader = builder.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        nativeListener.nativeAdFailed(loadAdError)

                        if (isDebug()) {
                            Snackbar.make(
                                activity.window.decorView.rootView,
                                " Native AD Error Native: ${loadAdError.message}",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        Log.d(nativeLogs, "Native failed native Ad  ${loadAdError.message}")
                        isNativeLoading = false
                    }

                    override fun onAdImpression() {
                        currentNativeAd = null
                        isNativeLoading = false
                        Log.d(nativeLogs, "Native onAdImpression native Ad")
                        if (adHeight > 0.0) {
                            if (adHeight > 170.0) {
                                //firebaseAnalytics("mainAdImpression", "AdImpressionFromMediaView")
                            } else {
                                //firebaseAnalytics("mainAdImpression", "AdImpressionWithoutMediaView")

                            }
                        }
                        super.onAdImpression()
                    }

                    override fun onAdClicked() {
                        isOpenNative = true
                        Log.d(nativeLogs, "Native onAdClicked native Ad")
                        isNativeLoading = false
                        if (adHeight > 0.0) {
                            if (adHeight > 170.0) {
                                // firebaseAnalytics("mainAdClicked", "AdClickedFromMediaView")
                            } else {
                                //firebaseAnalytics("mainAdClicked", "AdClickedWithoutMediaView")

                            }
                        }
                        super.onAdClicked()
                    }

                    override fun onAdLoaded() {
                        isNativeLoading = false

                        Log.d(nativeLogs, "Native onAdLoaded native Ad")
                        super.onAdLoaded()
                    }
                }).build()

                adLoader.loadAd(
                    AdRequest.Builder().build()
                )
            } else {
                nativeListener.nativeAdValidate("hideAll")
                if (isDebug()) {
                    throw NetworkErrorException("There is no internet connection available")
                }
            }
        }


    }


    fun populateUnifiedNativeAdViewMain(
        activity: Activity,
        nativeAd: NativeAd,
        adHeight: Float,
        onNativeView: OnNativeView
    ) {
        nativeAdView =
            activity.layoutInflater.inflate(
                R.layout.native_ad_view,
                null
            ) as NativeAdView

        textAd = nativeAdView.findViewById(R.id.ad)
        textAdBody = nativeAdView.findViewById(R.id.ad_body)
        btnAdCall = nativeAdView.findViewById(R.id.ad_call_to_action)
        addAppIcon = nativeAdView.findViewById(R.id.ad_app_icon)
        cardViewNative = nativeAdView.findViewById(R.id.cardViewNative)
        adMedia = nativeAdView.findViewById(R.id.ad_media)
        textAdHeadline = nativeAdView.findViewById(R.id.ad_headline)
        adStar = nativeAdView.findViewById(R.id.ad_stars)
        textAdPrice = nativeAdView.findViewById(R.id.ad_price)
        textAdStore = nativeAdView.findViewById(R.id.ad_store)
        textAdAdvertiser = nativeAdView.findViewById(R.id.ad_advertiser)
        nativeAdView = nativeAdView.findViewById(R.id.nativeAdView)

        textAdHeadline.text = nativeAd.headline
        if (adHeight > 170.0) {
            adMedia.visibility = View.VISIBLE
            adMedia.setImageScaleType(ImageView.ScaleType.FIT_XY)
        } else {

        }
        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            textAdBody.visibility = View.INVISIBLE
        } else {
            textAdBody.visibility = View.VISIBLE
            textAdBody.text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            btnAdCall.visibility = View.INVISIBLE
        } else {
            btnAdCall.visibility = View.VISIBLE
            btnAdCall.text = nativeAd.callToAction
        }
        if (nativeAd.icon == null) {
            addAppIcon.visibility = View.GONE
        } else {
            addAppIcon.setImageDrawable(
                nativeAd.icon?.drawable
            )
            addAppIcon.visibility = View.VISIBLE
        }

        if (nativeAd.starRating == null) {
            adStar.visibility = View.INVISIBLE
        } else {
            adStar.rating = nativeAd.starRating?.toFloat()!!
            adStar.visibility = View.VISIBLE
        }
        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        nativeAdView.setNativeAd(nativeAd)
        onNativeView.nativeViewFind(
            cardViewNative,
            textAdHeadline, textAdBody, textAd
        )
    }
}