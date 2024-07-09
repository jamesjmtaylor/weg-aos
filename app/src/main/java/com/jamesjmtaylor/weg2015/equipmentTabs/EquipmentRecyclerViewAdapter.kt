package com.jamesjmtaylor.weg2015.equipmentTabs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.jamesjmtaylor.weg2015.R
import com.jamesjmtaylor.weg2015.baseUrl
import com.jamesjmtaylor.weg2015.equipmentTabs.EquipmentRecyclerViewFragment.OnListFragmentInteractionListener
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.utils.openFile

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
        holder.item = equipment[position]
        holder.nameView.text = holder.item?.name
        val filepath = openFile(holder.item?.photoUrl)
        val image: Any = if (filepath?.exists() == true) filepath else baseUrl + holder.item?.photoUrl
        Glide.with(fragment)
                .load(image)
                .apply(RequestOptions()
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
        newEquipment?.let { this.equipment.addAll(it) }
        diffResult.dispatchUpdatesTo(this)
    }

    //MARK: - ViewHolder class
    inner class ViewHolder(val cellView: View) : RecyclerView.ViewHolder(cellView) {
        var nameView: TextView
        var photoView: ImageView
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
