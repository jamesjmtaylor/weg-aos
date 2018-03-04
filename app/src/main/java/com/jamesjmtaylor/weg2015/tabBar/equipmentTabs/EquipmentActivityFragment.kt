package com.jamesjmtaylor.weg2015.tabBar.equipmentTabs

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jamesjmtaylor.weg2015.R

/**
 * A placeholder fragment containing a simple view.
 */
class EquipmentActivityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_equipment, container, false)
    }
}
