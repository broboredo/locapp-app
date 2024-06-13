package com.abcfestas.locapp

import android.app.Application
import com.abcfestas.locapp.di.AppModule
import com.abcfestas.locapp.di.AppModuleImpl
import com.abcfestas.locapp.util.Constants

class LocAppApplication: Application() {
    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
        Constants.initialize(this)
    }
}