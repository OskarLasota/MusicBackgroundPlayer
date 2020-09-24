package com.frezzcoding.musicplayer.di.modules

import android.content.Context
import androidx.room.Room
import com.frezzcoding.musicplayer.App
import com.frezzcoding.musicplayer.contracts.MainContract
import com.frezzcoding.musicplayer.models.db.AppDatabase
import com.frezzcoding.musicplayer.models.db.FileDao
import com.frezzcoding.musicplayer.view.MainActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
abstract class AppModule {

    @ContributesAndroidInjector(modules = [SongModule::class])
    abstract fun contributeMainActivity() : MainActivity

    @Binds
    abstract fun bindMainView(mainActivity: MainActivity) : MainContract.View

    @Binds
    abstract fun bindContext(context: App) : Context

    @Module
    companion object {

        @JvmStatic
        @Singleton
        @Provides
        fun provideAppDatabase(ctx : App) = Room.databaseBuilder(ctx, AppDatabase::class.java, "song_database").build()

        @JvmStatic
        @Provides
        fun provideFileDao(db : AppDatabase) = db.fileDao()


    }


}