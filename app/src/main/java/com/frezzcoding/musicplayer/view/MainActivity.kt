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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frezzcoding.musicplayer.R
import com.frezzcoding.musicplayer.contracts.MainContract
import com.frezzcoding.musicplayer.models.Song
import com.frezzcoding.musicplayer.view.adapters.MusicViewAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
    MusicViewAdapter.OnItemClickListener, MainContract.View {


    private var mediaPlayer : MediaPlayer? = null
    private lateinit var musicViewAdapter: MusicViewAdapter
    @Inject lateinit var presenter : MainContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidInjection.inject(this)

        /*
        TODO play in background
        TODO maybe make a list of songs users can download songs from
         */
        //permissions
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PackageManager.PERMISSION_GRANTED)

        setListeners()

    }

    private fun setListeners(){
        findViewById<FloatingActionButton>(R.id.btn_play).setOnClickListener {
            mediaPlayer?.let {
                mediaPlayer!!.start()
            }
        }
        findViewById<FloatingActionButton>(R.id.btn_stop).setOnClickListener {
            mediaPlayer?.let {
                mediaPlayer!!.stop()
            }
        }
        findViewById<FloatingActionButton>(R.id.btn_pause).setOnClickListener {
            mediaPlayer?.let {
                mediaPlayer!!.pause()
            }
        }
    }

    private fun playSong(song : Song){
        mediaPlayer?.let {
            mediaPlayer!!.stop()
        }
        mediaPlayer = MediaPlayer.create(this, Uri.fromFile(presenter.getFileFromSong(song)))
        mediaPlayer!!.start()
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


    override fun onSongClick(song: Song) {
        //should show button layout with an animation on click
        playSong(song)
    }

    override fun onEditClick(song: Song) {
        showPopup(song)
    }


    private fun showPopup(song : Song){
        var dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_editname)

        var inputfield = dialog.findViewById<EditText>(R.id.et_newtitle)
        var removebutton = dialog.findViewById<Button>(R.id.btn_hidesong)
        var submitbutton = dialog.findViewById<Button>(R.id.btn_confirm)
        submitbutton.setOnClickListener {
            song.updatedName = inputfield.text.toString()
            presenter.editSong(song)
            dialog.dismiss()
        }
        removebutton.setOnClickListener {
            //should hide and not remove
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