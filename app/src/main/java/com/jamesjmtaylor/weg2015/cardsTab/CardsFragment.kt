package com.jamesjmtaylor.weg2015.cardsTab

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.jamesjmtaylor.weg2015.App
import com.jamesjmtaylor.weg2015.R
import com.jamesjmtaylor.weg2015.baseUrl
import com.jamesjmtaylor.weg2015.utils.Analytics
import com.jamesjmtaylor.weg2015.utils.openFile
import kotlinx.android.synthetic.main.activity_equipment.*
import kotlinx.android.synthetic.main.fragment_cards.*


class CardsFragment : Fragment(), LifecycleOwner {
    val TAG = "cardsFragment"
    var cVM: CardsViewModel? = null
    private var firebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVM()
        context?.let { firebaseAnalytics = FirebaseAnalytics.getInstance(it) }
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
        val totalCards = if ((cVM?.deckSize ?: 0) > 0) cVM?.deckSize else 1
        cardCountTextView.text = "${cVM?.getCurrentCardNumber()} of $totalCards"
        if (cVM?.difficulty?.equals(Difficulty.EASY) ?: true) {
            timeRemainingTextView.visibility = View.GONE
        }

        val filepath = openFile(cVM?.correctCard?.photoUrl)
        val image: Any = if (filepath?.exists() == true) filepath else baseUrl + cVM?.correctCard?.photoUrl
        Glide.with(this)
                .load(image)
                .apply(RequestOptions()
                        .centerInside())
                .into(equipmentImageView)
        populateGuessButtons()
    }

    private fun createGuessRows() {
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

    private fun populateGuessButtons() {
        try {
            for (row in 0 until guessLinearLayout.childCount) {
                val rowLayout = guessLinearLayout.getChildAt(row) as LinearLayout
                for (column in 0 until rowLayout.childCount) {
                    val button = rowLayout.getChildAt(column) as Button
                    button.text = cVM?.choices?.get(column + row * 3)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this.context, getString(R.string.no_flashcards_error), Toast.LENGTH_LONG).show()
            fragmentFrameLayout?.id?.let {
                val cardsSetupFragment = CardsSetupFragment()
                val transaction = activity?.supportFragmentManager
                transaction?.beginTransaction()?.replace(it, cardsSetupFragment, cardsSetupFragment.TAG)?.commit()
            }
        }

    }

    fun reactivateGuessButtons() {
        for (row in 0 until guessLinearLayout.childCount) {
            val rowLayout = guessLinearLayout.getChildAt(row) as LinearLayout
            for (column in 0 until rowLayout.childCount) {
                val button = rowLayout.getChildAt(column) as Button
                button.isEnabled = true
            }
        }
    }

    private val guessClickListener = object : View.OnClickListener {
        override fun onClick(p0: View?) {
            val button = (p0 as? Button)
            val guess = button?.text.toString()
            if ((cVM?.checkGuessAndIncrementTotal(guess) == true) || button == null) { //Go to next card
                reactivateGuessButtons()
                if (cVM?.isEnd() == true) { //Last answer
                    cVM?.stopTimer()
                    val percentage = cVM?.calculateCorrectPercentage() ?: 0

                    Analytics.saveQuizResults(cVM?.selectedTypes.toString(), percentage, cVM?.difficulty?.ordinal
                            ?: -1)
                    val pref = App.instance.getSharedPreferences(App.instance.getString(R.string.bundle_id), Context.MODE_PRIVATE)
                    val previouslyPrompted = pref.getBoolean(RATING_PROMPT_KEY, false)
                    if (percentage > 70 && !previouslyPrompted) {
                        showRequestRatingDialogue(percentage)
                    } else {
                        showQuizCompleteDialogue(percentage)
                    }
                } else { //Not last answer
                    cVM?.setNextCardGetChoicesResetTimer()
                    updateUi()
                }
            } else {//Incorrect answer
                button.isEnabled = false
            }
        }
    }

    private fun showQuizCompleteDialogue(percentage: Int) {
        val builder = AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert)
        builder.setTitle("Quiz Completed")
                .setMessage("You got $percentage% correct.")
                .setPositiveButton("Restart Quiz") { dialog, which ->
                    cVM?.resetTest()
                    updateUi()
                }
                .setNegativeButton("Change Quiz") { dialog, which ->
                    returnToSetup()
                }
                .show()
    }

    private fun showRequestRatingDialogue(percentage: Int) {
        val builder = AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert)
        val pref = App.instance.getSharedPreferences(App.instance.getString(R.string.bundle_id), Context.MODE_PRIVATE)
        pref.edit().putBoolean(RATING_PROMPT_KEY, true).apply()
        builder.setTitle("Congratulations!")
                .setMessage("You got $percentage% correct.  Would you please rate the app? This will be the only time that you're prompted to leave a rating on Google Play.")
                .setPositiveButton("Open rating page") { dialog, which ->
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(
                                "https://play.google.com/store/apps/details?id=com.jamesjmtaylor.weg2015")
                        setPackage("com.android.vending")
                    }
                    startActivity(intent)
                    returnToSetup()
                }
                .setNegativeButton("No thanks") { dialog, which ->
                    returnToSetup()
                }
                .show()
    }

    private fun returnToSetup() {
        activity?.fragmentFrameLayout?.id?.let {
            val cardsSetupFragment = CardsSetupFragment()
            val transaction = activity?.supportFragmentManager
            transaction?.beginTransaction()?.replace(it, cardsSetupFragment, cardsSetupFragment.TAG)?.commit()
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

private const val RATING_PROMPT_KEY = "ratingPrompted"