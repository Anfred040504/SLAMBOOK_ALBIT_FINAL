package com.example.slambook_albit_final

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.slambook_albit_final.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoView: VideoView = binding.logo // Ensure 'logo' is a VideoView in your layout

        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.book)
        videoView.setVideoURI(videoUri)

        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = false // Set if you want the video to loop
            videoView.start()
        }

        videoView.setOnCompletionListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val fadeIn = ObjectAnimator.ofFloat(binding.logo, "alpha", 0f, 1f)
        fadeIn.duration = 1500 // Duration of fade-in
        fadeIn.start()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // Delay of 3 seconds
    }
}
