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
    fun getSongs() : Song

    @Query("DELETE from song_table")
    fun removeAllSongs()

    @Query("UPDATE song_table SET id = :id")
    fun updateId(id : Int)



}