package com.frezzcoding.musicplayer.models.db

import androidx.room.*
import com.frezzcoding.musicplayer.models.Song



@Dao
interface FileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(song : Song)

    @Delete
    fun delete(song : Song)

    @Query("select * from song_table")
    fun getSongs() : List<Song>

    @Query("DELETE from song_table")
    fun removeAllSongs()

    @Query("UPDATE song_table SET updatedName = :updatedName WHERE id = :id")
    fun updateSong(id : Int, updatedName : String)

    @Query("UPDATE song_table SET updatedName = :updatedName, deleted = :deleted WHERE id = :id")
    fun updateSongDeleted(id : Int, updatedName : String, deleted : Boolean)


}