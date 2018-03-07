package com.jamesjmtaylor.weg2015.tabBar.equipmentTabs

import android.opengl.Visibility
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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.jamesjmtaylor.weg2015.App
import com.jamesjmtaylor.weg2015.R
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType
import com.jamesjmtaylor.weg2015.models.deParcelizeEquipment
import com.jamesjmtaylor.weg2015.models.entities.Air
import com.jamesjmtaylor.weg2015.models.entities.Gun
import com.jamesjmtaylor.weg2015.models.entities.Land
import com.jamesjmtaylor.weg2015.models.entities.Sea

/**
 * A placeholder fragment containing a simple view.
 */
class EquipmentActivityFragment : Fragment() {
    private var titleTextView : TextView? = null
    private var photoImageView : ImageView? = null
    private var groupImageView : ImageView? = null
    private var individualImageView : ImageView? = null
    private var detailLinearLayout : LinearLayout? = null

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

        configureViewToEquipmentType(equipment)
        return view
    }
    fun configureViewToEquipmentType(item: Equipment?){
        if (item is Gun){
            setImage(groupImageView, item.groupIconUrl)
            setImage(individualImageView, item.individualIcon)

        } else if (item is Land) {
            setImage(groupImageView, item.groupIconUrl)
            setImage(individualImageView, item.individualIconUrl)
            setDetailViews(item)
        } else if (item is Sea) {
            setImage(individualImageView, item.individualIcon)
            groupImageView?.visibility = View.INVISIBLE
        } else if (item is Air) {
            setImage(groupImageView, item.groupIconUrl)
            setImage(individualImageView, item.individualIcon)
        }
    }
    fun setDetailViews(land: Land){
        val inflater = LayoutInflater.from(activity)
        for (i in 0..4){
            val detailRow = inflater.inflate(R.layout.row_detail,null, false)
            val titleTextView = detailRow.findViewById<TextView>(R.id.titleTextView)
            val valueTextView = detailRow.findViewById<TextView>(R.id.valueTextView)
            titleTextView.text = "Primary Weapon"
            valueTextView.text = land.primaryWeapon?.name
            detailLinearLayout?.addView(detailRow)
        }
    }
    fun setImage(imageView: ImageView?, imageUrl: String?){
        val view = imageView ?: return
        Glide.with(this)
                .load(App.instance.getString(R.string.base_url) + imageUrl)
                .apply(RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view)
    }
}
