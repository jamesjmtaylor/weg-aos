package com.jamesjmtaylor.weg2015.tabBar.equipmentTabs

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

import com.jamesjmtaylor.weg2015.models.entities.Sea

import android.support.v7.widget.SearchView
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.entities.Land
import kotlinx.android.synthetic.main.fragment_equipment_list.view.*

class EquipmentRecyclerViewFragment : Fragment(), LifecycleOwner {
    val QUERY_STRING_KEY = "QueryString"
    var eVM : EquipmentViewModel? = null
    val TAG = "equipmentRecyclerFrag"
    private var columnCount = 2
    private var listener: OnListFragmentInteractionListener? = null
    private var adapter : EquipmentRecyclerViewAdapter? = null
    private var searchView: SearchView? = null
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
        initVM()
    }

    //MARK: ViewModel Methods
    private fun initVM() {
        val a = activity ?: return
        eVM = ViewModelProviders.of(a).get(EquipmentViewModel::class.java)
        eVM?.let { lifecycle.addObserver(it) } //Add ViewModel as an observer of this fragment's lifecycle
        eVM?.equipment?.observe(this, equipmentObserver)
        eVM?.initData()
    }
    //MARK: - Lifecycle Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_equipment_list, container, false)
        val recyclerView = view.recyclerList
        if (recyclerView is RecyclerView) { // Set the adapter
            val context = view.getContext()
            recyclerView.layoutManager = GridLayoutManager(context, columnCount)
            recyclerView.adapter = adapter
        }
        val searchView = view.searchView
        val lastSearch = savedInstanceState?.get(QUERY_STRING_KEY) as? String
        searchView.setOnQueryTextListener(searchViewListener)
        if (lastSearch?.isNotBlank() ?: false) searchView.setQuery(lastSearch,false)
        return view
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (searchView?.query?.isNotBlank() ?: false)
        outState.putString(QUERY_STRING_KEY,searchView?.query.toString())
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    //MARK: - Observers
    //TODO: Breakdown from specific object to equipment (data loss) occurs here.
    val equipmentObserver = Observer<List<Equipment>> { newEquipment ->
        adapter?.updateAdapterWithNewList(newEquipment)
    }

    //MARK: - Listener methods
    private val searchViewListener: SearchView.OnQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            filterEquipment(query)
            searchView?.clearFocus()
            return false
        }
        override fun onQueryTextChange(newText: String): Boolean {
            filterEquipment(newText)
            return true
        }
        private fun filterEquipment(query: String) {
            val filteredEquipment = eVM?.equipment?.value?.filter {
                it.name?.contains(query) == true
            } ?: ArrayList<Land>()
            adapter?.updateAdapterWithNewList(filteredEquipment)
        }
    }
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Equipment)
    }
}
