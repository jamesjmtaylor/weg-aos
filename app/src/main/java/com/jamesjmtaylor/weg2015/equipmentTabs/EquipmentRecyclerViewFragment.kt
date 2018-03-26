package com.jamesjmtaylor.weg2015.equipmentTabs

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

import android.support.v7.widget.SearchView
import com.bumptech.glide.Glide
import com.jamesjmtaylor.weg2015.App
import com.jamesjmtaylor.weg2015.baseUrl
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.entities.Air
import com.jamesjmtaylor.weg2015.models.entities.Gun
import com.jamesjmtaylor.weg2015.models.entities.Land
import com.jamesjmtaylor.weg2015.models.entities.Sea
import com.jamesjmtaylor.weg2015.utils.NpaGridLayoutManager
import kotlinx.android.synthetic.main.fragment_equipment_list.*
import kotlinx.android.synthetic.main.fragment_equipment_list.view.*

class EquipmentRecyclerViewFragment : Fragment(), LifecycleOwner {
    val TAG = "equipmentRecyclerFrag"
    val QUERY_STRING_KEY = "QueryString"
    var eVM: EquipmentViewModel? = null
    private var columnCount = 2
    private var listener: OnListFragmentInteractionListener? = null
    var adapter: EquipmentRecyclerViewAdapter? = null
    private var recyclerView : RecyclerView? = null
    private var lastSearch : String? = null
    //MARK: Lifecycle Methods
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
            adapter = EquipmentRecyclerViewAdapter(this, listener)
        }
    }
    fun calculateNoOfColumns(context: Context?): Int {
        val displayMetrics = context?.getResources()?.getDisplayMetrics()
        val dpWidth = displayMetrics?.widthPixels ?: 0 / (displayMetrics?.density ?: return 0).toInt()
        val columnWidth = context?.resources?.getDimension(R.dimen.column_width) ?: return 0
        val noOfColumns = dpWidth / columnWidth + 1
        return noOfColumns.toInt()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        columnCount = calculateNoOfColumns(App.instance.applicationContext)
        initVM()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_equipment_list, container, false)
        recyclerView = view.recyclerList
        if (recyclerView is RecyclerView) { // Set the adapter
            val context = view.getContext()
            val params = GridLayoutManager.LayoutParams(GridLayoutManager.LayoutParams.MATCH_PARENT,GridLayoutManager.LayoutParams.MATCH_PARENT)
            recyclerView?.layoutManager = NpaGridLayoutManager(context, columnCount)
            recyclerView?.adapter = adapter
        }
        lastSearch = savedInstanceState?.get(QUERY_STRING_KEY) as? String
        return view
    }
    override fun onResume() {
        this.searchView.setOnQueryTextListener(searchViewListener)
        super.onResume()
        if (lastSearch?.isNotBlank() ?: false) {
            searchView?.setQuery(lastSearch, false)
            adapter?.updateAdapterWithNewList(eVM?.filterResults)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (searchView?.query?.isNotBlank() ?: false)
            outState.putString(QUERY_STRING_KEY, searchView?.query.toString())
    }
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    //MARK: ViewModel Methods
    private fun initVM() {
        eVM = activity?.let {ViewModelProviders.of(it).get(EquipmentViewModel::class.java)}
        eVM?.let { lifecycle.addObserver(it) } //Add ViewModel as an observer of this fragment's lifecycle
        eVM?.equipment?.observe(this, equipmentObserver)
    }


    //MARK: - Observers
    val equipmentObserver = Observer<List<Equipment>> { newEquipment ->
        adapter?.updateAdapterWithNewList(newEquipment)
        newEquipment ?: return@Observer
        for (equipment in newEquipment.iterator()) {
            if (equipment is Gun) {
                preLoadImage(equipment.individualIconUrl)
                preLoadImage(equipment.groupIconUrl)
            } else if (equipment is Land) {
                preLoadImage(equipment.individualIconUrl)
                preLoadImage(equipment.groupIconUrl)
            } else if (equipment is Sea) {
                preLoadImage(equipment.individualIconUrl)
            } else if (equipment is Air) {
                preLoadImage(equipment.groupIconUrl)
                preLoadImage(equipment.individualIconUrl)
            }
        }

    }

    fun preLoadImage(url: String?) {
        val target = Glide.with(App.instance)
                .downloadOnly()
                .load(baseUrl + url)
                .preload()
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
                it.name.toLowerCase().contains(query.toLowerCase()) == true
            } ?: ArrayList<Land>()
            eVM?.filterResults = filteredEquipment as ArrayList<Equipment>
            adapter?.updateAdapterWithNewList(filteredEquipment)
        }
    }
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Equipment)
    }
}
