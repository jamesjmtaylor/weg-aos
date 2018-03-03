package com.jamesjmtaylor.weg2015.tabBar

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle

import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.jamesjmtaylor.weg2015.R
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType
import com.jamesjmtaylor.weg2015.tabBar.equipmentTabs.EquipmentActivity
import com.jamesjmtaylor.weg2015.tabBar.equipmentTabs.EquipmentRecyclerViewFragment
import com.jamesjmtaylor.weg2015.tabBar.equipmentTabs.EquipmentViewModel
import com.jamesjmtaylor.weg2015.tabBar.equipmentTabs.LoadingHudFragment
import kotlinx.android.synthetic.main.activity_nav.*

class TabBarActivity : AppCompatActivity(),
        LifecycleOwner,
        EquipmentRecyclerViewFragment.OnListFragmentInteractionListener {
    //MARK: - Lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVM()
        setContentView(R.layout.activity_nav)
        val equipmentRecyclerViewFragment = EquipmentRecyclerViewFragment()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.recyclerFrameLayout, equipmentRecyclerViewFragment, equipmentRecyclerViewFragment.TAG)
                .commit()
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun onListFragmentInteraction(item: Equipment) {
        val intent = Intent(applicationContext, EquipmentActivity::class.java)
        startActivity(intent)
    }

    //MARK: - Listener methods
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_land -> {
                eVM?.selectType(EquipmentType.LAND)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_air -> {
                eVM?.selectType(EquipmentType.AIR)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_sea -> {
                eVM?.selectType(EquipmentType.SEA)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_card -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_calculator -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    //MARK: - VM methods
    var eVM : EquipmentViewModel? = null
    private fun initVM() {
        eVM = ViewModelProviders.of(this).get(EquipmentViewModel::class.java)
        eVM?.let { lifecycle.addObserver(it) } //Add ViewModel as an observer of this fragment's lifecycle
        eVM?.isLoading?.observe(this, loadingObserver)
    }
    val loadingObserver = Observer<Boolean> { isLoading ->
        val topBackstackIndex = supportFragmentManager.getBackStackEntryCount() - 1
        val backstackEmpty = topBackstackIndex < 0
        val hudVisible = !backstackEmpty
                && supportFragmentManager.getBackStackEntryAt(0).name.equals("loadingHudFrag")
        if (isLoading ?: false && !hudVisible){//started loading
            val loadingHudFragment = LoadingHudFragment()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.hudFrameLayout, loadingHudFragment, loadingHudFragment.TAG)
                    .addToBackStack(loadingHudFragment.TAG)
                    .commit()
        } else if (hudVisible) {//finished loading
            supportFragmentManager.popBackStack()
        }
    }
}
