package com.custom.youtube

import android.webkit.WebSettings
import android.webkit.WebView

public class YoutubePlayerHelper(private val webView: WebView) {

    init {
        setupWebView()
    }

    private fun setupWebView() {
        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.mediaPlaybackRequiresUserGesture = false
    }

    fun loadVideo(videoId: String) {
        val youtubeUrl =
            "https://admin.videocrypt.in/youtube/$videoId?autoplay=1&vq=hd1080&enablejsapi=1"

        val html = """
            <!DOCTYPE html>
            <html>
            <body style="margin:0;padding:0;background:black;">
                <iframe 
                    width="100%" 
                    height="100%" 
                    src="$youtubeUrl"
                    frameborder="0"
                    allow="autoplay; encrypted-media"
                    allowfullscreen>
                </iframe>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(
            "https://www.youtube.com",
            html,
            "text/html",
            "UTF-8",
            null
        )
    }
}
