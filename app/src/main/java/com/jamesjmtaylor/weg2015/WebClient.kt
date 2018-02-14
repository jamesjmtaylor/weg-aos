package com.jamesjmtaylor.weg2015

import android.content.Context
import okhttp3.OkHttpClient

/**
 * Created by jtaylor on 2/14/18.
 */
abstract class WebClient : OkHttpClient() {
    companion object {
        private var INSTANCE: OkHttpClient? = null
        fun getInstance(): OkHttpClient {
            if (INSTANCE == null) {
                INSTANCE = OkHttpClient()
            }
            return INSTANCE as OkHttpClient
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
