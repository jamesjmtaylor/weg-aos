package com.jamesjmtaylor.weg2015.tabBar.equipmentTabs

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.jamesjmtaylor.weg2015.R
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.deParcelizeEquipment

/**
 * A placeholder fragment containing a simple view.
 */
class EquipmentActivityFragment : Fragment() {
    private var equipment : Equipment? = null
    private var titleTextView : TextView? = null
    private var photoImageView : ImageView? = null
    private var groupImageView : ImageView? = null
    private var individualImageView : ImageView? = null
    private var detailLinearLayout : LinearLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        equipment = deParcelizeEquipment(arguments)
        val view = inflater.inflate(R.layout.fragment_equipment, container, false)
        titleTextView = view.findViewById(R.id.titleTextView)
        photoImageView = view.findViewById(R.id.photoImageView)
        groupImageView = view.findViewById(R.id.icon_group)
        individualImageView = view.findViewById(R.id.individualImageView)
        detailLinearLayout = view.findViewById(R.id.detailLinearLayout)

        return view

    }
}
