package com.jamesjmtaylor.weg2015

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import com.jamesjmtaylor.weg2015.cardsTab.CardsSetupFragment
import com.jamesjmtaylor.weg2015.equipmentTabs.EquipmentActivity
import com.jamesjmtaylor.weg2015.equipmentTabs.EquipmentRecyclerViewFragment
import com.jamesjmtaylor.weg2015.equipmentTabs.EquipmentViewModel
import com.jamesjmtaylor.weg2015.equipmentTabs.LoadingHudFragment
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType
import com.jamesjmtaylor.weg2015.models.parcelizeEquipment
import com.jamesjmtaylor.weg2015.utils.Analytics
import kotlinx.android.synthetic.main.activity_nav.*

class TabBarActivity : AppCompatActivity(),
        LifecycleOwner,
        EquipmentRecyclerViewFragment.OnListFragmentInteractionListener {
    var equipmentRecyclerViewFragment: EquipmentRecyclerViewFragment? = null
    //MARK: - Lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVM()
        setContentView(R.layout.activity_nav)
        if (savedInstanceState == null) { //Prevents fragment from being instantiated when it already exists
            equipmentRecyclerViewFragment = EquipmentRecyclerViewFragment()
            equipmentRecyclerViewFragment?.let {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentFrameLayout, it, it.TAG)
                        .commit()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun onListFragmentInteraction(item: Equipment) {
        val intent = Intent(applicationContext, EquipmentActivity::class.java)
        parcelizeEquipment(item, intent)
        startActivity(intent)
    }

    //MARK: - Listener methods
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val searchView = equipmentRecyclerViewFragment?.view?.findViewById<SearchView>(R.id.searchView)
        searchView?.setQuery("", false) //Erases search on tab change
        var replacementFragment: Fragment? = null
        var replacementTag = ""
        when (item.itemId) {
            R.id.navigation_land -> {
                replacementFragment = EquipmentRecyclerViewFragment()
                replacementTag = replacementFragment.TAG
                eVM?.selectType(EquipmentType.LAND)
            }
            R.id.navigation_air -> {
                replacementFragment = EquipmentRecyclerViewFragment()
                replacementTag = replacementFragment.TAG
                eVM?.selectType(EquipmentType.AIR)
            }
            R.id.navigation_sea -> {
                replacementFragment = EquipmentRecyclerViewFragment()
                replacementTag = replacementFragment.TAG
                eVM?.selectType(EquipmentType.SEA)
            }
            R.id.navigation_card -> {
                replacementFragment = CardsSetupFragment()
                replacementTag = replacementFragment.TAG
            }
        }
        Analytics.saveTabView(item.title.toString())
        if (supportFragmentManager.findFragmentByTag(replacementTag) == null && replacementFragment != null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(fragmentFrameLayout.id, replacementFragment, replacementTag)
                    .commit()
        }
        return@OnNavigationItemSelectedListener true
    }

    //MARK: - VM methods
    var eVM: EquipmentViewModel? = null

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
        if (isLoading ?: false && !hudVisible) {//started loading
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
