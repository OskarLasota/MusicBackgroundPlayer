package com.frezzcoding.musicplayer.di.components

import com.frezzcoding.musicplayer.App
import com.frezzcoding.musicplayer.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(application: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun app(application: App) : Builder
        fun build() : AppComponent
    }

}