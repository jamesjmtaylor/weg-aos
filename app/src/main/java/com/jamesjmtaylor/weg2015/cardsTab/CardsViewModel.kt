package com.jamesjmtaylor.weg2015.cardsTab

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.jamesjmtaylor.weg2015.equipmentTabs.EquipmentRepository
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType
import java.lang.Integer.max
import java.lang.Integer.min
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
    private var cards = listOf<Equipment>()
    var choices = listOf<String>()
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

    fun getCurrentCardNumber(): Int = currentDeckIndex + 1

    private fun generateCards() {
        val possibleCards = equipment.value?.filter {
            selectedTypes.contains(it.type)
        }?.toMutableList()?.shuffled()?.toList() ?: emptyList()
        if (deckSize > possibleCards.size) { //More cards for the set than cards available
            cards = possibleCards
            deckSize = possibleCards.size
        } else {
            cards = possibleCards.subList(0, deckSize - 1)
        }
    }

    private fun generateChoicesAndSetCorrectChoiceIndex(correctCard: Equipment): List<String> {
        val possibleCards = equipment.value?.toMutableList()?.shuffled()?.toList() ?: return emptyList()
        val wrongChoices = possibleCards.filter {
            it.name != correctCard.name && it.type == correctCard.type
        }
        val possibleChoices = wrongChoices.map { shorten(it.name) }
            .subList(0, min(wrongChoices.size, difficulty.choices - 1))
            .plus(correctCard.name).shuffled()
        correctChoiceIndex = possibleChoices.indexOf(correctCard.name)
        return possibleChoices
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
        choices = generateChoicesAndSetCorrectChoiceIndex(cards[currentDeckIndex])
        resetTimer()
    }

    fun isEnd(): Boolean = currentDeckIndex >= cards.lastIndex
    fun getCorrectCardPhotoUrl(): String? {
        return cards[currentDeckIndex].photoUrl
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
    fun stopTimer() = timer.cancel()
    private fun resetTimer() {
        timeRemaining = getTimeForDifficulty()
        timer.cancel()
        timer = Timer() //Chuck the old-timer & old task
        val task = timerTask {
            timeRemaining--
            if (timeRemaining < 0) timeRemaining = getTimeForDifficulty()
            timeRemainingData.postValue(timeRemaining)
        }
        timer.schedule(task, 0, 1000)
    }

    private fun getTimeForDifficulty(): Int {
        return when (difficulty) {
            Difficulty.EASY -> 999
            Difficulty.MEDIUM -> 11
            Difficulty.HARD -> 6
        }
    }

    // A helper method to take the string returned by toString and shorten it
    private fun shorten(longName: String): String {
        val descriptionStart = max(longName.indexOf(";"),0)// max prevents StringIndexOutOfBoundsException below if not found
        val shortName = longName.substring(0, descriptionStart)
        return if (shortName.isNotEmpty()) shortName else longName
    }
}

fun ClosedRange<Int>.random() = ThreadLocalRandom.current().nextInt(endInclusive - start) + start
enum class Difficulty(val choices: Int) {
    EASY(3), MEDIUM(6), HARD(9)
}
