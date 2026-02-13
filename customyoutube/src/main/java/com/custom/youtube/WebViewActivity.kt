package com.custom.youtube

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_web_view)
        webView = findViewById<WebView>(R.id.webview);
        setupWebView()
        loadYoutubeVideo("roz9sXFkTuE")
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
        webView.webChromeClient = MyWebChromeClient(this)
    }

    private fun loadYoutubeVideo(videoId: String) {
        val youtubeUrl =
            "https://admin.videocrypt.in/youtube/$videoId?autoplay=1&vq=hd1080&enablejsapi=1"
        val html = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
        <body style="margin:0;padding:0;background:black;">
            <div class="video-container">
                <iframe 
                    id="player"
                    type="text/html"
                    width="100%" 
                    height="100%" 
                    src="$youtubeUrl" 
                    frameborder="1" 
                    allow="autoplay; encrypted-media" 
                    allowfullscreen>
                </iframe>
            </div>
            <style>
                .video-container { position: relative; padding-bottom: 56.25%; height: 0; overflow: hidden; }
                .video-container iframe { position: absolute; top: 0; left: 0; width: 100%; height: 100%; }
            </style>
        </body>
        </html>
    """.trimIndent()
        webView.loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "UTF-8", null)
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }
}