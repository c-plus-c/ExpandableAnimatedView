package com.cplusc.expandablelayout

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.expand_button)
                .setOnClickListener {
                    val view = findViewById<View>(R.id.expandable_view)
                    view?.expand()
                }

        findViewById<Button>(R.id.collapse_button)
                .setOnClickListener {
                    val view = findViewById<View>(R.id.expandable_view)
                    view?.collapse(200)
                }
    }
}
