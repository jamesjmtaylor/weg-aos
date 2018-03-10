package com.jamesjmtaylor.weg2015.tabBar.equipmentTabs

import android.graphics.Paint
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
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
import android.graphics.Paint.UNDERLINE_TEXT_FLAG
import android.graphics.Typeface
import android.text.SpannableString
import com.jamesjmtaylor.weg2015.utils.boldString


/**
 * A placeholder fragment containing a simple view.
 */
class EquipmentActivityFragment : Fragment() {
    private var titleTextView : TextView? = null
    private var photoImageView : ImageView? = null
    private var groupImageView : ImageView? = null
    private var individualImageView : ImageView? = null
    private var detailLinearLayout : LinearLayout? = null
    private var descriptionTextView : TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val equipment = deParcelizeEquipment(arguments)
        val view = inflater.inflate(R.layout.fragment_equipment, container, false)

        titleTextView = view.findViewById(R.id.titleTextView)
        titleTextView?.text = equipment?.name
        photoImageView = view.findViewById(R.id.photoImageView)
        groupImageView = view.findViewById(R.id.groupImageView)
        individualImageView = view.findViewById(R.id.individualImageView)
        setImage(photoImageView, equipment?.photoUrl)
        detailLinearLayout = view.findViewById(R.id.detailLinearLayout)
        descriptionTextView = view.findViewById(R.id.descriptionTextView)

        configureViewToEquipmentType(equipment)
        return view
    }
    fun configureViewToEquipmentType(item: Equipment?){
        val description = boldString("Description:")
        if (item is Gun){
            setImage(groupImageView, item.groupIconUrl)
            setImage(individualImageView, item.individualIconUrl)
            setDetailViews(item)
            item.description?.let { descriptionTextView?.text = description.append(" $it") }
        } else if (item is Land) {
            setImage(groupImageView, item.groupIconUrl)
            setImage(individualImageView, item.individualIconUrl)
            setDetailViews(item)
            item.description?.let { descriptionTextView?.text = description.append(" $it") }
        } else if (item is Sea) {
            setImage(individualImageView, item.individualIconUrl)
            groupImageView?.visibility = View.INVISIBLE
            setDetailViews(item)
            item.description?.let { descriptionTextView?.text = description.append(" $it") }
        } else if (item is Air) {
            setImage(groupImageView, item.groupIconUrl)
            setImage(individualImageView, item.individualIconUrl)
            setDetailViews(item)
            item.description?.let { descriptionTextView?.text = description.append(" $it") }
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
            createDetailRow("Penetration",it.penetration.toString()+"mm")
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
            createDetailRow("Penetration",it.penetration.toString()+"mm")
        }
        sea.asm?.let{
            createDetailRow("Anti-Ship Missile",it.name, true,true)
            createDetailRow("Range",it.range.toString()+" meters")
            createDetailRow("Penetration",it.penetration.toString()+"mm")
        }
        sea.sam?.let {
            createDetailRow("Surface-to-Air Missile",it.name, true,true)
            createDetailRow("Range",it.range.toString()+" meters")
            createDetailRow("Altitude",it.altitude.toString()+"mm")
        }
        sea.torpedo?.let {
            createDetailRow("Torpedo",it.name, true,true)
            createDetailRow("Range",it.range.toString()+" meters")
        }
        sea.transports?.let {
            createDetailRow("Transports",it.toString(), true,true)
            createDetailRow("Quantity",sea.qty.toString())
        }
        if (sea.dive ?: 0 > 0) {createDetailRow("Maximum Depth",sea.dive.toString()+" meters", true,true)}
        sea.speed?.let { createDetailRow("Speed",it.toString()+" kph", true,true) }
        sea.auto?.let { createDetailRow("Autonomy",it.toString()+" km", true,true) }
        sea.tonnage?.let { createDetailRow("Tonnage",it.toString()+" tons", true,true) }
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
        val titleTextView = detailRow.findViewById<TextView>(R.id.titleTextView)
        val valueTextView = detailRow.findViewById<TextView>(R.id.valueTextView)
        if (underline) {
            titleTextView.setPaintFlags(titleTextView.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            valueTextView.setPaintFlags(valueTextView.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        }
        if (bold) {
            valueTextView.setTypeface(null, Typeface.BOLD);
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
