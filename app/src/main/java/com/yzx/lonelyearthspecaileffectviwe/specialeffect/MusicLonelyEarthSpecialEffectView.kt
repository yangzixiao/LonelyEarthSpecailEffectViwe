package com.yzx.lonelyearthspecaileffectviwe.specialeffect

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PathMeasure
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import java.security.Provider
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.cos
import kotlin.math.sin


/**
 * 播放页面音乐特效
 */
class MusicLonelyEarthSpecialEffectView(
    context: Context,
    attributeSet: AttributeSet?,
) :
    View(context, attributeSet) {

    private companion object {
        const val TAG = "yzx"
    }

    private var centerX = 0F
    private var centerY = 0F
    private var maxRadius = 0F

    private var mPaint: Paint = Paint()


    private val circles = mutableListOf<LonelyEarthCircleBean>()
    private val animators = mutableListOf<Animator>()
    private val random = Random()

    private var canvasRotate = 0
    private val provider = Provider(Long.MAX_VALUE, 500)
    private var isStart = false
    private var startSize = 0F
    private var waveColor = 0xffffff

    init {
        Log.e(TAG, "init: ")
        initPaint()
    }

    private fun initPaint() {
        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.WHITE
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = 2f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val measuredWidth = measuredWidth
        setMeasuredDimension(measuredWidth, measuredWidth)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = (w / 2).toFloat()
        centerY = centerX
        maxRadius = centerX
    }


    private fun providerCircleBean(): LonelyEarthCircleBean {
        val lonelyEarthCircleBean = LonelyEarthCircleBean()
        lonelyEarthCircleBean.radius = startSize
        lonelyEarthCircleBean.startRadius = startSize
        lonelyEarthCircleBean.maxRadius = maxRadius
        lonelyEarthCircleBean.startAngel = random.nextInt(360)
        lonelyEarthCircleBean.littleCircleRadius = random.nextInt(10) + 3
        return lonelyEarthCircleBean
    }

    private fun providerAnimator() {
        val lonelyEarthCircleBean = providerCircleBean()
        circles.add(lonelyEarthCircleBean)
        val animator = ValueAnimator.ofFloat(0F, 1F)
        animator.interpolator = LinearInterpolator()
        animator.duration = 5000
        animator.repeatCount = 1

        animators.add(animator)
        animator.addUpdateListener {
            lonelyEarthCircleBean.apply {
                if (radius > maxRadius) {
                    circles.remove(lonelyEarthCircleBean)
                }
                radius += 2
                alpha = ((maxRadius - radius) / (maxRadius - startRadius) * 255).toInt()
            }
            invalidate()
        }
        animator.start()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvasRotate += 1
        canvas.rotate(canvasRotate.toFloat(), centerX, centerY)
        if (!circles.isNullOrEmpty()) {
            circles.forEach {
                if (it.radius < it.maxRadius) {

                    mPaint.alpha = it.alpha
                    mPaint.style = Paint.Style.STROKE
                    canvas.drawCircle(centerX, centerY, it.radius, mPaint)
                    mPaint.style = Paint.Style.FILL

                    val radian = Math.toRadians(it.startAngel.toDouble())
                    val x = centerX + cos(radian) * it.radius
                    val y = centerY + sin(radian) * it.radius
                    canvas.drawCircle(
                        x.toFloat(), y.toFloat(),
                        it.littleCircleRadius.toFloat(),
                        mPaint
                    )
                }
            }
        }
    }


    fun setStartSize(newStartSize: Float) {
        startSize = newStartSize
    }

    fun start() {
        if (isStart) {
            return
        }
        provider.start()
        isStart = true
    }

    fun stop() {
        provider.cancel()
        isStart = false
    }

    inner class Provider(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {

        private var batchIndex = 0
        private var batchCount = 3
        private var isNewBatch = true
        override fun onTick(p0: Long) {

            if (isNewBatch) {
                if (batchIndex > batchCount) {
                    batchCount = random.nextInt(7) + 3
                    batchIndex = 0
                    isNewBatch = false
                }
                batchIndex++
                providerAnimator()
            } else {
                isNewBatch = true
            }
            Log.e(TAG, "onTick:circleSize ${circles.size}startRadius${startSize}startRadius${maxRadius}")
        }

        override fun onFinish() {

        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
        /**
         * 遍历未结束的动画
         */
        if (animators.isNotEmpty()) {
            animators.forEach {
                if (it.isRunning) {
                    it.cancel()
                }
            }
        }
    }

    fun setWaveColor(newWaveColor: Int) {
        waveColor = newWaveColor
        mPaint.color = waveColor
    }
}