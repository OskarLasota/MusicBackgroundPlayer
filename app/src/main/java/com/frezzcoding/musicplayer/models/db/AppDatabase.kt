package com.frezzcoding.musicplayer.models.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.frezzcoding.musicplayer.models.Song

@Database(entities = [Song::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun fileDao() : FileDao



    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "song_database").build()
        }
    }

}