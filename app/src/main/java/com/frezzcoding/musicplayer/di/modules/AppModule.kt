package com.frezzcoding.musicplayer.di.modules

import com.frezzcoding.musicplayer.contracts.MainContract
import com.frezzcoding.musicplayer.view.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppModule {

    @ContributesAndroidInjector(modules = [SongModule::class])
    abstract fun contributeMainActivity() : MainActivity

    @Binds
    abstract fun bindMainView(mainActivity: MainActivity) : MainContract.View



}