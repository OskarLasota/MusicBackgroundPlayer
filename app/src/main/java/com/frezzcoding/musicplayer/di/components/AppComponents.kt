package com.frezzcoding.musicplayer.di.components

import com.frezzcoding.musicplayer.App
import com.frezzcoding.musicplayer.di.modules.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(application: App)

}