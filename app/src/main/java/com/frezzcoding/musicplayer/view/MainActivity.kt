package com.frezzcoding.musicplayer.view

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frezzcoding.musicplayer.R
import com.frezzcoding.musicplayer.contracts.MainContract
import com.frezzcoding.musicplayer.models.Song
import com.frezzcoding.musicplayer.view.adapters.MusicViewAdapter
import java.io.File

class MainActivity : AppCompatActivity(),
    MusicViewAdapter.OnItemClickListener, MainContract.View {


    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var musicViewAdapter: MusicViewAdapter
    private lateinit var listOfSongs : ArrayList<File>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /*
        TODO 3 buttons , pause start stop
        TODO play in background
        TODO allow name change of each song OR remove from the list - store in a roomdatabase
         */


        obtainSongs()
        setListeners()

    }

    private fun setListeners(){

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
        musicViewAdapter =
            MusicViewAdapter(
                listOfSongs,
                this
            )
        var songlistview = findViewById<RecyclerView>(R.id.layout_songlist)
        songlistview.layoutManager = GridLayoutManager(this, 1)
        songlistview.adapter = musicViewAdapter
    }

    override fun onItemClick(file: File) {
        showPopup()
    }


    private fun showPopup(){

        var dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_editname)

        var inputfield = dialog.findViewById<EditText>(R.id.et_newtitle)
        var removebutton = dialog.findViewById<Button>(R.id.btn_remove)
        var submitbutton = dialog.findViewById<Button>(R.id.btn_confirm)
        submitbutton.setOnClickListener {

        }
        removebutton.setOnClickListener {

        }


        dialog.show()


    }

    override fun initView(list: List<Song>) {


    }

    override fun updateScreenNewMessage(list: List<Song>) {
        TODO("Not yet implemented")
    }


}