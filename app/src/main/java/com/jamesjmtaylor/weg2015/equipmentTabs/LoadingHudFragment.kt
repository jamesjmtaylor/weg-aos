package com.jamesjmtaylor.weg2015.equipmentTabs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jamesjmtaylor.weg2015.R

/**
 * A simple [Fragment] subclass.
 */
class LoadingHudFragment : Fragment() {
    val TAG = "loadingHudFrag"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_loading_hud, container, false)
        return v
    }

}
