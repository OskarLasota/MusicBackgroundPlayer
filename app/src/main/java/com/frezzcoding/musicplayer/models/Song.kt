package com.frezzcoding.musicplayer.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File
import java.io.Serializable

@Entity(tableName="song_table")
data class Song(@PrimaryKey(autoGenerate = true)
                var id: Int = 0,
                var primaryname : String,
                var updatedName: String = "",
                var duration : String = "00:01",
                var deleted : Boolean = false) : Serializable