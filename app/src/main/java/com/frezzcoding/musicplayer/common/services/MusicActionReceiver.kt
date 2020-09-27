package com.frezzcoding.musicplayer.common.services

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.frezzcoding.musicplayer.models.Song
import com.frezzcoding.musicplayer.view.MainActivity

class MusicActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        var action1 = intent?.getStringExtra("pause_button")
        var action2 = intent?.getStringExtra("play_button")
        var song = intent?.getSerializableExtra("song")
        action1?.let {

        }
        action2?.let {
            //service.playSong(song)
        }
    }



}