package com.jomar.senhorpintor.ui.instructions

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.BaseFragment
import com.jomar.senhorpintor.base.YOUTUBE_INSTRUCTIONS
import kotlinx.android.synthetic.main.content_instrucoes.*


class InstructionsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_instructions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setActionBarWithButtonHome(getString(R.string.app_name))
    }

    private fun setupListeners() {
        ic_youtube.setOnClickListener {
            openYoutube()
        }
    }

    private fun openYoutube() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_INSTRUCTIONS)))
    }

    override fun onBackPressed(): Boolean {
        findNavController().popBackStack()
        return super.onBackPressed()
    }

}
