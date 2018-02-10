package com.jamesjmtaylor.weg2015.tabBar

import android.app.Application
import android.arch.lifecycle.*
import android.os.AsyncTask
import com.jamesjmtaylor.weg2015.Models.Gun
import java.io.File

/**
 * Created by jtaylor on 2/10/18.
 */
class EquipmentViewModel: ViewModel(), LifecycleObserver {
    // You probably have something more complicated
    // than just a String. Roll with me
    var guns = MutableLiveData<List<Gun>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unSubscribeViewModel() {
        for (disposable in currencyRepository.allCompositeDisposable) {
            compositeDisposable.addAll(disposable)
        }
        compositeDisposable.clear()
    }
    override fun onCleared() {
        unSubscribeViewModel()
        super.onCleared()
    }

    fun initEquipment() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}