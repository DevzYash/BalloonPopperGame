package com.yash.balloonpopper

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatImageView
import com.yash.balloonpopper.Utils.PixelsUtils


class Balloon : AppCompatImageView, Animator.AnimatorListener,
    ValueAnimator.AnimatorUpdateListener, View.OnClickListener {
    constructor(context: Context) : super(context)

    private lateinit var animator: ValueAnimator
    private lateinit var listener: BalloonListener
    private var isPopped: Boolean = false
    lateinit var colorName: String
    var colorcode = 0

    constructor(context: Context, color: Int, rawHeight: Int) : super(context) {
        setImageResource(R.drawable.balloon)
        colorcode = color
        if (color == Color.RED) {
            colorName = "Red"
        } else {
            colorName = "Blue"
        }
        setColorFilter(color)
        val rawWidth = rawHeight / 2
        val dpHeight = PixelsUtils().pixelsToDp(height, context)
        val dpWidth = PixelsUtils().pixelsToDp(rawWidth, context)

        layoutParams = ViewGroup.LayoutParams(dpWidth, dpHeight)
        isClickable = true
        setOnClickListener(this)
        //setOnTouchListener(this)
        listener = context as BalloonListener

    }

    fun releaseBalloon(screenHeight: Int, duration: Int) {
        animator = ValueAnimator()
        animator.apply {
            setDuration(duration.toLong())
            setFloatValues(screenHeight.toFloat(), 0f)
            interpolator = LinearInterpolator()
            setTarget(this@Balloon)
            addListener(this@Balloon)
            addUpdateListener(this@Balloon)
            start()
        }

    }

    override fun onClick(v: View?) {
        if (!isPopped) {
            listener.popBalloon(this, true, this.colorcode);
            isPopped = true;
            animator.cancel();
        }

    }


    interface BalloonListener {
        fun popBalloon(balloon: Balloon?, touched: Boolean, color: Int)
    }

    override fun onAnimationStart(animation: Animator) {
    }

    override fun onAnimationEnd(animation: Animator) {
        if (!isPopped) {
            listener.popBalloon(this, false, this.colorcode);
        }
    }

    override fun onAnimationCancel(animation: Animator) {
    }

    override fun onAnimationRepeat(animation: Animator) {
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        if (!isPopped) {
            y = animation.animatedValue as Float
        }
    }

    fun setPopped(popped: Boolean) {
        this.isPopped = popped
    }


}