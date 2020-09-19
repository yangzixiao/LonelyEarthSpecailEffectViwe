package com.yzx.lonelyearthspecaileffectviwe

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var posterAnimator: ObjectAnimator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "http://p4.music.126.net/F8qPSIm1QGBkR2kNQrjP2Q==/109951164163689589.jpg"
        Glide.with(this).load(url)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(100, 6))).into(ivBackground)
        Glide.with(this).load(url).into(ivPoster)
         initPosterAnimator()
        lonelyEarthSpecialEffect.setStartSize((resources.displayMetrics.widthPixels / 2 - 80 * resources.displayMetrics.density).toFloat())

        button.setOnClickListener {
            if (posterAnimator.isStarted) {
                posterAnimator.resume()
            } else {
                posterAnimator.start()
            }
            lonelyEarthSpecialEffect.start()
        }
        button2.setOnClickListener {
            lonelyEarthSpecialEffect.stop()
            if (posterAnimator.isRunning) {
                posterAnimator.pause()
            }
        }
    }

    private fun initPosterAnimator(): Animator {
         posterAnimator =
            ObjectAnimator.ofFloat(ivPoster, "rotation", 0f, 360f)
        posterAnimator.apply {
            repeatCount = -1
            interpolator = LinearInterpolator()
            duration = 20000
        }
        return posterAnimator
    }

    override fun onDestroy() {
        super.onDestroy()
        if (posterAnimator.isRunning){
            posterAnimator.cancel()
        }
    }
}