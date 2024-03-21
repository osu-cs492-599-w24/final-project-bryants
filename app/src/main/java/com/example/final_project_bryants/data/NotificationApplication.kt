package com.example.final_project_bryants.data

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.final_project_bryants.R
import java.util.concurrent.TimeUnit

class NotificationApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        launchNotificationWorker()
    }

    private fun launchNotificationWorker() {
//        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
//            24,
//            TimeUnit.HOURS
//        ).build()
//        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
//            "NotificationWorker",
//            ExistingPeriodicWorkPolicy.KEEP,
//            workRequest
//        )
        val workRequest = OneTimeWorkRequest.from(NotificationWorker::class.java)
        WorkManager.getInstance(this).enqueue(workRequest)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.notification_channel),
                getString(R.string.notification_channel_title),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}