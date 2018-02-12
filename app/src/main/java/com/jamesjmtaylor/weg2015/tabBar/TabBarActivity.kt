package com.jamesjmtaylor.weg2015.tabBar

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle

import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.jamesjmtaylor.weg2015.Models.Gun
import com.jamesjmtaylor.weg2015.R
import com.jamesjmtaylor.weg2015.tabBar.equipmentTabs.EquipmentRecyclerViewFragment
import com.jamesjmtaylor.weg2015.tabBar.equipmentTabs.EquipmentViewModel
import com.jamesjmtaylor.weg2015.tabBar.equipmentTabs.LoadingHudFragment
import kotlinx.android.synthetic.main.activity_nav.*

class TabBarActivity : AppCompatActivity(),
        LifecycleOwner,
        EquipmentRecyclerViewFragment.OnListFragmentInteractionListener {

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
        initVM()
        setContentView(R.layout.activity_nav)
        val equipmentRecyclerViewFragment = EquipmentRecyclerViewFragment()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, equipmentRecyclerViewFragment, equipmentRecyclerViewFragment.TAG)
                .commit()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }
    override fun onListFragmentInteraction(item: Gun) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    var eVM : EquipmentViewModel? = null
    private fun initVM() {
        eVM = ViewModelProviders.of(this).get(EquipmentViewModel::class.java)
        eVM?.let { lifecycle.addObserver(it) } //Add ViewModel as an observer of this fragment's lifecycle
        eVM?.equipment?.observe(this, loadingObserver)
//        eVM?.initData() //TODO: Not calling this causes Activity to never receive the observed ∆
    }
    val loadingObserver = Observer<List<Gun>> { equipment ->
        val isLoading = true
        val topBackstackIndex = supportFragmentManager.getBackStackEntryCount() - 1
        val backstackEmpty = topBackstackIndex < 0
        val hudVisible = !backstackEmpty
                && supportFragmentManager.getBackStackEntryAt(topBackstackIndex).equals("loadingHudFrag")
        if (isLoading ?: false && !hudVisible){//started loading
            val loadingHudFragment = LoadingHudFragment()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frameLayout, loadingHudFragment, loadingHudFragment.TAG)
                    .commit()
        } else if (hudVisible) {//finished loading
            supportFragmentManager.popBackStack()
        }
    }
}
