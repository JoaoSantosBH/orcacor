package com.jomar.senhorpintor.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.BaseFragment
import com.jomar.senhorpintor.extensions.setTitle

class HomeFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbarAndTitle(getString(R.string.app_name),true)
        removeHomeButton()

    }
}
