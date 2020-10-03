package com.frezzcoding.musicplayer.models

import com.frezzcoding.musicplayer.contracts.MainContract
import com.frezzcoding.musicplayer.models.db.FileDao
import javax.inject.Inject

class SongModel @Inject constructor(var fileDao: FileDao) : MainContract.Model {


    override fun getStoredSongs(): List<Song> {
        return fileDao.getSongs()
    }

    override fun insertSong(song: Song) {
        fileDao.insert(song)
    }

    override fun removeSong(song: Song) {
        fileDao.updateSongDeleted(song.id, song.updatedName, true)
    }

    override fun editSong(song: Song) {
        fileDao.updateSong(song.id, song.updatedName)
    }


}