package com.example.news.data.backgound

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.example.news.R
import com.example.news.presentation.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationsHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = context.getSystemService<NotificationManager>()

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "New articles",
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager?.createNotificationChannel( channel)
        }
    }

    fun showNotification(topics: List<String>) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            PENDING_INTENT_RC,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.material_symbols_outlined_news)
            .setContentTitle("New articles")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setContentText("Updated subscriptions for following topics: ${topics.joinToString(", ")}")
            .build()

        notificationManager?.notify(NOTIFICATION_ID, notification)

    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "channel_id"
        private const val NOTIFICATION_ID = 1
        private const val PENDING_INTENT_RC = 1

    }
}