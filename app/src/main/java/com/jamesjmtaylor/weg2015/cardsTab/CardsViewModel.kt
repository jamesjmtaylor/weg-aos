package com.jamesjmtaylor.weg2015.cardsTab

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MediatorLiveData
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType
import com.jamesjmtaylor.weg2015.equipmentTabs.EquipmentRepository
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList

/**
 * Created by jtaylor on 3/11/18.
 */
class CardsViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
    val equipment = MediatorLiveData<List<Equipment>>()
    var selectedTypes = ArrayList<EquipmentType>()//TODO: Find out why this is empty even adding values
    private val repo = EquipmentRepository()
    private var cards = ArrayList<Equipment>()
    var correctCard : Equipment? = null
    var choices = ArrayList<String>()
    private var correctChoiceIndex = 0
    var deckSize = 10
    var currentDeckIndex = 0
    var difficulty = Difficulty.EASY
    private var incorrectGuesses = 0
    private var totalGuesses = 0

    init {
        val source = repo.getAll()
        equipment.addSource(source) {
            equipment.value = it
        }
    }
    private fun generateCards(){
        val possibleCards = equipment.value?.filter { selectedTypes.contains(it.type) }
        if (deckSize > possibleCards?.size ?: 0){
            cards.addAll(0,possibleCards as? Collection<Equipment> ?: return)
            deckSize = possibleCards.size
        } else {
            Collections.shuffle(possibleCards)
            for (i in 0 until deckSize) {
                val card = possibleCards?.get(i)
                cards.add(card ?: return)
            }
        }
    }
    private fun generateChoices(correctCard: Equipment?){
        val possibleCards = equipment.value ?: ArrayList<Equipment>()
        Collections.shuffle(possibleCards)
        var i = -1
        while (choices.size < difficulty.choices && i < possibleCards.lastIndex){
            i++
            if (possibleCards.get(i).name.equals(correctCard?.name)) continue //Don't add correct answer yet
            choices.add(possibleCards.get(i).name)
        }
        correctChoiceIndex = (0 .. difficulty.choices).random()
        choices[correctChoiceIndex] = (correctCard?.name ?: return) //choices fully generated
    }
    fun checkGuess(selectedAnswer: String):Boolean{
        val correct = (selectedAnswer.equals(choices.get(correctChoiceIndex)))
        totalGuesses++
        if (!correct) incorrectGuesses++
        return correct
    }
    fun setNextCard(){
        currentDeckIndex++
        if (currentDeckIndex > cards.lastIndex) return
        correctCard = cards.get(currentDeckIndex)
    }
    fun calculateCorrectPercentage(): Int{
        return (totalGuesses - incorrectGuesses) / totalGuesses
    }
    fun resetCards(){
        totalGuesses = 0
        incorrectGuesses = 0
        generateCards()
        setNextCard()
        generateChoices(correctCard as? Equipment)
    }

}
fun ClosedRange<Int>.random() = ThreadLocalRandom.current() .nextInt(endInclusive - start) +  start
enum class Difficulty(val choices: Int) {
    EASY(3), MEDIUM(6), HARD(9)
}
