package com.jamesjmtaylor.weg2015.utils

import android.animation.ValueAnimator
import android.view.View
import androidx.core.view.ViewCompat.setRotationY
import androidx.core.view.ViewCompat.setScaleX
import androidx.core.view.ViewCompat.setScaleY


/**
 * Created by jtaylor on 3/29/18.
 */
class FlipListener(private val mFrontView: View, private val mBackView: View) : ValueAnimator.AnimatorUpdateListener {
    private var mFlipped: Boolean = false

    init {
        this.mBackView.setVisibility(View.GONE)
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        val value = animation.animatedFraction
        val scaleValue = 0.625f + 1.5f * (value - 0.5f) * (value - 0.5f)

        if (value <= 0.5f) {
            this.mFrontView.setRotationY(180 * value)
            this.mFrontView.setScaleX(scaleValue)
            this.mFrontView.setScaleY(scaleValue)
            if (mFlipped) {
                setStateFlipped(false)
            }
        } else {
            this.mBackView.setRotationY(-180 * (1f - value))
            this.mBackView.setScaleX(scaleValue)
            this.mBackView.setScaleY(scaleValue)
            if (!mFlipped) {
                setStateFlipped(true)
            }
        }
    }

    private fun setStateFlipped(flipped: Boolean) {
        mFlipped = flipped
        this.mFrontView.setVisibility(if (flipped) View.GONE else View.VISIBLE)
        this.mBackView.setVisibility(if (flipped) View.VISIBLE else View.GONE)
    }
//Broken, but potentially useful, methods.
//    private fun isFlipped(): Boolean {
//        return this.getAnimatedFraction() === 1
//    }
//    private fun isFlipping(): Boolean {
//        val currentValue = mFlipAnimator.getAnimatedFraction()
//        return currentValue < 1 && currentValue > 0
//    }
//    private fun toggleFlip() {
//        if (isFlipped()) {
//            mFlipAnimator.reverse()
//        } else {
//            mFlipAnimator.start()
//        }
//    }
}