package com.jarvis.android

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.view.Gravity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = TextView(this).apply {
            text = "Hello! I am JARVIS.\nYour personal AI assistant."
            textSize = 24f
            gravity = Gravity.CENTER
        }

        setContentView(textView)
    }
}
