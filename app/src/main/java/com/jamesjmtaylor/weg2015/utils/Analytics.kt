package com.jamesjmtaylor.weg2015.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.jamesjmtaylor.weg2015.App

object Analytics {
    private var firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(App.INSTANCE)
    fun saveQuizResults(categories: String, score: Int, difficulty: Int) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CHARACTER, categories)
        bundle.putLong(FirebaseAnalytics.Param.SCORE, score.toLong())
        bundle.putLong(FirebaseAnalytics.Param.LEVEL, difficulty.toLong())
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.POST_SCORE, bundle)
    }

    fun saveUserSearch(searchQuery: String, resultCount: Int) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchQuery)
        bundle.putLong(FirebaseAnalytics.Param.NUMBER_OF_NIGHTS, resultCount.toLong())
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
    }

    fun saveEquipmentView(equipmentName: String, category: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, equipmentName)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
    }

    fun saveTabView(tabName: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, tabName)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, bundle)
    }
}