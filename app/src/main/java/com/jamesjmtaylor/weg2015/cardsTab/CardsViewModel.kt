package com.jamesjmtaylor.weg2015.cardsTab

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.jamesjmtaylor.weg2015.equipmentTabs.EquipmentRepository
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask

/**
 * Created by jtaylor on 3/11/18.
 */
class CardsViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
    val equipment = MediatorLiveData<List<Equipment>>()
    var selectedTypes = ArrayList<EquipmentType>()
    private val repo = EquipmentRepository()
    private var cards = ArrayList<Equipment>()
    var correctCard: Equipment? = null
    var choices = ArrayList<String>()
    private var correctChoiceIndex = 0
    var deckSize = 10
    private var currentDeckIndex = -1
    var difficulty = Difficulty.EASY
    private var timeRemaining = 10
    var timeRemainingData = MutableLiveData<Int>()
    private var incorrectGuesses = 0
    private var totalGuesses = 0

    init {
        val source = repo.getAll()
        equipment.addSource(source) {
            equipment.value = it
        }
    }

    fun getCurrentCardNumber(): Int {
        return currentDeckIndex + 1
        //return totalGuesses - incorrectGuesses + 1
    }

    private fun generateCards() {
        val possibleCards = equipment.value?.filter {
            selectedTypes.contains(it.type)
        }?.toMutableList()
        if (deckSize > possibleCards?.size ?: 0) {
            cards.addAll(0, possibleCards as? Collection<Equipment> ?: return)
            deckSize = possibleCards.size
        } else {
            possibleCards?.shuffle()
            for (i in 0 until deckSize) {
                val card = possibleCards?.get(i)
                cards.add(card ?: return)
            }
        }
    }

    private fun generateChoices(correctCard: Equipment?) {
        val possibleCards = (equipment.value ?: ArrayList()).toMutableList()
        possibleCards.shuffle()
        var i = -1
        choices.removeAll { true }
        while (choices.size < difficulty.choices && i < possibleCards.lastIndex) {
            i++
            if (possibleCards[i].name == correctCard?.name) continue //Don't add correct answer yet
            if (possibleCards[i].type != correctCard?.type) continue //Only add cards that have the same type
            choices.add(shorten(possibleCards[i].name))
        }
        correctChoiceIndex = (0..difficulty.choices).random()
        choices[correctChoiceIndex] = (shorten(correctCard?.name ?: "")) //choices fully generated
    }

    fun checkGuessAndIncrementTotal(selectedAnswer: String): Boolean {
        val correct = (selectedAnswer == choices[correctChoiceIndex])
        totalGuesses++
        if (!correct) incorrectGuesses++
        return correct
    }

    fun setNextCardGetChoicesResetTimer() {
        currentDeckIndex++
        if (currentDeckIndex >= cards.size) return
        correctCard = cards[currentDeckIndex]
        generateChoices(correctCard)
        resetTimer()
    }

    fun isEnd(): Boolean {
        return (currentDeckIndex >= cards.lastIndex)
    }

    fun calculateCorrectPercentage(): Int {
        return (((totalGuesses - incorrectGuesses).toDouble()) / (totalGuesses.toDouble()) * 100).toInt()
    }

    fun resetTest() {
        totalGuesses = 0
        incorrectGuesses = 0
        currentDeckIndex = -1
        cards = ArrayList()
        generateCards()
        setNextCardGetChoicesResetTimer()
    }

    private var timer = Timer()
    fun stopTimer() {
        timer.cancel()
    }

    private fun resetTimer() {
        setTimeToDifficulty()
        timer.cancel()
        timer = Timer() //Chuck the old-timer & old task
        val task = timerTask {
            timeRemaining--; if (timeRemaining < 0) setTimeToDifficulty()
            timeRemainingData.postValue(timeRemaining)
        }
        timer.schedule(task, 0, 1000)
    }

    private fun setTimeToDifficulty() {
        timeRemaining = when (difficulty) {
            Difficulty.EASY -> 999
            Difficulty.MEDIUM -> 11
            Difficulty.HARD -> 6
        }
    }

    // A helper method to take the string returned by toString and shorten it
    private fun shorten(longName: String): String {
        val descriptionStart = longName.indexOf(";")
        return if (descriptionStart > 0) {
            longName.substring(0, descriptionStart)
        } else {
            longName
        }
    }

}

fun ClosedRange<Int>.random() = ThreadLocalRandom.current().nextInt(endInclusive - start) + start
enum class Difficulty(val choices: Int) {
    EASY(3), MEDIUM(6), HARD(9)
}
