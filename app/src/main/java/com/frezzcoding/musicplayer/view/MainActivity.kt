package com.frezzcoding.musicplayer.view

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frezzcoding.musicplayer.R
import com.frezzcoding.musicplayer.contracts.MainContract
import com.frezzcoding.musicplayer.models.Song
import com.frezzcoding.musicplayer.view.adapters.MusicViewAdapter
import dagger.android.AndroidInjection
import java.io.File
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
    MusicViewAdapter.OnItemClickListener, MainContract.View {


    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var musicViewAdapter: MusicViewAdapter
    @Inject lateinit var presenter : MainContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidInjection.inject(this)

        /*
        TODO 3 buttons , pause start stop
        TODO play in background
        TODO allow name change of each song OR remove from the list - store in a roomdatabase
        TODO maybe make a list of songs users can download songs from
         */
        //permissions
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PackageManager.PERMISSION_GRANTED)

        setListeners()

    }

    private fun setListeners(){
        findViewById<Button>(R.id.btn_play).setOnClickListener {

        }
        findViewById<Button>(R.id.btn_restart).setOnClickListener {

        }
        findViewById<Button>(R.id.btn_pause).setOnClickListener {

        }
    }

    private fun playSong(song : File){
        mediaPlayer = MediaPlayer.create(this, Uri.fromFile(song))
        mediaPlayer.start()
    }



    override fun initView(list: List<Song>) {
        setAdapter(list)
    }

    private fun setAdapter(listOfSongs : List<Song>){
        musicViewAdapter =
            MusicViewAdapter(
                listOfSongs,
                this
            )
        var songlistview = findViewById<RecyclerView>(R.id.layout_songlist)
        songlistview.layoutManager = LinearLayoutManager(this)
        songlistview.adapter = musicViewAdapter
    }

    override fun updateScreenNewSong(list: List<Song>) {

    }

    override fun onItemClick(song: Song) {
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            presenter.getAllSongs()
        }else{
            Toast.makeText(this, "You need to give access to your storage", Toast.LENGTH_SHORT)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PackageManager.PERMISSION_GRANTED)
        }
    }



}