package com.example.serviciobackground

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder

class AudioService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private val channelId = "audio_service_channel"
    private var isForeground = false
    private lateinit var message: String

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        mediaPlayer = MediaPlayer.create(this, R.raw.renegade)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "PLAY" -> startPlayback()
            "PAUSE" -> pausePlayback()
            "STOP" -> stopPlayback()
            "SHOW_NOTIFICATION" -> showNotification()
            "HIDE_NOTIFICATION" -> hideNotification()
        }
        return START_NOT_STICKY
    }

    private fun startPlayback() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
        message = "Reproduciendo musica"
    }

    private fun pausePlayback() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
        message = "Musica pausada"
    }

    private fun stopPlayback() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun showNotification() {
        if (!isForeground) {
            startForeground(1, createNotification(message))
            isForeground = true
        }
    }

    private fun hideNotification() {
        if (isForeground) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            isForeground = false
        }
    }

    private fun createNotification(message: String): Notification {
        return Notification.Builder(this, channelId)
            .setContentTitle("Reproductor de Audio")
            .setContentText(message)
            .setSmallIcon(R.drawable.headphones)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Audio Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
