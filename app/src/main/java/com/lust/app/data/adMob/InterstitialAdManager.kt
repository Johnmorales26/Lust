package com.lust.app.data.adMob

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.lust.app.BuildConfig
import java.util.Date

class InterstitialAdManager(private val context: Context) {

    private var interstitialAd: InterstitialAd? = null
    private var lastAdShowTime: Long = 0
    private var adInterval: Long = 10 * 60 * 1000

    init {
        MobileAds.initialize(context)
        loadInterstitialAd()
    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            if (BuildConfig.DEBUG) BuildConfig.AD_UNIT_ID_INTERSTITIAL_DEBUG else BuildConfig.AD_UNIT_ID_INTERSTITIAL_RELEASE,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    fun showAdIfReady(activity: Activity) {
        val currentTime = Date().time
        if (currentTime - lastAdShowTime >= adInterval) {
            interstitialAd?.let { ad ->
                ad.show(activity)
                lastAdShowTime = currentTime
                Log.d(InterstitialAdManager::class.simpleName, "Show AD")
                loadInterstitialAd()
            }
        }
    }

}