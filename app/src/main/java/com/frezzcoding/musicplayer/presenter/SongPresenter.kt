package com.frezzcoding.musicplayer.presenter

import com.frezzcoding.musicplayer.contracts.MainContract
import com.frezzcoding.musicplayer.models.Song
import javax.inject.Inject

class SongPresenter @Inject constructor() : MainContract.Presenter{
    override fun getAllSongs() {

    }

    override fun insertSong(song: Song) {
        TODO("Not yet implemented")
    }

}