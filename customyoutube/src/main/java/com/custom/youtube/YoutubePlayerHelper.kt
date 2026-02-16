package com.example.zoomvideosdk

import android.webkit.WebSettings
import android.webkit.WebView
import com.custom.youtube.MyWebChromeClient
import com.custom.youtube.WebViewActivity

class YoutubePlayerHelper(private val webView: WebView, val  activity: WebViewActivity) {

    init {
        setupWebView()
    }

    private fun setupWebView() {
        val settings: WebSettings = webView.settings
        settings.userAgentString =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome Safari"
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.mediaPlaybackRequiresUserGesture = false
        webView.webChromeClient = MyWebChromeClient(activity)
    }

    fun loadVideo(videoId: String) {
        val youtubeUrl =
            "https://admin.videocrypt.in/youtube/$videoId?autoplay=1&vq=hd1080&enablejsapi=1"

        val html = """
    <!DOCTYPE html>
    <html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
            html, body {
                margin: 0;
                padding: 0;
                width: 100%;
                height: 100%;
                background: black;
            }
            .video-container {
                position: relative;
                width: 100%;
                height: 100%;
            }
            .video-container iframe {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                border: 0;
            }
        </style>
    </head>
    <body>
        <div class="video-container">
            <iframe
                src="$youtubeUrl"
                allow="autoplay; encrypted-media"
                allowfullscreen>
            </iframe>
        </div>
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
