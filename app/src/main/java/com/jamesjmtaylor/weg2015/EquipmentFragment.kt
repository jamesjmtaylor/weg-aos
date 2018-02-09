package com.jamesjmtaylor.weg2015

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.jamesjmtaylor.weg2015.dummy.DummyContent
import com.jamesjmtaylor.weg2015.dummy.DummyContent.DummyItem

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class EquipmentFragment : Fragment() {
    // TODO: Customize parameters
    private var mColumnCount = 2
    private var mListener: OnListFragmentInteractionListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {mListener = context}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mColumnCount = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_equipment_list, container, false)
        if (view is RecyclerView) { // Set the adapter
            val context = view.getContext()
            view.layoutManager = GridLayoutManager(context, mColumnCount)
            view.adapter = WeaponRecyclerViewAdapter(DummyContent.ITEMS, mListener)
        }
        return view
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: DummyItem) //TODO: Update item name
    }
}
