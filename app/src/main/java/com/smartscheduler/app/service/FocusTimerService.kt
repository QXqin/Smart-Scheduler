package com.smartscheduler.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.smartscheduler.app.domain.timer.FocusTimerManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class FocusTimerService : Service() {

    @Inject
    lateinit var timerManager: FocusTimerManager

    private val CHANNEL_ID = "focus_timer_channel"
    private val NOTIFICATION_ID = 1001
    
    private var serviceJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when (action) {
            ACTION_START -> startForegroundTimer()
            ACTION_STOP -> stopSelf()
        }
        return START_NOT_STICKY
    }

    private fun startForegroundTimer() {
        startForeground(NOTIFICATION_ID, buildNotification("Focusing..."))

        // Observe the timer state to update the notification
        serviceJob?.cancel()
        serviceJob = timerManager.timerState.onEach { state ->
            if (state.isRunning) {
                val mins = (state.timeRemainingMillis / 1000) / 60
                val secs = (state.timeRemainingMillis / 1000) % 60
                val timeStr = String.format("%02d:%02d", mins, secs)
                updateNotification("Time remaining: $timeStr")
            } else {
                stopSelf()
            }
        }.launchIn(CoroutineScope(Dispatchers.Main))
    }

    private fun updateNotification(text: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, buildNotification(text))
    }

    private fun buildNotification(text: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Smart Focus Timer")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Focus Timer",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows the remaining time for your active focus session"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob?.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}
