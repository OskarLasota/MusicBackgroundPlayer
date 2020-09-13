package com.frezzcoding.musicplayer.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Entity(tableName="song_table")
class Song : Serializable {

    @PrimaryKey var id = 0
    var name = ""

}