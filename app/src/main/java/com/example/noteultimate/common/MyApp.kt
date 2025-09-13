package com.example.noteultimate.common


import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import java.io.File

@HiltAndroidApp
class MyApp: Application()
{
    override fun onCreate() {
        super.onCreate()

        //val dexOutputDir: File = codeCacheDir
        //dexOutputDir.setReadOnly()
    }
}