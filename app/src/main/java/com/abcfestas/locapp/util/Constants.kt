package com.abcfestas.locapp.util

import android.content.Context
import java.io.InputStream
import java.util.Properties

object Constants {
    lateinit var API_BASE_URL: String
    lateinit var BASE_URL: String
    lateinit var SECURITY_TOKEN: String

    fun initialize(context: Context) {
        val inputStream: InputStream = context.assets.open("config.properties")
        val properties = Properties()
        properties.load(inputStream)

        API_BASE_URL = properties.getProperty("API_BASE_URL")
        BASE_URL = properties.getProperty("BASE_URL")
        SECURITY_TOKEN = properties.getProperty("SECURITY_TOKEN")
    }
}