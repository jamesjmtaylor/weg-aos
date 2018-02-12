package com.jamesjmtaylor.weg2015.tabBar

import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle

import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.jamesjmtaylor.weg2015.Models.Gun
import com.jamesjmtaylor.weg2015.R
import com.jamesjmtaylor.weg2015.tabBar.equipmentTabs.EquipmentRecyclerViewFragment
import kotlinx.android.synthetic.main.activity_nav.*

class TabBarActivity : AppCompatActivity(), LifecycleOwner, EquipmentRecyclerViewFragment.OnListFragmentInteractionListener {
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_land -> {
                message.setText(R.string.title_land)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_air -> {
                message.setText(R.string.title_air)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_sea -> {
                message.setText(R.string.title_sea)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_card -> {
                message.setText(R.string.title_card)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_calculator -> {
                message.setText(R.string.title_calculator)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
        val equipmentRecyclerViewFragment = EquipmentRecyclerViewFragment()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, equipmentRecyclerViewFragment, "TAG goes here")
                .commit()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onListFragmentInteraction(item: Gun) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
