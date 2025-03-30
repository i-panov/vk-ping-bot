package ru.panov.vk_ping_bot

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.random.Random

class MyVkApiClient(private val accessToken: String) {
    companion object {
        const val API_END_POINT = "https://api.vk.com/method/"
        const val API_VERSION = 5.199
    }

    private val client = OkHttpClient()

    fun sendMessage(userId: Int, text: String) {
        val url = makeUrl("messages.send", mapOf(
            "peer_id" to userId,
            "message" to text,
            "random_id" to Random.nextInt(),
        ))

        val request = Request.Builder().get().url(url).build()

        client.newCall(request).execute()
    }

    private fun makeUrl(action: String, params: Map<String, Any>): HttpUrl {
        val builder = API_END_POINT.toHttpUrl().newBuilder()
            .addPathSegment(action)
            .addQueryParameter("v", API_VERSION.toString())
            .addQueryParameter("access_token", accessToken)

        for ((key, value) in params) {
            builder.addQueryParameter(key, value.toString())
        }

        return builder.build()
    }
}
