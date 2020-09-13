package com.frezzcoding.musicplayer.models

import com.frezzcoding.musicplayer.contracts.MainContract
import javax.inject.Inject

class SongModel @Inject constructor() : MainContract.Model {
    override fun getStoredSongs(): List<Song> {
        return arrayListOf()
    }

    override fun insertSong(message: Song) {

    }


}