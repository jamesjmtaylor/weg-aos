package com.jamesjmtaylor.weg2015

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity


/**
 * Created by jtaylor on 2/11/18.
 */

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, TabBarActivity::class.java)
        startActivity(intent)
        finish()
    }
}