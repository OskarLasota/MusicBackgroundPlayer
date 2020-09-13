package com.frezzcoding.musicplayer.presenter

import com.frezzcoding.musicplayer.contracts.MainContract
import com.frezzcoding.musicplayer.models.Song
import java.io.File
import javax.inject.Inject

class SongPresenter @Inject constructor(private val view : MainContract.View, private val model : MainContract.Model) : MainContract.Presenter{

    init{
        var songs = getAllSongs()
        compareToCache(songs)

    }

    override fun getAllSongs() : List<Song>{

        var listOfSongs : ArrayList<Song> = arrayListOf()
        var listOfFiles = File("/sdcard/Download")
        listOfFiles.listFiles()?.let { filelist ->
            for (file in filelist) {
                if (file.name.contains("mp3") || file.name.contains("mp4")) {
                    listOfSongs.add(Song(0, file.name, file))
                }
            }
        }

        return listOfSongs
    }

    private fun compareToCache(songs : List<Song>){
        //compare each song with the existing song in the cache then update view
        view.initView(songs)
    }


    override fun insertSong(song: Song) {
        TODO("Not yet implemented")
    }

}