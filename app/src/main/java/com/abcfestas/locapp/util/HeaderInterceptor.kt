package com.abcfestas.locapp.util

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        Log.d("LOG: Security-token", Constants.SECURITY_TOKEN)
        val modifiedRequest = originalRequest.newBuilder()
            .header("Security-token", Constants.SECURITY_TOKEN)
            .build()
        return chain.proceed(modifiedRequest)
    }
}