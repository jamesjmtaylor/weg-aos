package com.jamesjmtaylor.weg2015

import androidx.lifecycle.LiveData
import okhttp3.*
import java.io.InputStream

fun setOfflineWebMock(json: String, app: App) {
    val mWeb = OkHttpClient.Builder()
            .addInterceptor(OfflineMockInterceptor(json))
            .build()
    app.setAppWebClient(mWeb)
}

val NO_DATA = "No data"
val <T> LiveData<T>.blockingValue: T?
    get() {
        var value: T? = null
        val latch = CountDownLatch(1)
        observeForever {
            value = it
            latch.countDown()
        }
        if (latch.await(20, TimeUnit.SECONDS)) return value
        else throw Exception("LiveData value was not set within 2 seconds")
    }

fun getJsonFromInputStream(input: InputStream?): String {
    if (input == null) {
        return ""
    }
    val sb = StringBuilder()
    try {
        System.out.println("Total file size to read (in bytes) : " + input.available())
        var content = input.read()
        while (content != -1) {
            sb.append(content.toChar())
            content = input.read()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        input.close()
    }
    return sb.toString()
}

class OfflineMockInterceptor(private val json: String) : Interceptor {
    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return Response.Builder()
                .body(ResponseBody.create(MEDIA_JSON, json))
                .request(chain.request())
                .message(json)
                .protocol(Protocol.HTTP_2)
                .code(200)
                .build()
    }

    val MEDIA_JSON = MediaType.parse("application/json")
}