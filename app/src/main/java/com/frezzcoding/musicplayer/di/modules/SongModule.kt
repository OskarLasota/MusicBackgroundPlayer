package com.frezzcoding.musicplayer.di.modules

import android.content.Context
import androidx.room.Room
import com.frezzcoding.musicplayer.App
import com.frezzcoding.musicplayer.contracts.MainContract
import com.frezzcoding.musicplayer.models.SongModel
import com.frezzcoding.musicplayer.models.db.AppDatabase
import com.frezzcoding.musicplayer.models.db.FileDao
import com.frezzcoding.musicplayer.presenter.SongPresenter
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [SongModule.SongBindings::class])
class SongModule {



    @Module
    interface SongBindings {
        @Binds
        fun bindChatPresenter(songPresenter : SongPresenter) : MainContract.Presenter

        @Binds
        fun bindModelState(songModel: SongModel) : MainContract.Model
    }


}