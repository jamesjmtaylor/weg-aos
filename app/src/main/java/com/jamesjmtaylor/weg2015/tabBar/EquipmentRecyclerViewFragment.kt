package com.jamesjmtaylor.weg2015.tabBar

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.arch.lifecycle.LifecycleOwner
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jamesjmtaylor.weg2015.R

import com.jamesjmtaylor.weg2015.Models.Gun

import android.support.v7.util.DiffUtil

class EquipmentRecyclerViewFragment : Fragment(), LifecycleOwner {
    private var columnCount = 2
    var eVM : EquipmentViewModel? = null
    private var listener: OnListFragmentInteractionListener? = null
    private var adapter : EquipmentRecyclerViewAdapter? = null
    //MARK: Lifecycle Methods
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
            adapter = EquipmentRecyclerViewAdapter(this, listener)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        columnCount = 2
        initViewModel()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_equipment_list, container, false)
        if (view is RecyclerView) { // Set the adapter
            val context = view.getContext()
            view.layoutManager = GridLayoutManager(context, columnCount)
            view.adapter = adapter
        }
        return view
    }
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Gun) //TODO: Update item name
    }
    //MARK: ViewModel Methods
    var guns: List<Gun>? = null //Minimizes changes by allowing updates only to the ∆'s
    private fun initViewModel() {
        eVM = ViewModelProviders.of(this).get(EquipmentViewModel::class.java)
        eVM?.let { lifecycle.addObserver(it) } //Add ViewModel as an observer of this fragment's lifecycle
        eVM?.guns?.observe(this, equipmentObserver) //
        eVM?.initEquipment()
    }

    val equipmentObserver = Observer<List<Gun>> { newGuns ->
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {return guns?.size ?: 0}
            override fun getNewListSize(): Int {return newGuns?.size ?: 0}
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldId = guns?.let { it.get(oldItemPosition).id } ?: return false
                val newId = newGuns?.let { it.get(oldItemPosition).id } ?: return false
                return oldId == newId
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldTodo = guns?.get(oldItemPosition)  ?: return false
                val newTodo = newGuns?.get(newItemPosition)  ?: return false
                return oldTodo == newTodo
            }
        })
        result.dispatchUpdatesTo(adapter)

        adapter?.notifyDataSetChanged()
        guns = newGuns
    }
}
