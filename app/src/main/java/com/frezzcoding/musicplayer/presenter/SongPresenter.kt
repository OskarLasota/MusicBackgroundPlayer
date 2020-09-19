package com.frezzcoding.musicplayer.presenter

import com.frezzcoding.musicplayer.contracts.MainContract
import com.frezzcoding.musicplayer.models.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
                    listOfSongs.add(Song(0, file.name))
                }
            }
        }
        return listOfSongs
    }

    private fun compareToCache(songs : List<Song>){
       /*
        //compare each song with the existing song in the cache then update view
        CoroutineScope(Dispatchers.IO).launch {
            var cachedSongs = model.getStoredSongs()

            songs?.let {
                var checkedDeleted = false
                for(song in songs){
                    var found = false
                    cachedSongs.let {
                        for(cachedsong in cachedSongs){
                            if(song.file == cachedsong.file){
                                found = true
                            }
                            if(!checkedDeleted){
                                var cachedSongFound = false
                                for(songcheck in songs){
                                    if(cachedsong.file == songcheck.file){
                                        cachedSongFound = true
                                    }
                                }
                                if(!cachedSongFound){
                                    //remove from cache here if deleted from device
                                    model.removeSong(cachedsong)
                                }
                            }
                        }
                    }
                    checkedDeleted = true
                    if(!found){
                        //if song is not found then add it to cache
                        model.insertSong(song)
                    }
                }
            }


           view.initView(model.getStoredSongs())
        }

        */
    }


    override fun insertSong(song: Song) {
        TODO("Not yet implemented")
    }

}