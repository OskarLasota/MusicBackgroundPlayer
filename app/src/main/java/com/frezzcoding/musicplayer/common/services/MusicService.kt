package com.frezzcoding.musicplayer.common.services

import android.app.NotificationManager
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
import com.frezzcoding.musicplayer.view.callbacks.ServiceCallbacks
import java.io.File


class MusicService : Service() {


    private val CHANNEL_ID = "musicService"
    val binder: IBinder = MusicServiceBinder()
    private var mediaPlayer : MediaPlayer? = null
    private lateinit var callback : ServiceCallbacks
    var paused = false
    private var intent : Intent? = null
    private var currentsong : Song? = null

    inner class MusicServiceBinder : Binder(){
        val service: MusicService = this@MusicService
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        this.intent = intent
        sendNotification()
        return START_NOT_STICKY
    }

    fun sendNotification(){
        var data = intent?.getSerializableExtra("song") as Song
        val pauseIntent = Intent(this, MusicActionReceiver::class.java).apply {
            putExtra("pause_button", "pause")
            putExtra("song", currentsong ?: data)
        }
        val playIntent = Intent(this, MusicActionReceiver::class.java).apply {
            putExtra("play_button", "play")
            putExtra("song", currentsong ?: data)
        }
        var contentText = if(currentsong == null){
            if(data.updatedName.isNotEmpty()) data.updatedName else data.primaryname
        }else{
            if(currentsong!!.updatedName.isNotEmpty()) currentsong!!.updatedName else currentsong!!.primaryname
        }
        var notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Now Playing..")
            .setContentText(contentText)
            .setOngoing(true)
            .setAutoCancel(false)
            .addAction(R.mipmap.ic_launcher, "Pause", PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT))
            .addAction(R.mipmap.ic_launcher, "Play", PendingIntent.getBroadcast(this, 0, playIntent,  PendingIntent.FLAG_IMMUTABLE))
            .setSmallIcon(R.drawable.ic_edit)
            //.setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
    }

    fun updateNotification(){
        var data = intent?.getSerializableExtra("song") as Song
        val pauseIntent = Intent(this, MusicActionReceiver::class.java).apply {
            putExtra("pause_button", "pause")
            putExtra("song", currentsong ?: data)
        }
        val playIntent = Intent(this, MusicActionReceiver::class.java).apply {
            putExtra("play_button", "play")
            putExtra("song", currentsong ?: data)
        }
        var contentText = if(currentsong == null){
            if(data.updatedName.isNotEmpty()) data.updatedName else data.primaryname
        }else{
            if(currentsong!!.updatedName.isNotEmpty()) currentsong!!.updatedName else currentsong!!.primaryname
        }
        var notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Now Playing..")
            .setContentText(contentText)
            .setOngoing(true)
            .setAutoCancel(false)
           // .addAction(R.mipmap.ic_launcher, "Pause", PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT))
            //.addAction(R.mipmap.ic_launcher, "Play", PendingIntent.getBroadcast(this, 0, playIntent,  PendingIntent.FLAG_IMMUTABLE))
            .setSmallIcon(R.drawable.ic_music)
            //.setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
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
        currentsong = song
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
        mediaPlayer!!.release()
        super.onDestroy()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        mediaPlayer!!.release()
        return super.onUnbind(intent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }
}