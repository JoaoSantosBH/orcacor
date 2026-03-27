package com.jomar.senhorpintor.ui.onboarding

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.jomar.senhorpintor.R

class OnboardingAdapter : PagerAdapter() {

    override fun instantiateItem(
            collection: ViewGroup,
            position: Int
    ): Any = collection.findViewById(
            when (position) {
                0 -> R.id.on01
                1 -> R.id.on02
                2 -> R.id.on03
                else -> R.id.on04
            }
    )

    override fun isViewFromObject(
            arg0: View,
            arg1: Any
    ) = arg0 === arg1 as View

    override fun getCount() = 4

    override fun destroyItem(
            container: ViewGroup,
            position: Int,
            arg1: Any
    ) = Unit
}