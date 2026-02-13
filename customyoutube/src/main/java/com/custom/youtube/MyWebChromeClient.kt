package com.custom.youtube

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.View
import android.webkit.WebChromeClient
import android.widget.FrameLayout
import androidx.annotation.RequiresApi

class MyWebChromeClient(
    private val activity: Activity
) : WebChromeClient() {

    private var customView: View? = null
    private var customViewCallback: CustomViewCallback? = null
    private var originalSystemUiVisibility = 0
    private var originalOrientation = 0

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        if (customView != null) {
            callback?.onCustomViewHidden()
            return
        }

        customView = view
        customViewCallback = callback

        originalOrientation = activity.requestedOrientation

        (activity.window.decorView as FrameLayout).addView(
            customView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        activity.window.insetsController?.hide(
            android.view.WindowInsets.Type.systemBars()
        )

        activity.window.insetsController?.systemBarsBehavior =
            android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onHideCustomView() {
        (activity.window.decorView as FrameLayout).removeView(customView)
        customView = null
        customViewCallback?.onCustomViewHidden()

        activity.window.insetsController?.show(
            android.view.WindowInsets.Type.systemBars()
        )

        activity.requestedOrientation = originalOrientation
    }

}
