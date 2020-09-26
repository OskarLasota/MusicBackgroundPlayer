package com.frezzcoding.musicplayer

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.frezzcoding.musicplayer.di.components.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var injector : DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = injector
    private val CHANNEL_ID = "musicService"

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder().app(this).build().inject(this)

        createNotificationChannel()

    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var serviceChannel = NotificationChannel(CHANNEL_ID, "Music in Foreground", NotificationManager.IMPORTANCE_LOW)

            var manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }


}