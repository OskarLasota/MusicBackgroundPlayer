package com.frezzcoding.musicplayer.di.modules

import com.frezzcoding.musicplayer.contracts.MainContract
import com.frezzcoding.musicplayer.models.SongModel
import com.frezzcoding.musicplayer.presenter.SongPresenter
import dagger.Binds
import dagger.Module

@Module(includes = [SongModule.SongBindings::class])
class SongModule {

    //provides database here

    @Module
    interface SongBindings {
        @Binds
        fun bindChatPresenter(songPresenter : SongPresenter) : MainContract.Presenter

        @Binds
        fun bindModelState(songeModel: SongModel) : MainContract.Model
    }


}