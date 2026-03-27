package com.jomar.senhorpintor.extensions

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.App
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

var actionDisplayText = "..."

private fun TextView.getPartialStringWithDots(
        string: CharSequence?,
        lastCharShown: Int
) {

    text = SpannableStringBuilder().append(
            string?.substring(
                    0,
                    lastCharShown
            )
    ).append(actionDisplayText)
}

fun TextView.addShortString(text: String) {
    this.text = text
    movementMethod = LinkMovementMethod.getInstance()
    post {
        setTitleCaption(text)
    }
}

fun TextView.setTitleCaption(string: CharSequence?) {
    if (lineCount > 2) {
        val lastCharShown = layout.getLineVisibleEnd(2 - 1)
        maxLines = 2
        getPartialStringWithDots(string, lastCharShown)
    }
}

fun CharSequence.applyBoldOn(match: String): CharSequence {
    val stringBuilder = SpannableStringBuilder(this)
    val boldSpan = StyleSpan(Typeface.BOLD)
    if (stringBuilder.contains(match)) {
        stringBuilder.setSpan(
                boldSpan,
                lastIndexOf(match),
                lastIndexOf(match) + match.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
    }
    return stringBuilder
}
fun CharSequence.applyColor(match: String): CharSequence {
    val stringBuilder = SpannableStringBuilder(this)
    if (stringBuilder.contains(match)) {
        stringBuilder.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(App.instance, R.color.blueAccent)),
                lastIndexOf(match),
                lastIndexOf(match) + match.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return stringBuilder
}



fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

