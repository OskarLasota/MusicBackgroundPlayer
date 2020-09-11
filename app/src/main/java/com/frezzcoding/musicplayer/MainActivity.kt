package com.frezzcoding.musicplayer

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.GridLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {


    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var musicViewAdapter: MusicViewAdapter
    private lateinit var listOfSongs : ArrayList<File>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /*
        TODO We want a recycler view with all listOfSongs
        TODO 3 buttons , pause start stop
        TODO play in background
         */

        obtainSongs()


    }

    private fun obtainSongs(){
        //permissions
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PackageManager.PERMISSION_GRANTED)

        listOfSongs = arrayListOf()
        var listOfFiles = File("/sdcard/Download")
        for(file in listOfFiles.listFiles()){
            if(file.name.contains("mp3") || file.name.contains("mp4")) {
                listOfSongs.add(file)
            }
        }
        setAdapter(listOfSongs)
    }

    private fun playSong(song : File){
        mediaPlayer = MediaPlayer.create(this, Uri.fromFile(song))
        mediaPlayer.start()
    }


    private fun setAdapter(listOfSongs : ArrayList<File>){
        musicViewAdapter = MusicViewAdapter(listOfSongs)
        var songlistview = findViewById<RecyclerView>(R.id.layout_songlist)
        songlistview.layoutManager = GridLayoutManager(this, 1)
        songlistview.adapter = musicViewAdapter
    }


}