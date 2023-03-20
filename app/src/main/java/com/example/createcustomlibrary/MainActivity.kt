package com.example.createcustomlibrary

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.adssdk.banner_ads.BannerAds
import com.example.adssdk.callbacks.NativeListener
import com.example.adssdk.callbacks.OnNativeView
import com.example.adssdk.callbacks.RemoteConfigCallback
import com.example.adssdk.intertesialAds.AdMobFullScreenListener
import com.example.adssdk.intertesialAds.FullScreenAdListener
import com.example.adssdk.intertesialAds.FullScreenAds
import com.example.adssdk.native_ad.CreateNativeAds
import com.example.adssdk.remote_config.RemoteConfiguration
import com.example.adssdk.types.AdTypes
import com.example.createcustomlibrary.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    val bannerTestId = "ca-app-pub-3940256099942544/6300978111"
    val collapseBannerTestId = "ca-app-pub-3940256099942544/2014213617"
    val App_Open = "ca-app-pub-3940256099942544/3419835294"
    val Interstitial = "ca-app-pub-3940256099942544/1033173712"
    val Interstitial_Video = "ca-app-pub-3940256099942544/8691691433"
    val Rewarded = "ca-app-pub-3940256099942544/5224354917"
    val Rewarded_Interstitial = "ca-app-pub-3940256099942544/5354046379"
    val Native_Advanced = "ca-app-pub-3940256099942544/2247696110"
    val Native_Advanced_Video = "ca-app-pub-3940256099942544/1044960115"

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RemoteConfiguration(this).initializeConfig(object : RemoteConfigCallback{
            override fun onSuccess(json: String) {
                Log.e("MainScreen",json)
            }

            override fun onFailure(exception: java.lang.Exception) {
                Log.e("MainScreen",exception.message.toString())
            }

        })

        try {
            FullScreenAds(this,Interstitial).loadFullScreenAd(object :FullScreenAdListener{

            })
        }catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
        try {
            val a = BannerAds(this,binding.bannerAds,collapseBannerTestId, AdTypes.Collapse.toString())
            a.loadBanner(null)
        }catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }


        /*ads.loadNativeAd(object :NativeListener{

        })
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            ads.populateUnifiedNativeAdViewMain(object :OnNativeView{
                override fun nativeViewFind(
                    cardViewNative: ConstraintLayout,
                    headlineView: TextView,
                    bodyView: TextView,
                    ad: TextView
                ) {

                }

            })
        }*/
        binding.btnOpenAd.setOnClickListener {
            try {
                FullScreenAds(this,Interstitial).showAndLoad(object :AdMobFullScreenListener{
                    override fun fullScreenAdShow() {

                    }

                    override fun fullScreenAdDismissed() {

                    }

                    override fun fullScreenAdFailedToShow() {

                    }

                    override fun fullScreenAdNotAvailable() {

                    }

                }, object :FullScreenAdListener{

                })
            }catch (e:Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val ads = CreateNativeAds(this)
        ads.loadNativeAd(this,Native_Advanced_Video,object :NativeListener{
            override fun nativeAdLoaded(currentNativeAd: com.google.android.gms.ads.nativead.NativeAd?) {
                if (currentNativeAd != null) {
                    ads.populateUnifiedNativeAdViewMain(this@MainActivity,currentNativeAd,171.0f,object :OnNativeView{
                        override fun nativeViewFind(
                            cardViewNative: ConstraintLayout,
                            headlineView: TextView,
                            bodyView: TextView,
                            ad: TextView
                        ) {
                            Toast.makeText(this@MainActivity, headlineView.text, Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }

            override fun nativeAdFailed(loadAdError: com.google.android.gms.ads.LoadAdError) {
                Toast.makeText(this@MainActivity, loadAdError.message, Toast.LENGTH_SHORT).show()
            }

            override fun nativeAdValidate(string: String) {
                Toast.makeText(this@MainActivity, string, Toast.LENGTH_SHORT).show()
            }

        },171.0f)
    }
}