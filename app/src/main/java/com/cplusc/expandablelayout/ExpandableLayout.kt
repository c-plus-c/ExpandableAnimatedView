package com.cplusc.expandablelayout

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout


class ExpandableLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr){
    
    var collapsedHeight: Int = 0
    var isExpanded: Boolean = false
        private set

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableLayout, defStyleAttr, 0)
        collapsedHeight = typedArray.getDimensionPixelSize(R.styleable.ExpandableLayout_collapse_height, 0)
        isExpanded = typedArray.getBoolean(R.styleable.ExpandableLayout_is_expanded, false)

        typedArray.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if(isExpanded){
            expand()
        }else{
            collapse()
        }
    }

    fun expand() {
        layoutParams.height = WRAP_CONTENT
        isExpanded = true
    }

    fun collapse() {
        layoutParams.height = collapsedHeight
        isExpanded = false
    }
}