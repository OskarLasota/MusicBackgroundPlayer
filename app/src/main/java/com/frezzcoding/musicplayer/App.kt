package com.frezzcoding.musicplayer

import android.app.Activity
import android.app.Application
import com.frezzcoding.musicplayer.di.components.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var injector : DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = injector

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder().build().inject(this)
    }


}