package com.ife.keepscreenon

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*

class NotificationHelper(private val context: Context) {

    private val NOTIFICATION_CHANNEL_ID = "1"
    private val SCREEN_ON_NOTIFICATION_ID = 1

    private var pendingIntentOpenApp: PendingIntent? = null
    private var notificationManager: NotificationManager? = null
    private var notificationManagerCompat: NotificationManagerCompat? = null

    init {
//        notificationManager = getNotificationManager()
        notificationManagerCompat = getNotificationManagerCompat()
        createNotificationChannel("Channel Name", "Channel Description", NOTIFICATION_CHANNEL_ID)
        pendingIntentOpenApp = pendingIntentOpenApp()
    }

//    fun getNotificationManager(): NotificationManager? {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Objects.requireNonNull(context)
//                .getSystemService(NotificationManager::class.java)
//        } else {
//            Log.d(this.javaClass.simpleName, "Android version needs to be Marshmallow (23) or higher")
//            null
//        }
//    }

    fun getNotificationManagerCompat(): NotificationManagerCompat {
        return NotificationManagerCompat.from(Objects.requireNonNull(context))
    }

    private fun pendingIntentOpenApp(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    private fun createNotificationChannel(
        channelName: String,
        channelDescription: String,
        notificationChannelId: String
    ) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                notificationChannelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.description = channelDescription
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    fun hideNotification() = notificationManagerCompat?.cancel(SCREEN_ON_NOTIFICATION_ID)

    fun showNotification(
        imageResId: Int?,
        notificationTitle: String,
        setContentText: String,
        notificationChannelId: String?
    ) {

        val IMAGE = android.R.drawable.ic_notification_overlay

        val notificationBuilder =
            NotificationCompat.Builder(context, notificationChannelId ?: NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(imageResId ?: IMAGE)
                .setContentTitle(notificationTitle)
                .setContentText(setContentText)
                .setDefaults(Notification.DEFAULT_ALL)
                .setOngoing(true)
                .addAction(
                    imageResId ?: IMAGE,
                    context.getString(R.string.open_app),
                    pendingIntentOpenApp
                )
                .setContentIntent(pendingIntentOpenApp)
                .setAutoCancel(false)

        notificationManagerCompat?.notify(
            SCREEN_ON_NOTIFICATION_ID,
            notificationBuilder.build()
        )
    }

}