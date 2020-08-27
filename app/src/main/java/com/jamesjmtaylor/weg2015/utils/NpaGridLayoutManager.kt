package com.jamesjmtaylor.weg2015.utils

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager


/**
 * Created by jtaylor on 3/11/18.
 */
class NpaGridLayoutManager : GridLayoutManager {
    /**
     * Disable predictive animations. There is a bug in RecyclerView which causes views that
     * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
     * adapter size has decreased since the ViewHolder was recycled.
     */
    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}
    constructor(context: Context, spanCount: Int) : super(context, spanCount) {}
    constructor(context: Context, spanCount: Int, orientation: Int, reverseLayout: Boolean) : super(context, spanCount, orientation, reverseLayout) {}
}