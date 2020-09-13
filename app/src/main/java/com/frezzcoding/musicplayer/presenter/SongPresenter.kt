package com.frezzcoding.musicplayer.presenter

import com.frezzcoding.musicplayer.contracts.MainContract
import com.frezzcoding.musicplayer.models.Song
import java.io.File
import javax.inject.Inject

class SongPresenter @Inject constructor(private val view : MainContract.View, private val model : MainContract.Model) : MainContract.Presenter{

    init{
        getAllSongs()
    }

    override fun getAllSongs() {

        var listOfSongs : ArrayList<Song> = arrayListOf()
        var listOfFiles = File("/sdcard/Download")
        listOfFiles?.let {
            for (file in listOfFiles.listFiles()) {
                if (file.name.contains("mp3") || file.name.contains("mp4")) {
                    listOfSongs.add(Song(0, file.name, file))
                }
            }
        }

        view.initView(listOfSongs)
    }

    override fun insertSong(song: Song) {
        TODO("Not yet implemented")
    }

}