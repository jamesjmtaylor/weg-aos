package com.jamesjmtaylor.weg2015

import okhttp3.OkHttpClient

fun setOfflineWebMock(json: String) {
    val mWeb = OkHttpClient.Builder()
            .addInterceptor(OfflineMockInterceptor(json))
            .build()
    WebClient.setInstance(mWeb)
}


