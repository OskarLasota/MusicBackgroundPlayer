package com.frezzcoding.musicplayer.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File
import java.io.Serializable

@Entity(tableName="song_table")
data class Song(@PrimaryKey var id: Int = 0, var name : String) : Serializable