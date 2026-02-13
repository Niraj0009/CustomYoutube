package com.custom.youtube

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.View
import android.webkit.WebChromeClient
import android.widget.FrameLayout

class MyWebChromeClient(
    private val activity: Activity
) : WebChromeClient() {

    private var customView: View? = null
    private var customViewCallback: CustomViewCallback? = null
    private var originalSystemUiVisibility = 0
    private var originalOrientation = 0

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        if (customView != null) {
            callback?.onCustomViewHidden()
            return
        }

        customView = view
        customViewCallback = callback

        originalSystemUiVisibility = activity.window.decorView.systemUiVisibility
        originalOrientation = activity.requestedOrientation

        (activity.window.decorView as FrameLayout).addView(
            customView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        activity.window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onHideCustomView() {
        (activity.window.decorView as FrameLayout).removeView(customView)
        customView = null
        customViewCallback?.onCustomViewHidden()

        activity.window.decorView.systemUiVisibility = originalSystemUiVisibility
        activity.requestedOrientation = originalOrientation
    }
}
