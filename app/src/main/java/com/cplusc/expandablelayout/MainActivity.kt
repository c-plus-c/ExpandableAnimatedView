package com.cplusc.expandablelayout

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.transition.AutoTransition
import android.support.transition.Transition
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cplusc.expandablelayout.entity.Row

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var preventableAnimator: PreventableAnimator
    lateinit var expandCollapseTransition: AutoTransition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerViewAdapter()
        preventableAnimator = PreventableAnimator()
        recyclerView.itemAnimator = preventableAnimator

        expandCollapseTransition = AutoTransition().apply {
            this.duration = 120
            this.interpolator = android.view.animation.AnimationUtils.loadInterpolator(this@MainActivity, android.R.interpolator.bounce)
            this.addListener(object : android.support.transition.TransitionListenerAdapter() {
                @SuppressLint("ClickableViewAccessibility")
                override fun onTransitionStart(transition: Transition) {
                    recyclerView.setOnTouchListener({ _, _ ->
                        true
                    })
                }

                @SuppressLint("ClickableViewAccessibility")
                override fun onTransitionEnd(transition: Transition) {
                    preventableAnimator.setAnimateMoves(true)
                    recyclerView.setOnTouchListener(null)
                }
            })
        }
    }


    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val wrappingView = itemView.findViewById(R.id.wrapping) as ExpandableLayout
            val titleView = itemView.findViewById(R.id.title) as TextView
            val detailView = itemView.findViewById(R.id.detail) as TextView
        }

        private val EXPANSION_UPDATE_STATE = "update_state"

        private val dataList = mutableListOf<Row>(
                Row("1", "initially expanded", true),
                Row("2", "initially collapsed", false),
                Row("3", "initially expanded", true),
                Row("4", "initially collapsed", false),
                Row("5", "initially expanded", true)
        )

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
            return ViewHolder(inflatedView)
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
            super.onBindViewHolder(holder, position, payloads)

            if(payloads.any()){
                val payload = payloads[0] as? Pair<*, *>
                when(payload?.first as? String){
                    EXPANSION_UPDATE_STATE -> {
                        (payload.second as? Boolean)?.let {
                            setExpansion(holder, it)
                        }
                    }
                }
            }else{
                onBindViewHolder(holder, position)
            }
        }

        private fun setExpansion(holder: ViewHolder, isExpand: Boolean){
            if (isExpand) {
                holder.wrappingView.expand()
            } else {
                holder.wrappingView.collapse()
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.titleView.text = dataList[position].title
            holder.detailView.text = dataList[position].detail

            setExpansion(holder, dataList[position].isExpanding)

            holder.wrappingView.setOnClickListener {
                TransitionManager.beginDelayedTransition(recyclerView, expandCollapseTransition)
                preventableAnimator.setAnimateMoves(false)
                dataList[position].isExpanding = !dataList[position].isExpanding
                setExpansion(holder, dataList[position].isExpanding)
                this@RecyclerViewAdapter.notifyItemChanged(position, EXPANSION_UPDATE_STATE to dataList[position].isExpanding)
            }
        }
    }
}
