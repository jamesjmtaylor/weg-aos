package com.jamesjmtaylor.weg2015.equipmentTabs

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.jamesjmtaylor.weg2015.App
import com.jamesjmtaylor.weg2015.R
import com.jamesjmtaylor.weg2015.models.Equipment

import com.jamesjmtaylor.weg2015.equipmentTabs.EquipmentRecyclerViewFragment.OnListFragmentInteractionListener

class EquipmentRecyclerViewAdapter(private val fragment: EquipmentRecyclerViewFragment,
                                   private val listener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<EquipmentRecyclerViewAdapter.ViewHolder>() {
    //MARK: - Adapter methods
    private val equipment = mutableListOf<Equipment>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_equipment, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item =  equipment.get(position)
        holder.nameView.text = holder.item?.name
        Glide.with(fragment)
                .load(App.instance.getString(R.string.base_url) + holder.item?.photoUrl)
                .apply(RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.photoView)

        val item = holder.item ?: return
        holder.cellView.setOnClickListener {
            listener?.onListFragmentInteraction(item)
        }
    }
    override fun getItemCount(): Int {
        return equipment.size
    }
    fun updateAdapterWithNewList(newEquipment: List<Equipment>?) {
        //DifUtil below keeps shifts in the new loaded list to a minimum
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return equipment.size
            }
            override fun getNewListSize(): Int {
                return newEquipment?.size ?: 0
            }
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldId = equipment.get(oldItemPosition).id ?: return false
                val newId = newEquipment?.get(newItemPosition)?.id ?: return false
                return oldId == newId
            }
            //Items may have the same id, but their contents may have been updated
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldGun = equipment.get(oldItemPosition)
                val newGun = newEquipment?.get(newItemPosition) ?: return false
                return oldGun.equals(newGun)
            }
            //This allows you to introspect on what exactly changed and report it to the adapter as a bundle
            override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                return super.getChangePayload(oldItemPosition, newItemPosition)
            }
        })
        this.equipment.clear()
        this.equipment.addAll(newEquipment as List<Equipment>)
        diffResult.dispatchUpdatesTo(this)
    }
    //MARK: - ViewHolder class
    inner class ViewHolder(val cellView: View) : RecyclerView.ViewHolder(cellView) {
        var nameView : TextView
        var photoView : ImageView
        var item: Equipment? = null

        init {
            nameView = cellView.findViewById<View>(R.id.nameTextView) as TextView
            photoView = cellView.findViewById<View>(R.id.photoImageView) as ImageView
        }

        override fun toString(): String {
            return super.toString() + " '" + nameView.text + "'"
        }
    }
}