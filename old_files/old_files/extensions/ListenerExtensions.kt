package com.jomar.senhorpintor.extensions

import com.google.android.material.tabs.TabLayout

fun TabLayout.addOnTabSelectedListener(
        onReselected: (tab: TabLayout.Tab) -> Unit = {},
        onUnselected: (tab: TabLayout.Tab) -> Unit = {},
        onSelected: (tab: TabLayout.Tab) -> Unit = {}
) = object : TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab) { onReselected(tab) }
    override fun onTabUnselected(tab: TabLayout.Tab) { onUnselected(tab) }
    override fun onTabSelected(tab: TabLayout.Tab) { onSelected(tab) }
}.also{ addOnTabSelectedListener(it) }