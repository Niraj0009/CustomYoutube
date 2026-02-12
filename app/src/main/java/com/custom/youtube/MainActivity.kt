package com.custom.youtube

import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class MainActivity : AppCompatActivity() {
    private lateinit var web: WebView
    private lateinit var seek: SeekBar
    private lateinit var playBtn: ImageButton

    private var duration = 0
    private var isPlaying = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val youTubePlayerView = findViewById<YouTubePlayerView?>(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)


        val options = IFramePlayerOptions.Builder(this)
            .controls(1)
            .autoplay(0)
            .rel(0)
            .build()

        youTubePlayerView?.initialize(
            object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("Y4-Ly0gHGLE", 0f)
                }
            },
            options
        )


    }

}