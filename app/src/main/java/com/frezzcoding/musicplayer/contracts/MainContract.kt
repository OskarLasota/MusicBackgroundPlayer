package com.frezzcoding.musicplayer.contracts

import com.frezzcoding.musicplayer.models.Song

interface MainContract {

    interface View {
        fun initView(list : List<Song>)
        fun updateScreenNewMessage(list : List<Song>)
    }

    interface Presenter {
        fun getAllSongs()
        fun insertSong(song : Song)
    }

    interface Model {
        fun getStoredSongs() : List<Song>
        fun insertSong(message : Song)
    }

}