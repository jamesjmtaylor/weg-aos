package com.jamesjmtaylor.weg2015.tabBar

import android.arch.lifecycle.LiveData
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jamesjmtaylor.weg2015.R

import com.jamesjmtaylor.weg2015.tabBar.EquipmentFragment.OnListFragmentInteractionListener
import com.jamesjmtaylor.weg2015.Models.Gun

class WeaponRecyclerViewAdapter(private val mValues: LiveData<List<Gun>>, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<WeaponRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_equipment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.mIdView.text = mValues[position].id.toString()
        holder.mContentView.text = mValues[position].name

        val item = holder.mItem ?: return
        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
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
