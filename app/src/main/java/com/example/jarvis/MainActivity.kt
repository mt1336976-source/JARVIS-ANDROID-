package com.example.jarvis

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : Activity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this, this)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(40, 60, 40, 40)

        status = TextView(this)
        status.text = "JARVIS is ready, Sir."
        status.textSize = 25f

        val button = Button(this)
        button.text = "TALK TO JARVIS"

        layout.addView(status)
        layout.addView(button)

        setContentView(layout)

        button.setOnClickListener {
            startListening()
        }
    }

    override fun onInit(result: Int) {
        if (result == TextToSpeech.SUCCESS) {
            tts.language = Locale.UK
            speak("Hello Sir. JARVIS is ready.")
        }
    }

    private fun speak(message: String) {
        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "JARVIS")
    }

    private fun startListening() {
        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to JARVIS")

            startActivityForResult(intent, 100)
        } catch (e: Exception) {
            Toast.makeText(this, "Speech recognition unavailable", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            val command = data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
                ?.lowercase(Locale.getDefault()) ?: return

            status.text = "You said: $command"

            when {
                command.contains("hello") || command.contains("hi") -> {
                    speak("Hello Sir. How can I help you?")
                }

                command.contains("time") -> {
                    val time = SimpleDateFormat("hh:mm a", Locale.UK).format(Date())
                    speak("Sir, the time is $time")
                }

                command.contains("settings") -> {
                    speak("Opening settings, Sir.")
                    startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
                }

                else -> {
                    speak("Sorry Sir, I don't understand that command yet.")
                }
            }
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
