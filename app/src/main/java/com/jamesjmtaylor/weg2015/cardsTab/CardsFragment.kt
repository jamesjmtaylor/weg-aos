package com.jamesjmtaylor.weg2015.cardsTab

import android.app.AlertDialog
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.jamesjmtaylor.weg2015.App
import com.jamesjmtaylor.weg2015.R
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
        showNextFlashcard()
    }

    private fun showNextFlashcard() {
        val current = cVM?.getCurrentCardNumber().toString()
        val total = cVM?.deckSize.toString()
        cardCountTextView.text = "${current} of ${total}"
        guessLinearLayout.removeAllViews()
        for (i in 0..(cVM?.difficulty?.ordinal ?: 0)) {
            createGuessRow(i)
        }
        Glide.with(this)
                .load(App.instance.getString(R.string.base_url) + cVM?.correctCard?.photoUrl)
                .apply(RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter())
                .into(equipmentImageView)
    }
    //TODO: Find out why percentage is broken
    //TODO: Figure out why name shortner doesn't always work.
    //TODO: Find out why the correct button is the same button every single time (buttons don't change)
    //TODO: Find out why reset quiz means that all the buttons return incorrect
    //TODO: Find out why percentage is incorrect
    //TODO: Add incorrect animation
    //TODO: Animate countdown timer (Medium & Hard only, gone otherwise)
    //TODO: Add cool animation for card
    //TODO: Remove calculator tab

    fun createGuessRow(rowNumber: Int) {
        val inflater = LayoutInflater.from(activity)
        val guessRow = inflater.inflate(R.layout.row_cards, null, false)
        val choice0 = guessRow.findViewById<Button>(R.id.choice0)
        val choice1 = guessRow.findViewById<Button>(R.id.choice1)
        val choice2 = guessRow.findViewById<Button>(R.id.choice2)
        if ((cVM?.choices?.count() ?: 0) < 2 + rowNumber * 3) return
        choice0.text = cVM?.choices?.get((0 + rowNumber * 3))
        choice1.text = cVM?.choices?.get((1 + rowNumber * 3))
        choice2.text = cVM?.choices?.get((2 + rowNumber * 3))

        choice0.setOnClickListener(guessClickListener)
        choice1.setOnClickListener(guessClickListener)
        choice2.setOnClickListener(guessClickListener)
        //TODO: Add clickListeners to buttons as well
        guessLinearLayout.addView(guessRow)
    }
    private val guessClickListener = object : View.OnClickListener{
        override fun onClick(p0: View?) {
            val button = (p0 as Button)
            val guess = button.text.toString()
            if (cVM?.checkGuess(guess) ?: false){ //Correct answer
                if (cVM?.isEndElseSetNextCard() ?: false){ //Last answer
                    val percentage = cVM?.calculateCorrectPercentage() ?: 0
                    val builder = AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert)
                    builder.setTitle("Quiz Completed")
                            .setMessage("You got ${percentage}% correct.")
                            .setPositiveButton("Restart Quiz") { dialog, which ->
                                cVM?.resetCards()
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
                    showNextFlashcard()
                }
            } else {//Incorrect answer
                button.isEnabled = false
            }
        }
    }
    //MARK: ViewModel Methods
    private fun initVM() {
        cVM = activity?.let { ViewModelProviders.of(it).get(CardsViewModel::class.java) }
        cVM?.let { lifecycle.addObserver(it) } //Add ViewModel as an observer of this fragment's lifecycle
    }
}
