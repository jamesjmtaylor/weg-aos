package com.jamesjmtaylor.weg2015.cardsTab

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.jamesjmtaylor.weg2015.App
import com.jamesjmtaylor.weg2015.R
import com.jamesjmtaylor.weg2015.models.Equipment
import kotlinx.android.synthetic.main.fragment_cards.*

class CardsFragment : Fragment(), LifecycleOwner {
    val TAG = "cardSetupFragment"
    var cVM : CardsViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVM()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val current = cVM?.currentDeckIndex.toString()
        val total = cVM?.deckSize.toString()
        cardCountTextView.text =  "${current} of ${total}"

        for (i in 0..(cVM?.difficulty?.ordinal ?: 0)){
            createGuessRow(i)
        }

        Glide.with(this)
                .load(App.instance.getString(R.string.base_url) + cVM?.correctCard?.photoUrl)
                .apply(RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter())
                .into(equipmentImageView)

    }
    fun createGuessRow(rowNumber: Int){
        val inflater = LayoutInflater.from(activity)
        val guessRow = inflater.inflate(R.layout.row_cards,null, false)
        val choice0 = guessRow.findViewById<Button>(R.id.choice0)
        val choice1 = guessRow.findViewById<Button>(R.id.choice1)
        val choice2 = guessRow.findViewById<Button>(R.id.choice2)
        if ((cVM?.choices?.count() ?: 0) < 2+rowNumber*3) return
        choice0.text = cVM?.choices?.get((0+rowNumber*3))
        choice1.text = cVM?.choices?.get((1+rowNumber*3))
        choice2.text = cVM?.choices?.get((2+rowNumber*3))
        //TODO: Add clickListeners to buttons as well
        guessLinearLayout.addView(guessRow)
    }

    //MARK: ViewModel Methods
    private fun initVM() {
        cVM = activity?.let { ViewModelProviders.of(it).get(CardsViewModel::class.java)}
        cVM?.let { lifecycle.addObserver(it) } //Add ViewModel as an observer of this fragment's lifecycle
        cVM?.equipment?.observe(this, cardsObserver)
    }
    private val cardsObserver = Observer<List<Equipment>> { newEquipment ->

    }
}
