package com.example.serviciobackground

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playBtn = findViewById<Button>(R.id.play_button)
        val pauseBtn = findViewById<Button>(R.id.pause_button)
        val stopBtn = findViewById<Button>(R.id.stop_button)

        playBtn.setOnClickListener {
            val intent = Intent(this, AudioService::class.java).apply {
                action = "PLAY"
            }
            startService(intent)
        }

        pauseBtn.setOnClickListener {
            val intent = Intent(this, AudioService::class.java).apply {
                action = "PAUSE"
            }
            startService(intent)
        }

        stopBtn.setOnClickListener {
            val intent = Intent(this, AudioService::class.java).apply {
                action = "STOP"
            }
            startService(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, AudioService::class.java).apply {
            action = "HIDE_NOTIFICATION"
        }
        startService(intent)
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, AudioService::class.java).apply {
            action = "HIDE_NOTIFICATION"
        }
        startService(intent)
    }

    override fun onStop() {
        super.onStop()
        // Mostrar notificación cuando la app está en segundo plano
        val intent = Intent(this, AudioService::class.java).apply {
            action = "SHOW_NOTIFICATION"
        }
        startService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Detener el servicio al cerrar la app
        val intent = Intent(this, AudioService::class.java).apply {
            action = "STOP"
        }
        startService(intent)
    }
}
