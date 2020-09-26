package com.frezzcoding.musicplayer.view

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
    private lateinit var buttonLayout : ConstraintLayout
    private lateinit var btnPlay : FloatingActionButton
    private lateinit var btnStop : FloatingActionButton
    private lateinit var btnPause : FloatingActionButton
    @Inject lateinit var presenter : MainContract.Presenter
    private lateinit var currentSong: Song


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidInjection.inject(this)

        /*
        TODO play in background
        TODO maybe make a list of songs users can download songs from
        TODO when song finishes playing, change pause button to play
        TODO when stop is pressed, unselect the song OR hold a reference so when play is pressed then it works
        TODO PASTE YOUTUBE URL AND DOWNLOAD SONG
         */
        //permissions
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PackageManager.PERMISSION_GRANTED)

        init()
        setListeners()

    }
    private fun init(){
        buttonLayout = findViewById(R.id.layout_buttons)
        btnPlay = findViewById(R.id.btn_play)
        btnPause = findViewById(R.id.btn_pause)
        btnStop = findViewById(R.id.btn_stop)
    }

    private fun setListeners(){

        btnPlay.setOnClickListener {
            mediaPlayer?.let {
                mediaPlayer!!.start()
                if(mediaPlayer!!.isPlaying){
                    btnPlay.hide()
                    btnPause.show()
                }
            }
        }
        btnStop.setOnClickListener {
            mediaPlayer?.let {
                mediaPlayer!!.stop()
                mediaPlayer = MediaPlayer.create(this, Uri.fromFile(presenter.getFileFromSong(currentSong)))
                btnPlay.show()
            }
        }
        btnPause.setOnClickListener {
            mediaPlayer?.let {
                mediaPlayer!!.pause()
                btnPlay.show()
                btnPause.hide()
            }
        }
    }

    private fun playSong(song : Song){
        mediaPlayer?.let {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
        }
        mediaPlayer = MediaPlayer.create(this, Uri.fromFile(presenter.getFileFromSong(song)))
        mediaPlayer?.setOnCompletionListener{
            btnPause.hide()
            btnPlay.show()
        }
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
        currentSong = song
        showControlButtons()
    }

    override fun onEditClick(song: Song) {
        showPopup(song)
    }


    private fun showControlButtons(){
        buttonLayout.visibility = View.VISIBLE
        btnPause.show()
        btnPlay.hide()
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