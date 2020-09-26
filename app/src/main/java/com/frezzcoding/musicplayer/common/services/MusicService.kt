package com.frezzcoding.musicplayer.common.services

import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.frezzcoding.musicplayer.R
import com.frezzcoding.musicplayer.models.Song
import com.frezzcoding.musicplayer.view.MainActivity

class MusicService : Service() {
    private val CHANNEL_ID = "musicService"
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var data = intent?.getSerializableExtra("song") as Song

       // var notificationIntent = Intent(this, MainActivity::class.java)
       // var pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val pauseIntent = Intent(this, MusicActionReceiver::class.java).apply {
            putExtra("pause_button", "pause")
            putExtra("song", data)
        }
        val playIntent = Intent(this, MusicActionReceiver::class.java).apply {
            putExtra("play_button", "play")
            putExtra("song", data)
        }

        var notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Now Playing..")
            .setContentText(if(data.updatedName.isNotEmpty()) data.updatedName else data.primaryname)
            .setOngoing(true)
            .setAutoCancel(false)
            .addAction(R.mipmap.ic_launcher, "Pause", PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT))
            .addAction(R.mipmap.ic_launcher, "Play", PendingIntent.getBroadcast(this, 0, playIntent,  PendingIntent.FLAG_IMMUTABLE))
            .setSmallIcon(R.drawable.ic_edit)
            //.setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}