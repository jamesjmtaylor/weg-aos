package com.jamesjmtaylor.weg2015.tabBar.equipmentTabs

import android.support.v7.util.DiffUtil
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
    //MARK: - Adapter methods
    private val equipment = mutableListOf<Gun>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_equipment, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem =  equipment.get(position)
        holder.mIdView.text = holder.mItem?.id.toString()
        holder.mContentView.text = holder.mItem?.name

        val item = holder.mItem ?: return
        holder.mView.setOnClickListener {
            listener?.onListFragmentInteraction(item)
        }
    }
    override fun getItemCount(): Int {
        return equipment.size
    }
    fun updateAdapterWithNewList(newGuns: List<Gun>?) {
        //DifUtil below keeps shifts in the new loaded list to a minimum
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return equipment.size
            }
            override fun getNewListSize(): Int {
                return newGuns?.size ?: 0
            }
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldId = equipment.get(oldItemPosition).id ?: return false
                val newId = newGuns?.get(newItemPosition)?.id ?: return false
                return oldId == newId
            }
            //Items may have the same id, but their contents may have been updated
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldGun = equipment.get(oldItemPosition)
                val newGun = newGuns?.get(newItemPosition) ?: return false
                return oldGun.equals(newGun)
            }
            //This allows you to introspect on what exactly changed and report it to the adapter as a bundle
            override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                return super.getChangePayload(oldItemPosition, newItemPosition)
            }
        })
        this.equipment.clear()
        this.equipment.addAll(newGuns as List<Gun>)
        diffResult.dispatchUpdatesTo(this)
    }
    //MARK: - ViewHolder class
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
