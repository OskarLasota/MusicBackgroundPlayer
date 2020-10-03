package com.frezzcoding.musicplayer

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
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
        checkForUpdates()

    }

    private fun checkForUpdates(){
        val versionCode = BuildConfig.VERSION_CODE
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("shared preferences", Context.MODE_PRIVATE)

        val json = sharedPreferences.getInt("version", 0)
        if(json == 0){
            val editor = sharedPreferences.edit()
            editor.putInt("version", versionCode)
            editor.apply()
        }else{
            if(json != versionCode){
                val editor = sharedPreferences.edit()
                editor.putInt("version", versionCode)
                editor.apply()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.frezzcoding.musicplayer")).also {
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            }
        }
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var serviceChannel = NotificationChannel(CHANNEL_ID, "Music in Foreground", NotificationManager.IMPORTANCE_LOW)

            var manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }


}