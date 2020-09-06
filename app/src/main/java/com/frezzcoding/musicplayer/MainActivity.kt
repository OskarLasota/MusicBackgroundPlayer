package com.frezzcoding.musicplayer

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.File

class MainActivity : AppCompatActivity() {


    private lateinit var mediaPlayer : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PackageManager.PERMISSION_GRANTED)

        var listOfFiles = File("/sdcard/Download")
        var listOfSongs : ArrayList<File> = arrayListOf()
        for(file in listOfFiles.listFiles()){
            if(file.name.contains("mp3") || file.name.contains("mp4")) {
                listOfSongs.add(file)
                println(file.path)
            }
        }

        /*
        TODO We want a recycler view with all listOfSongs
        TODO 3 buttons , pause start stop
        TODO play in background
         */


        mediaPlayer = MediaPlayer.create(this, Uri.fromFile(listOfSongs[1]))
        mediaPlayer.start()
        println("soungs found ${listOfSongs.size}")


    }
}