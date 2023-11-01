package com.example.firstproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class NotificationService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Create a notification channel (for Android 8.0 and higher)
        createNotificationChannel()

        // Create an intent to open an activity when the notification is clicked
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // Set the notification icon
            .setContentTitle("My Notification Title") // Set the title
            .setContentText("Hello, this is a basic notification.") // Set the message
            .setContentIntent(pendingIntent) // Set the click action
            .build()

        // Send the notification
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)

        // Return START_NOT_STICKY to indicate that if the service is killed, it should not be restarted
        return START_NOT_STICKY
    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel"
            val descriptionText = "My Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "my_channel_id"
        private const val NOTIFICATION_ID = 1
    }
}
