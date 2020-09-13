package com.frezzcoding.musicplayer.models.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.frezzcoding.musicplayer.models.Song

@Database(entities = [Song::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun fileDao() : FileDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "song_database"
                    ).build()
                }
                return instance
            }
        }
    }
}