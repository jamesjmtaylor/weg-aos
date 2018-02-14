package com.jamesjmtaylor.weg2015.tabBar.equipmentTabs

import android.app.Application
import android.arch.lifecycle.*
import com.jamesjmtaylor.weg2015.Models.Gun

/**
 * Created by jtaylor on 2/10/18.
 */
class EquipmentViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
    var equipment = MutableLiveData<List<Gun>>()
    var isLoading = MutableLiveData<Boolean>()
    val repo = EquipmentRepository()
//    var db : AppDatabase? = null
    fun initData() {
        repo.getGuns()
//        val context = this.getApplication<Application>() as Context
//        class someTask() : AsyncTask<Void, Void, String>() {
//            override fun doInBackground(vararg params: Void?): String? {
//                db = AppDatabase.getInstance(context)
//                var gunList = ArrayList<Gun>()
//                for (i in 0..100){
//                    gunList.add(Gun("Gun "+i.toString()))
//                }
//                db?.GunDao()?.insertGuns(gunList)
//
//                return null
//            }
//            override fun onPostExecute(result: String?) {
//                //equipment.setValue(db?.GunDao()?.getAllGuns()?.value)
//            }
//        }
//        someTask().execute()
    }


//        isLoading.postValue(true)
//        equipment.postValue(db?.GunDao()?.getAllGuns()?.value)
//        isLoading.postValue(false)

//        thread { Thread.sleep(5000) //Simulates async network call
//            var gunList = ArrayList<Gun>()
//            for (i in 0..100){
//                gunList.add(Gun("Gun "+i.toString()))
//            }
//            equipment.postValue(gunList)
//            isLoading.postValue(false)
//        }
}