package com.jamesjmtaylor.weg2015.equipmentTabs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jamesjmtaylor.weg2015.R
import kotlinx.android.synthetic.main.activity_equipment.*

class EquipmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val equipmentActivityFragment = EquipmentActivityFragment()
        equipmentActivityFragment.arguments = intent.extras
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentFrameLayout, equipmentActivityFragment)
                .commit()
    }

}
