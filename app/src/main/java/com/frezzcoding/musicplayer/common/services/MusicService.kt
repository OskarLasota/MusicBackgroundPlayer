package com.frezzcoding.musicplayer.common.services

import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.frezzcoding.musicplayer.R
import com.frezzcoding.musicplayer.models.Song
import com.frezzcoding.musicplayer.view.MainActivity
import com.frezzcoding.musicplayer.view.callbacks.ServiceCallbacks
import java.io.File

class MusicService : Service() {


    private val CHANNEL_ID = "musicService"
    val binder: IBinder = MusicServiceBinder()
    private var mediaPlayer : MediaPlayer? = null
    private lateinit var callback : ServiceCallbacks
    private var paused = false

    inner class MusicServiceBinder : Binder(){
        val service: MusicService = this@MusicService
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var data = intent?.getSerializableExtra("song") as Song
        println("on start command")
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

    fun pauseSong(){
        mediaPlayer?.let {
            mediaPlayer!!.pause()
            paused = true
        }
    }
    fun stopSong(){
        mediaPlayer?.let {
            mediaPlayer!!.stop()
        }
    }

    fun isSongPlaying() : Boolean? {
        return mediaPlayer?.isPlaying
    }

    fun playSong(song : Song, file : File){
        if(paused){
            paused = false
        }else{
            mediaPlayer?.let {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                //unbindSafely()
            }
            mediaPlayer = MediaPlayer.create(this, Uri.fromFile(file))
            mediaPlayer?.setOnCompletionListener{
                callback.onSongCompletion()
            }
        }
        mediaPlayer!!.start()
    }

    fun setCallback(callback : ServiceCallbacks){
        this.callback = callback
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }
}