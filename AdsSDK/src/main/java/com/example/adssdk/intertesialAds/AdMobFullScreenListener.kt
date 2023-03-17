package com.example.adssdk.intertesialAds

/**
 * Created by
 *
 * @Author: Mahboob Khan ,
 * @Company: The IT Zone ,
 * @Email: mahboobk522@gmail.com ,
 * on 11/5/2021 , Fri .
 */
interface AdMobFullScreenListener {
    fun fullScreenAdShow()
    fun fullScreenAdDismissed()
    fun fullScreenAdFailedToShow()
    fun fullScreenAdNotAvailable()
}