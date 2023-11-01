package com.example.firstproject

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class NotificationApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initChannel()
    }

    private fun initChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "my_channel_id"
            val channelName = "My Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}