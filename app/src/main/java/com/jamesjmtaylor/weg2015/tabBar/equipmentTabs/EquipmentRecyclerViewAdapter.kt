package com.jamesjmtaylor.weg2015.tabBar.equipmentTabs

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jamesjmtaylor.weg2015.R

import com.jamesjmtaylor.weg2015.tabBar.equipmentTabs.EquipmentRecyclerViewFragment.OnListFragmentInteractionListener
import com.jamesjmtaylor.weg2015.Models.Gun

class EquipmentRecyclerViewAdapter(private val fragment: EquipmentRecyclerViewFragment,
                                   private val listener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<EquipmentRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_equipment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = fragment.guns?.get(position)
        holder.mIdView.text = fragment.guns?.get(position)?.id.toString()
        holder.mContentView.text = fragment.guns?.get(position)?.name

        val item = holder.mItem ?: return
        holder.mView.setOnClickListener {
            listener?.onListFragmentInteraction(item)
        }
    }

    override fun getItemCount(): Int {
        return fragment.guns?.size ?: 0
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView
        val mContentView: TextView
        var mItem: Gun? = null

        init {
            mIdView = mView.findViewById<View>(R.id.id) as TextView
            mContentView = mView.findViewById<View>(R.id.content) as TextView
        }

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
