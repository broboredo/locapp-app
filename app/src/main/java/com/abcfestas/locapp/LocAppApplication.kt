package com.abcfestas.locapp

import android.app.Application
import com.abcfestas.locapp.di.AppModule
import com.abcfestas.locapp.di.AppModuleImpl

class LocAppApplication: Application() {
    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}