package com.frezzcoding.musicplayer.view

import android.Manifest
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frezzcoding.musicplayer.R
import com.frezzcoding.musicplayer.common.services.MusicService
import com.frezzcoding.musicplayer.contracts.MainContract
import com.frezzcoding.musicplayer.models.Song
import com.frezzcoding.musicplayer.view.adapters.MusicViewAdapter
import com.frezzcoding.musicplayer.view.callbacks.ServiceCallbacks
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
    MusicViewAdapter.OnItemClickListener, MainContract.View, ServiceCallbacks {


    private var mediaPlayer : MediaPlayer? = null
    private lateinit var musicViewAdapter: MusicViewAdapter
    private lateinit var buttonLayout : ConstraintLayout
    private lateinit var btnPlay : FloatingActionButton
    private lateinit var btnStop : FloatingActionButton
    private lateinit var btnPause : FloatingActionButton
    private lateinit var seekbar : SeekBar
    @Inject lateinit var presenter : MainContract.Presenter
    private lateinit var currentSong : Song
    private lateinit var mHandler : Handler
    var service : MusicService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidInjection.inject(this)

        /*
        TODO play in background
        TODO maybe make a list of songs users can download songs from
        TODO PASTE YOUTUBE URL AND DOWNLOAD SONG
        TODO MAKE SURE NO MEMORY LEAK
         */

        //permissions
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PackageManager.PERMISSION_GRANTED)

        init()
        setListeners()
        createAd()

    }


    private fun createAd(){
        val extras = Bundle()
        extras.putString("max_ad_content_rating", "G")
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter::class.java, extras).tagForChildDirectedTreatment(true).build()

        ad_schedule.adListener  = object: AdListener(){
            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
                println("ad failed")
                ad_layout.visibility = View.GONE
            }
            override fun onAdLoaded() {
                println("ad loaded")
                ad_layout.visibility = View.VISIBLE
            }


        }

        ad_schedule.loadAd(adRequest)

    }

    private var serviceConnection = object : ServiceConnection{
        override fun onServiceDisconnected(componentName: ComponentName) {
            service = null
        }

        override fun onServiceConnected(componentName: ComponentName
                                        , serviceBinder: IBinder
        ) {
            service = (serviceBinder as MusicService.MusicServiceBinder).service
            service!!.setCallback(this@MainActivity)
            service!!.playSong(currentSong, presenter.getFileFromSong(currentSong)!!)
            handleSeekbarProgress()
        }
    }


    private fun startService(){
        var serviceIntent  = Intent(this, MusicService::class.java).also {
            it.putExtra("song", currentSong)
        }
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        startService(serviceIntent)
    }

    override fun onDestroy() {
        unbindSafely()
        super.onDestroy()
    }

    private fun unbindSafely(){
        if(serviceConnection != null) {
            unbindService(serviceConnection)
        }
        service!!.stopSelf()
    }

    private fun init(){
        buttonLayout = findViewById(R.id.layout_buttons)
        btnPlay = findViewById(R.id.btn_play)
        btnPause = findViewById(R.id.btn_pause)
        btnStop = findViewById(R.id.btn_stop)
        seekbar = findViewById(R.id.seekbar_song)

    }

    private fun setListeners(){
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    service!!.updateSeek(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
               //
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //
            }

        })
        btnPlay.setOnClickListener {
            btnPlay.hide()
            btnPause.show()
            service!!.playSong(currentSong, presenter.getFileFromSong(currentSong)!!)
            seekbar.visibility = View.VISIBLE
            handleSeekbarProgress()
        }
        btnStop.setOnClickListener {
            service!!.isSongPlaying()?.let {
                service!!.stopSong()
                btnPlay.show()
                mHandler.removeCallbacksAndMessages(null)
            }
        }
        btnPause.setOnClickListener {
            service!!.isSongPlaying()?.let {
                btnPlay.show()
                btnPause.hide()
                service!!.pauseSong()
                mHandler.removeCallbacksAndMessages(null)
            }
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
        seekbar.visibility = View.VISIBLE
        if(service == null){
            startService()
        }else{
            service!!.paused = false
            service!!.playSong(currentSong, presenter.getFileFromSong(currentSong)!!)
            service!!.updateNotification()
            handleSeekbarProgress()
        }
        seekbar.max = presenter.getSongDuration(presenter.getFileFromSong(currentSong)!!).toInt()

    }

    private fun handleSeekbarProgress(){
        mHandler = Handler()
        //Make sure you update Seekbar on UI thread
        runOnUiThread(object : Runnable {
            override fun run() {
                println("run")
                val mCurrentPosition: Int = service!!.getSongProgress()
                println(mCurrentPosition)
                seekbar.progress = mCurrentPosition
                mHandler.postDelayed(this, 1000)
            }
        })
    }

    override fun onEditClick(song: Song) {
        showPopup(song)
    }


    override fun onSongCompletion() {
        //update ui and unbind
        btnPlay.show()
        btnPause.hide()
        seekbar.visibility = View.GONE
    }

    override fun onSongEnd() {
        seekbar.visibility = View.GONE
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