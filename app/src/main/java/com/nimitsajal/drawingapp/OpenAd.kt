package com.nimitsajal.drawingapp

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.*

class OpenAd : Application(), Application.ActivityLifecycleCallbacks, LifecycleObserver {

    private val LOG_TAG = "OpenAd"
    private var currentActivity: Activity? = null
//    private var OPEN_AD_UNIT_ID: String = "ca-app-pub-6377531315503643/5664657548"
    private var OPEN_AD_UNIT_ID: String = "ca-app-pub-3940256099942544/9257395921"

    private lateinit var appOpenAdManager: AppOpenAdManager
    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "OpenAd class onCreate started")
        registerActivityLifecycleCallbacks(this)
        MobileAds.initialize(this) {}
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        appOpenAdManager = AppOpenAdManager()
    }

    /** LifecycleObserver method that shows the app open ad when the app moves to foreground. */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.
        currentActivity?.let {
            appOpenAdManager.showAdIfAvailable(it)
        }
    }

    /** Show the ad if one isn't already showing. */
    fun showAdIfAvailable(activity: Activity, param: OnShowAdCompleteListener) {
        showAdIfAvailable(
            activity,
            object : OnShowAdCompleteListener {
                override fun onShowAdComplete() {
                    // Empty because the user will go back to the activity that shows the ad.
                }
            })
    }

    /** ActivityLifecycleCallback methods. */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        // Updating the currentActivity only when an ad is not showing.
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    /** Interface definition for a callback to be invoked when an app open ad is complete. */
    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }

    /** Inner class that loads and shows app open ads. */
    private inner class AppOpenAdManager {
        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false

        /** Keep track of the time an app open ad is loaded to ensure you don't show an expired ad. */
        private var loadTime: Long = 0

        /** Request an ad. */
        fun loadAd(context: Context) {
            if (!isLoadingAd && !isAdAvailable()) {
                isLoadingAd = true
                val adRequest = AdRequest.Builder().build()
                AppOpenAd.load(
                    context,
                    OPEN_AD_UNIT_ID,
                    adRequest,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    object : AppOpenAd.AppOpenAdLoadCallback() {
                        override fun onAdLoaded(ad: AppOpenAd) {
                            appOpenAd = ad
                            isLoadingAd = false
                            loadTime = Date().time
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            isLoadingAd = false
                            // Handle the error
                            val error = "domain: ${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}"
                            println(error)
                        }
                    }
                )
            }
        }

        fun showAdIfAvailable(context: Context) {
            if (!isShowingAd && isAdAvailable()) {
                appOpenAd?.show(context as Activity)
                isShowingAd = true
            }
        }

        private fun isAdAvailable(): Boolean {
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
        }

        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
            val dateDifference: Long = Date().time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return dateDifference < numMilliSecondsPerHour * numHours
        }

        /** Shows the ad if one isn't already showing. */
        fun showAdIfAvailable(
            activity: Activity,
            onShowAdCompleteListener: OnShowAdCompleteListener) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.d(LOG_TAG, "The app open ad is already showing.")
                return
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                Log.d(LOG_TAG, "The app open ad is not ready yet.")
                onShowAdCompleteListener.onShowAdComplete()
                loadAd(activity)
                return
            }

            appOpenAd?.setFullScreenContentCallback(
                object : FullScreenContentCallback() {

                    override fun onAdDismissedFullScreenContent() {
                        // Called when full screen content is dismissed.
                        // Set the reference to null so isAdAvailable() returns false.
                        Log.d(LOG_TAG, "Ad dismissed fullscreen content.")
                        appOpenAd = null
                        isShowingAd = false

                        onShowAdCompleteListener.onShowAdComplete()
                        loadAd(activity)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        // Called when fullscreen content failed to show.
                        // Set the reference to null so isAdAvailable() returns false.
                        Log.d(LOG_TAG, adError.message)
                        appOpenAd = null
                        isShowingAd = false

                        onShowAdCompleteListener.onShowAdComplete()
                        loadAd(activity)
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        Log.d(LOG_TAG, "Ad showed fullscreen content.")
                    }
                })
            isShowingAd = true
            appOpenAd?.show(activity)
        }
    }
}

//class OpenAds(private val activity: Activity) : DefaultLifecycleObserver {
//
//    private var appOpenAd: AppOpenAd? = null
//    private var isShowingAd = false
//    private var OPEN_AD_UNIT_ID = "ca-app-pub-6377531315503643/5664657548"
//
//    override fun onStart(owner: LifecycleOwner) {
//        super.onStart(owner)
//
//        if (!isShowingAd && isAdAvailable()) {
//            appOpenAd?.show(activity)
//            isShowingAd = true
//        } else {
//            loadAd()
//        }
//    }
//
//    fun loadAd() {
//        val adRequest = AdRequest.Builder().build()
//        AppOpenAd.load(
//            activity,
//            OPEN_AD_UNIT_ID,
//            adRequest,
//            object : AppOpenAd.AppOpenAdLoadCallback() {
//                override fun onAdLoaded(ad: AppOpenAd) {
//                    appOpenAd = ad
//                }
//
//                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                    // Handle the error
//                    val error = "domain: ${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}"
//                    println(error)
//                }
//            }
//        )
//    }
//
//    private fun isAdAvailable(): Boolean {
//        return appOpenAd != null
//    }
//}
