package com.jamesjmtaylor.weg2015.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder

/**
 * Created by jtaylor on 3/10/18.
 */
fun boldString(text: String): SpannableStringBuilder{
    val str = SpannableStringBuilder(text)
    str.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
            0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return str
}
