package com.jamesjmtaylor.weg2015.cardsTab

import android.app.AlertDialog
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.jamesjmtaylor.weg2015.R
import com.jamesjmtaylor.weg2015.baseUrl
import com.jamesjmtaylor.weg2015.utils.openFile
import kotlinx.android.synthetic.main.activity_nav.*
import kotlinx.android.synthetic.main.fragment_cards.*


class CardsFragment : Fragment(), LifecycleOwner {
    val TAG = "cardSetupFragment"
    var cVM: CardsViewModel? = null
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
        createGuessRows()
        updateUi()
    }

    private fun updateUi() {
        val totalCards = if ((cVM?.deckSize ?: 0) > 0) cVM!!.deckSize else 1
        val totalCardsString = totalCards.toString()
        val currentCardsString = cVM?.getCurrentCardNumber().toString()
        cardCountTextView.text = "$currentCardsString of $totalCardsString"
        if (cVM?.difficulty?.equals(Difficulty.EASY)?:true){
            timeRemainingTextView.visibility = View.GONE
        }
        Glide.with(this)
                .load(openFile(cVM?.correctCard?.photoUrl))
                .apply(RequestOptions()
                        .centerInside())
                .into(equipmentImageView)
        populateGuessButtons()
    }
    fun createGuessRows() {
        for (i in 0..(cVM?.difficulty?.ordinal ?: 0)) {
            val inflater = LayoutInflater.from(activity)
            val guessRow = inflater.inflate(R.layout.row_cards, null, false)
            val choice0 = guessRow.findViewById<Button>(R.id.choice0)
            val choice1 = guessRow.findViewById<Button>(R.id.choice1)
            val choice2 = guessRow.findViewById<Button>(R.id.choice2)
            choice0.setOnClickListener(guessClickListener)
            choice1.setOnClickListener(guessClickListener)
            choice2.setOnClickListener(guessClickListener)
            guessLinearLayout.addView(guessRow)
        }
    }
    fun populateGuessButtons() {
        try {
            for (row in 0 until guessLinearLayout.childCount){
                val rowLayout = guessLinearLayout.getChildAt(row) as LinearLayout
                for (column in 0 until rowLayout.childCount){
                    val button = rowLayout.getChildAt(column) as Button
                    button.text = cVM?.choices?.get(column + row * 3)
                }
            }
        } catch (e: Exception){
            Toast.makeText(this.context,getString(R.string.no_flashcards_error),Toast.LENGTH_LONG).show()
            activity?.fragmentFrameLayout?.id?.let {
                val cardsSetupFragment = CardsSetupFragment()
                val transaction = activity?.supportFragmentManager
                transaction?.beginTransaction()?.replace(it, cardsSetupFragment, cardsSetupFragment.TAG)?.commit()
            }
        }

    }
    fun reactivateGuessButtons(){
        for (row in 0 until guessLinearLayout.childCount){
            val rowLayout = guessLinearLayout.getChildAt(row) as LinearLayout
            for (column in 0 until rowLayout.childCount){
                val button = rowLayout.getChildAt(column) as Button
                button.isEnabled = true
            }
        }
    }
    private val guessClickListener = object : View.OnClickListener{
        override fun onClick(p0: View?) {
            val button = (p0 as? Button)
            val guess = button?.text.toString()
            if ((cVM?.checkGuessAndIncrementTotal(guess) ?: false)||button==null){ //Go to next card
                reactivateGuessButtons()
                if (cVM?.isEnd() ?: false){ //Last answer
                    cVM?.stopTimer()
                    val percentage = cVM?.calculateCorrectPercentage() ?: 0
                    val builder = AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert)
                    builder.setTitle("Quiz Completed")
                            .setMessage("You got ${percentage}% correct.")
                            .setPositiveButton("Restart Quiz") { dialog, which ->
                                cVM?.resetTest()
                                updateUi()
                            }
                            .setNegativeButton("Change Quiz") { dialog, which ->
                                activity?.fragmentFrameLayout?.id?.let {
                                    val cardsSetupFragment = CardsSetupFragment()
                                    val transaction = activity?.supportFragmentManager
                                    transaction?.beginTransaction()?.replace(it, cardsSetupFragment, cardsSetupFragment.TAG)?.commit()
                                }
                            }
                            .show()
                } else { //Not last answer
                    cVM?.setNextCardGetChoicesResetTimer()
                    updateUi()
                }
            } else {//Incorrect answer
                button?.isEnabled = false
            }
        }
    }
    //MARK: ViewModel Methods
    private fun initVM() {
        cVM = activity?.let { ViewModelProviders.of(it).get(CardsViewModel::class.java) }
        cVM?.let { lifecycle.addObserver(it) } //Add ViewModel as an observer of this fragment's lifecycle
        cVM?.timeRemainingData?.observe(this, timeObserver)
    }
    val timeObserver = Observer<Int> { timeRemaining ->
        val timeText = """00:${String.format("%02d", timeRemaining)} Remaining"""
        timeRemainingTextView.text = timeText
        if (timeRemaining != null && timeRemaining < 1) {
            guessClickListener.onClick(null)
        }
    }
}
