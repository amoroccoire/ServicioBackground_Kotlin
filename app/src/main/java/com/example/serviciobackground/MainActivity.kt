package com.example.serviciobackground

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

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

    override fun onDestroy() {
        super.onDestroy()
        // Enviar un comando opcional para detener el servicio si se desea finalizar la app
        val intent = Intent(this, AudioService::class.java).apply {
            action = "STOP"
        }
        startService(intent)
    }
}