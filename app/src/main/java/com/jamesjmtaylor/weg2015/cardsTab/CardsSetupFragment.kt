package com.jamesjmtaylor.weg2015.cardsTab

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.jamesjmtaylor.weg2015.models.EquipmentType
import kotlinx.android.synthetic.main.fragment_cards_setup.*

/**
 * Created by jtaylor on 3/17/18.
 */
class CardsSetupFragment: Fragment(),LifecycleOwner {
    val TAG = "cardSetupFragment"
    //<editor-fold desc="Lifecycle Methods">
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVM()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        qtySeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener)
        startButton.setOnClickListener(onStartClickListener)
    }
    //</editor-fold>

    //<editor-fold desc="UI Listeners">
    val onStartClickListener = object : View.OnClickListener {
        override fun onClick(p0: View?) {
            val f = this@CardsSetupFragment
            when (f.radioGroup.checkedRadioButtonId){
                easyRadioButton.id -> cVM?.difficulty = Difficulty.EASY
                mediumRadioButton.id -> cVM?.difficulty = Difficulty.MEDIUM
                hardRadioButton.id -> cVM?.difficulty = Difficulty.HARD
            }
            if (f.landToggleButton.isActivated) cVM?.selectedTypes?.add(EquipmentType.LAND)
            if (f.airToggleButton.isActivated) cVM?.selectedTypes?.add(EquipmentType.AIR)
            if (f.seaToggleButton.isActivated) cVM?.selectedTypes?.add(EquipmentType.SEA)
            if (f.gunsToggleButton.isActivated) cVM?.selectedTypes?.add(EquipmentType.GUN)


        }
    }
    val onSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(p0: SeekBar?) {}
        override fun onStopTrackingTouch(p0: SeekBar?) {}
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            this@CardsSetupFragment.qtyTextView.text = p1.toString()
        }
    }
    //</editor-fold>

    //<editor-fold desc="VM Methods">
    private var cVM : CardsViewModel? = null
    fun initVM(){
        cVM = activity?.let { ViewModelProviders.of(it).get(CardsViewModel::class.java) }
        cVM?.let { lifecycle.addObserver(it) }
    }
    //</editor-fold>
}