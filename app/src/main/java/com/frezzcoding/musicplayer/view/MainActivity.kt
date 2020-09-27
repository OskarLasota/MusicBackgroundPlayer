package com.frezzcoding.musicplayer.view

import android.Manifest
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frezzcoding.musicplayer.R
import com.frezzcoding.musicplayer.common.services.MusicService
import com.frezzcoding.musicplayer.contracts.MainContract
import com.frezzcoding.musicplayer.models.Song
import com.frezzcoding.musicplayer.view.adapters.MusicViewAdapter
import com.frezzcoding.musicplayer.view.callbacks.ServiceCallbacks
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
    MusicViewAdapter.OnItemClickListener, MainContract.View, ServiceCallbacks {


    private var mediaPlayer : MediaPlayer? = null
    private lateinit var musicViewAdapter: MusicViewAdapter
    private lateinit var buttonLayout : ConstraintLayout
    private lateinit var btnPlay : FloatingActionButton
    private lateinit var btnStop : FloatingActionButton
    private lateinit var btnPause : FloatingActionButton
    @Inject lateinit var presenter : MainContract.Presenter
    private lateinit var currentSong : Song
    var service : MusicService? = null
    private var bounded = false
    val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidInjection.inject(this)

        /*
        TODO play in background
        TODO maybe make a list of songs users can download songs from
        TODO PASTE YOUTUBE URL AND DOWNLOAD SONG
         */

        //permissions
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PackageManager.PERMISSION_GRANTED)

        init()
        setListeners()

    }

    private var serviceConnection = object : ServiceConnection{
        override fun onServiceDisconnected(componentName: ComponentName) {
            println("service disconnected")
            service = null
        }

        override fun onServiceConnected(componentName: ComponentName
                                        , serviceBinder: IBinder
        ) {
            println("service connected")
            service = (serviceBinder as MusicService.MusicServiceBinder).service
            service!!.setCallback(this@MainActivity)
            service!!.playSong(currentSong, presenter.getFileFromSong(currentSong)!!)
        }
    }


    private fun startService(){
        var serviceIntent  = Intent(this, MusicService::class.java)
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        unbindSafely()
        super.onDestroy()
    }

    private fun unbindSafely(){
        if(bounded){
            unbindService(serviceConnection)
            bounded = false
        }
    }

    private fun init(){
        buttonLayout = findViewById(R.id.layout_buttons)
        btnPlay = findViewById(R.id.btn_play)
        btnPause = findViewById(R.id.btn_pause)
        btnStop = findViewById(R.id.btn_stop)
    }

    private fun setListeners(){

        btnPlay.setOnClickListener {
            if(!bounded){
                btnPlay.hide()
                btnPause.show()
                bounded = true
                //TODO call service method to play the song
            }
        }
        btnStop.setOnClickListener {
            if(bounded){
                //TODO call service method to stop the song
                btnPlay.show()
                //unbind?
            }
        }
        btnPause.setOnClickListener {
            //TODO call service method to stop the song
            btnPlay.show()
            btnPause.hide()
        }
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
        currentSong = song
        showControlButtons()
        if(service == null){
            startService()
        }else{
            service!!.playSong(currentSong, presenter.getFileFromSong(currentSong)!!)
        }
    }

    override fun onEditClick(song: Song) {
        showPopup(song)
    }


    override fun onSongCompletion() {
        //update ui and unbind
        btnPlay.show()
        btnPause.hide()
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