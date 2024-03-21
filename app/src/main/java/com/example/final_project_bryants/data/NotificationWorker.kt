package com.example.final_project_bryants.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.final_project_bryants.R
import com.example.final_project_bryants.ui.calendar.NotificationViewModel
import kotlinx.coroutines.flow.forEach
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationWorker (
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    private val notificationRepository = NotificationRepository(
        AppDatabase.getInstance(applicationContext)
            .notificationDao()
    )

    override suspend fun doWork(): Result {
        val notifications = notificationRepository.getAllNotifications()
        for (notification in notifications) {
            // get the time of the notification
            val notificationDate = notification.timeToShare.split("/")
            val month = notificationDate[0]
            val day = notificationDate[1]
            val year = notificationDate[2]

            Log.w("ShareDate", "WORKING...")

            // get the current day
            val simpleDate = SimpleDateFormat("mm:dd:yyyy", Locale.US)
            val currentMonth = simpleDate.format(Date()).split(":")[0]
            val currentDay = simpleDate.format(Date()).split(":")[1]
            val currentYear = simpleDate.format(Date()).split(":")[2]

            // if current time is equal or past notification time, trigger a notification
            if ((year.toInt() < currentYear.toInt())
                || ((year.toInt() == currentYear.toInt()) && (month.toInt() < currentMonth.toInt()))
                || ((year.toInt() == currentYear.toInt()) && (month.toInt() == currentMonth.toInt())
                        && (day.toInt() <= currentDay.toInt())))
            {
                sendNotification(notification.dateToShare)
                notificationRepository.deleteNotification(notification)
                Log.w("ShareDate", "Notification Sent")
            }
        }
        return Result.success()
    }

    private fun sendNotification(date: String) {
        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.nav_calendar)
            .createPendingIntent()


        val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.notification_channel)
        )
            .setSmallIcon(R.drawable.baseline_hourglass_bottom_24)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(applicationContext.getString(R.string.notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            //.setAutoCancel(true)


        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(applicationContext)
            .notify(date.hashCode(), builder.build())
    }
}