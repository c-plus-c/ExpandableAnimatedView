package com.cplusc.expandablelayout

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.DefaultItemAnimator


class PreventableAnimator internal constructor() : DefaultItemAnimator() {

    private var animateMoves = false

    internal fun setAnimateMoves(animateMoves: Boolean) {
        this.animateMoves = animateMoves
    }

    override fun animateMove(
            holder: RecyclerView.ViewHolder, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        if (!animateMoves) {
            dispatchMoveFinished(holder)
            return false
        }
        return super.animateMove(holder, fromX, fromY, toX, toY)
    }
}