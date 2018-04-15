package com.cplusc.expandablelayout

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation

fun View.expand() {
    val prevHeight = this.measuredHeight
    this.measure(android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.WRAP_CONTENT)
    val targetHeight = this.measuredHeight

    val heightVariation = targetHeight - prevHeight

    this.layoutParams.height = 1
    this.visibility = View.VISIBLE

    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            this@expand.layoutParams.height = if (interpolatedTime == 1.0f) {
                ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                (heightVariation * interpolatedTime + prevHeight).toInt()
            }
            this@expand.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    animation.duration = (heightVariation / this.context.resources.displayMetrics.density).toLong()
    this.startAnimation(animation)
}

@JvmOverloads
fun View.collapse(targetHeight: Int = 0) {
    val initialHeight = this.measuredHeight
    val heightVariation = initialHeight - targetHeight

    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1.0f && targetHeight == 0) {
                this@collapse.visibility = View.GONE
            } else {
                this@collapse.layoutParams.height = initialHeight - (heightVariation * interpolatedTime).toInt()
                this@collapse.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    animation.duration = (heightVariation / this.context.resources.displayMetrics.density).toLong()
    this.startAnimation(animation)
}