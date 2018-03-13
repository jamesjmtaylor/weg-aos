package com.jamesjmtaylor.weg2015.tabBar.cardsTab

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jamesjmtaylor.weg2015.R
import com.jamesjmtaylor.weg2015.models.Equipment

class CardsFragment : Fragment() {
    var cVM : CardsViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVM()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cards, container, false)
    }

    override fun onResume() {
        super.onResume()
    }
    //MARK: ViewModel Methods
    private fun initVM() {
        cVM = activity?.let { ViewModelProviders.of(it).get(CardsViewModel::class.java)}
        cVM?.let { lifecycle.addObserver(it) } //Add ViewModel as an observer of this fragment's lifecycle
        cVM?.equipment?.observe(this, cardsObserver)
    }
    private val cardsObserver = Observer<List<Equipment>> { newEquipment ->

    }
}
