package com.jomar.senhorpintor.ui.terms_use

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.BaseFragment
import com.jomar.senhorpintor.extensions.hide
import com.jomar.senhorpintor.extensions.show
import com.jomar.senhorpintor.makeItGone
import kotlinx.android.synthetic.main.fragment_terms_of_use.*


class TermsOfUseFragment : BaseFragment() {
    private val arguments by navArgs<TermsOfUseFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_terms_of_use, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        loadWebView()
        setupViews()

    }

    private fun setupViews() {
        val title = getString(R.string.accept_terms_page_name)
        setActionBarWithButtonHome(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                back()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun back() {
        findNavController().navigateUp()
    }

    private fun setupListeners() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView() {
        progressBar.visibility = View.VISIBLE
        val cookieManager = CookieManager.getInstance()
        val urlWebView = arguments.url

        cookieManager.flush()
        cookieManager.setAcceptCookie(true)
        cookieManager.getCookie(urlWebView)
        cookieManager.acceptCookie()
        CookieManager.getInstance().acceptCookie()

        termsWebView.apply {
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.domStorageEnabled = true
            while (canGoBack()) {
                goBack()
            }
            webViewClient = handleWithWebViewFlow(urlWebView)
            loadUrl(urlWebView)
        }

        WebView.setWebContentsDebuggingEnabled(false)

    }

    private fun WebView.handleWithWebViewFlow(urlWebView: String?) =
            createWebViewClientEvent(
                    onSuccess = {
                        if (progressBar != null) progressBar.hide()
                        this.show()
                    },
                    onError = {
                        webViewClient = null
                        progressBar.show()
                        this.makeItGone()
                    },
                    onAnswer = { false }
            )
}
