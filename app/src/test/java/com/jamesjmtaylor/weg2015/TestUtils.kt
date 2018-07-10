package com.jamesjmtaylor.weg2015

import android.arch.lifecycle.LiveData
import okhttp3.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun setOfflineWebMock(json: String){
    val mWeb = OkHttpClient.Builder()
            .addInterceptor(OfflineMockInterceptor(json))
            .build()
    WebClient.setInstance(mWeb)
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