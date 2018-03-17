package com.jamesjmtaylor.weg2015.equipmentTabs

import android.graphics.Paint
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.jamesjmtaylor.weg2015.App
import com.jamesjmtaylor.weg2015.R
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.deParcelizeEquipment
import com.jamesjmtaylor.weg2015.models.entities.Air
import com.jamesjmtaylor.weg2015.models.entities.Gun
import com.jamesjmtaylor.weg2015.models.entities.Land
import com.jamesjmtaylor.weg2015.models.entities.Sea
import android.graphics.Typeface
import com.jamesjmtaylor.weg2015.utils.boldString
import kotlinx.android.synthetic.main.fragment_equipment.*


/**
 * A placeholder fragment containing a simple view.
 */
class EquipmentActivityFragment : Fragment() {
    var equipment : Equipment? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        equipment = deParcelizeEquipment(arguments)
        val view = inflater.inflate(R.layout.fragment_equipment, container, false)

        return view
    }


    override fun onResume() {
        super.onResume()
        this.titleTextView.text = equipment?.name
        setImage(this.photoImageView, equipment?.photoUrl)
        configureViewToEquipmentType(equipment)
    }
    fun configureViewToEquipmentType(item: Equipment?){
        val description = boldString("Description:")
        if (item is Gun){
            setImage(this.groupImageView, item.groupIconUrl)
            setImage(this.individualImageView, item.individualIconUrl)
            setDetailViews(item)
            item.description?.let { this.descriptionTextView?.text = description.append(" $it") }
        } else if (item is Land) {
            setImage(this.groupImageView, item.groupIconUrl)
            setImage(this.individualImageView, item.individualIconUrl)
            setDetailViews(item)
            item.description?.let { this.descriptionTextView?.text = description.append(" $it") }
        } else if (item is Sea) {
            setImage(this.individualImageView, item.individualIconUrl)
            groupImageView?.visibility = View.INVISIBLE
            setDetailViews(item)
            item.description?.let { this.descriptionTextView?.text = description.append(" $it") }
        } else if (item is Air) {
            setImage(this.groupImageView, item.groupIconUrl)
            setImage(this.individualImageView, item.individualIconUrl)
            setDetailViews(item)
            item.description?.let { this.descriptionTextView?.text = description.append(" $it") }
        }
    }
    fun setDetailViews(gun: Gun){
            createDetailRow("Range",gun.range.toString()+" meters")
        if (gun.penetration ?: 0 > 0 ) {createDetailRow("Range",gun.range.toString()+" meters")}
        if (gun.altitude ?: 0 > 0 ) {createDetailRow("Altitude",gun.penetration.toString()+" meters")}
        if (gun.penetration ?: 0 > 0 ) {createDetailRow("Penetration",gun.penetration.toString()+"mm")}
    }
    fun setDetailViews(land: Land){
        land.primaryWeapon?.let{
            createDetailRow("Primary Weapon",it.name, true,true)
            createDetailRow("Range",it.range.toString()+" meters")
            if (it.altitude ?: 0 > 0){createDetailRow("Altitude",it.altitude.toString()+" meters") }
            if (it.penetration ?: 0 > 0){createDetailRow("Penetration",it.penetration.toString()+"mm") }
        }
        land.secondaryWeapon?.let{
            createDetailRow("Secondary Weapon",it.name, true,true)
            createDetailRow("Range",it.range.toString()+" meters")
            if (it.altitude ?: 0 > 0){createDetailRow("Altitude",it.altitude.toString()+" meters") }
            if (it.penetration ?: 0 > 0){createDetailRow("Penetration",it.penetration.toString()+"mm") }
        }
        land.atgm?.let {
            createDetailRow("ATGM",it.name, true,true)
            createDetailRow("Range",it.range.toString()+" meters")
            createDetailRow("Penetration",it.penetration.toString()+"mm")
        }
        land.armor?.let { createDetailRow("Armor",it.toString()+"mm", true,true) }
        land.speed?.let { createDetailRow("Speed",it.toString()+" kph", true,true) }
        land.auto?.let { createDetailRow("Autonomy",it.toString()+" km", true,true) }
        land.weight?.let { createDetailRow("Weight",it.toString()+" tons", true,true) }
    }
    fun setDetailViews(sea: Sea){
        sea.gun?.let{
            createDetailRow("Deck Gun",it.name, true,true)
            createDetailRow("Range",it.range.toString()+" meters")
            if (it.altitude ?: 0 > 0){createDetailRow("Altitude",it.altitude.toString()+" meters") }
            if (it.penetration ?: 0 > 0){createDetailRow("Penetration",it.penetration.toString()+"mm") }
        }
        sea.asm?.let{
            createDetailRow("Anti-Ship Missile",it.name, true,true)
            createDetailRow("Range",it.range.toString()+" meters")
            if (it.altitude ?: 0 > 0){createDetailRow("Altitude",it.altitude.toString()+" meters") }
            if (it.penetration ?: 0 > 0){createDetailRow("Penetration",it.penetration.toString()+"mm") }
        }
        sea.sam?.let {
            createDetailRow("Surface-to-Air Missile",it.name, true,true)
            createDetailRow("Range",it.range.toString()+" meters")
            if (it.altitude ?: 0 > 0){createDetailRow("Altitude",it.altitude.toString()+" meters") }
            if (it.penetration ?: 0 > 0){createDetailRow("Penetration",it.penetration.toString()+"mm") }
        }
        sea.torpedo?.let {
            createDetailRow("Torpedo",it.name, true,true)
            createDetailRow("Range",it.range.toString()+" meters")
        }
        if (!(sea.transports.isNullOrBlank() || sea.transports?.contains("null") ?: true)){
            createDetailRow("Transports",sea.transports.toString(), true,true)
            if (sea.qty ?: 0 > 0){createDetailRow("Quantity",sea.qty.toString()) }
        }
        if (sea.dive ?: 0 > 0) {createDetailRow("Maximum Depth",sea.dive.toString()+" meters", true,true)}
        sea.speed?.let { createDetailRow("Speed",it.toString()+" kph", true,true) }
        sea.auto?.let { createDetailRow("Autonomy",it.toString()+" km", true,true) }
        sea.tonnage?.let { createDetailRow("Displacement",it.toString()+" tons", true,true) }
    }
    fun setDetailViews(air: Air){
        air.gun?.let{
            createDetailRow("Cannon",it.name, true,true)
            createDetailRow("Range",it.range.toString()+" meters")
            if (it.penetration ?: 0 > 0){createDetailRow("Penetration",it.penetration.toString()+"mm") }
        }
        air.agm?.let{
            createDetailRow("Air-to-Ground Missile",it.name, true,true)
            createDetailRow("Range",it.range.toString()+" meters")
            createDetailRow("Penetration",it.penetration.toString()+"mm")
        }
        air.asm?.let {
            createDetailRow("Air-to-Surface Missile",it.name, true,true)
            createDetailRow("Range",it.range.toString()+" meters")
        }
        air.aam?.let {
            createDetailRow("Air-to-Air Missile",it.name, true,true)
            createDetailRow("Range",it.range.toString()+" meters")
        }
        air.speed?.let { createDetailRow("Speed",it.toString()+" kph", true,true) }
        air.ceiling?.let { createDetailRow("Ceiling",it.toString()+" meters", true,true) }
        air.auto?.let { createDetailRow("Autonomy",it.toString()+" km", true,true) }
        air.weight?.let { createDetailRow("Weight",it.toString()+" kg", true,true) }
    }
    fun createDetailRow(title: String, value: String, underline: Boolean = false, bold: Boolean = false){
        val inflater = LayoutInflater.from(activity)
        val detailRow = inflater.inflate(R.layout.row_detail,null, false)
        val titleTextView = detailRow.findViewById<TextView>(R.id.rowTitleTextView)
        val valueTextView = detailRow.findViewById<TextView>(R.id.rowValueTextView)
        if (underline) {
            titleTextView.setPaintFlags(titleTextView.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            valueTextView.setPaintFlags(valueTextView.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        }
        if (bold) {
            valueTextView.setTypeface(null, Typeface.BOLD)
        }
        titleTextView.text = title
        valueTextView.text = value
        detailLinearLayout?.addView(detailRow)

    }
    fun setImage(imageView: ImageView?, imageUrl: String?){
        val view = imageView ?: return
        Glide.with(this)
                .load(App.instance.getString(R.string.base_url) + imageUrl)
                .apply(RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter())
                .into(view)
    }
}
