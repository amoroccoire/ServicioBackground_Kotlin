package com.example.serviciobackground

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder

class AudioService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private val channelId = "audio_service_channel"

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
        }
        return START_NOT_STICKY
    }

    private fun startPlayback() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            startForeground(1, createNotification("Reproduciendo audio"))
        }
    }

    private fun pausePlayback() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            stopForeground(false)
        }
    }

    private fun stopPlayback() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            stopForeground(true)
            stopSelf()
        }
    }

    private fun createNotification(message: String): Notification {
        val stopIntent = Intent(this, AudioService::class.java).apply { action = "STOP" }
        val stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        return Notification.Builder(this, channelId)
            .setContentTitle("Reproductor de Audio")
            .setContentText(message)
            .setSmallIcon(R.drawable.headphones) // Cambia al ícono deseado
            .addAction(R.drawable.stop, "Detener", stopPendingIntent)
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

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }

}