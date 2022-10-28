package com.jamesjmtaylor.weg2015.cardsTab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jamesjmtaylor.weg2015.R
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType
import kotlinx.android.synthetic.main.activity_equipment.*
import kotlinx.android.synthetic.main.fragment_cards_setup.*

/**
 * Created by jtaylor on 3/17/18.
 */
class CardsSetupFragment : Fragment(), LifecycleOwner {
    val TAG = "cardSetupFragment"

    //<editor-fold desc="Lifecycle Methods">
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVM()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cards_setup, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        qtySeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener)
        landToggleButton.setOnClickListener(onToggleListener)
        seaToggleButton.setOnClickListener(onToggleListener)
        airToggleButton.setOnClickListener(onToggleListener)
        gunsToggleButton.setOnClickListener(onToggleListener)
        startButton.setOnClickListener(onStartClickListener)
    }
    //</editor-fold>

    //<editor-fold desc="UI Listeners">
    private val onToggleListener = View.OnClickListener { configureVmCardsAndGetTotalChoices() }
    private val onStartClickListener = object : View.OnClickListener {
        override fun onClick(p0: View?) {
            if (configureVmCardsAndGetTotalChoices() == 0) {
                Toast.makeText(
                    this@CardsSetupFragment.context,
                    getString(R.string.no_flashcards_error),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val frameLayout = activity?.fragmentFrameLayout?.id ?: return
                val cardsFragment = CardsFragment()
                cardsFragment.cVM = cVM
                val transaction = activity?.supportFragmentManager ?: return
                transaction.beginTransaction()
                    .replace(frameLayout, cardsFragment, cardsFragment.TAG).commit()
            }
        }
    }

    private val onSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(p0: SeekBar?) {}
        override fun onStopTrackingTouch(p0: SeekBar?) {}
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            val progress = Math.max(p1 / 10 * 10, 10)
            qtySeekBar.progress = progress
            this@CardsSetupFragment.qtyTextView.text = "$progress Cards"
        }
    }

    private fun configureVmCardsAndGetTotalChoices(): Int {
        val f = this@CardsSetupFragment
        when (f.radioGroup.checkedRadioButtonId) {
            easyRadioButton.id -> cVM?.difficulty = Difficulty.EASY
            mediumRadioButton.id -> cVM?.difficulty = Difficulty.MEDIUM
            hardRadioButton.id -> cVM?.difficulty = Difficulty.HARD
        }
        cVM?.selectedTypes?.removeAll { true }
        if (f.landToggleButton.isChecked) cVM?.selectedTypes?.add(EquipmentType.LAND)
        if (f.airToggleButton.isChecked) cVM?.selectedTypes?.add(EquipmentType.AIR)
        if (f.seaToggleButton.isChecked) cVM?.selectedTypes?.add(EquipmentType.SEA)
        if (f.gunsToggleButton.isChecked) cVM?.selectedTypes?.add(EquipmentType.GUN)
        cVM?.deckSize = qtySeekBar.progress
        cVM?.resetTest()
        return cVM?.deckSize ?: 0
    }
    //</editor-fold>

    //<editor-fold desc="VM Methods">
    private var cVM: CardsViewModel? = null
    fun initVM() {
        cVM = activity?.let { ViewModelProviders.of(it).get(CardsViewModel::class.java) }
        cVM?.let { lifecycle.addObserver(it) }
        cVM?.equipment?.observe(this, cardsObserver)
    }

    private val cardsObserver = Observer<List<Equipment>> { newEquipment ->
        configureVmCardsAndGetTotalChoices()
    }
    //</editor-fold>
}