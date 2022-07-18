package com.flourenco.movieschallenge.core.network.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApiInterceptor(
    private val apiKey: String
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val signedRequest = chain.request().signRequest()
            return chain.proceed(signedRequest)
        } catch (e: Exception) {
            throw e
        }
    }

    private fun Request.signRequest(): Request {
        return newBuilder()
            .header("Content-Type", "application/json")
            .url(url.toString().replace("API_KEY", apiKey))
            .build()
    }
}