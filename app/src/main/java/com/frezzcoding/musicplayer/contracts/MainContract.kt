package com.frezzcoding.musicplayer.contracts

import com.frezzcoding.musicplayer.models.Song
import java.io.File

interface MainContract {

    interface View {
        fun initView(list : List<Song>)
        fun notifyEmpty()
    }

    interface Presenter {
        fun getFileFromSong(song : Song) : File?
        fun getAllSongs()
        fun insertSong(song : Song)
        fun editSong(song : Song)
        fun hideSong(song : Song)
        fun getSongDuration(file : File) : Long
    }

    interface Model {
        fun getStoredSongs() : List<Song>
        fun insertSong(message : Song)
        fun removeSong(song : Song)
        fun editSong(song: Song)
    }

}