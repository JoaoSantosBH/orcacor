package com.jomar.senhorpintor.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.webkit.WebResourceErrorCompat
import androidx.webkit.WebViewClientCompat
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.extensions.findActivityNavViewController
import com.jomar.senhorpintor.extensions.handleErrors
import com.jomar.senhorpintor.extensions.setToolbar
import com.jomar.senhorpintor.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolba.*

open class BaseFragment : Fragment() , OnBackPressedListener{
    val mainNavController by lazy { findActivityNavViewController() }
    override fun onCreate(savedInstanceState: Bundle?) {
        val simpleName = this@BaseFragment.javaClass.simpleName
        super.onCreate(savedInstanceState)
    }

    fun setToolbarCustomTitle(
            toolbarId: Int,
            title: String? = null,
            hasNavigationHome: Boolean = false,
            hasOptionMenu: Boolean = true
    ) {

        requireView().findNavController()
        setHasOptionsMenu(hasOptionMenu)
        (activity as AppCompatActivity).setToolbar(toolbarId, title, hasNavigationHome)
    }

    fun <T> LiveData<FlowState<T>>.handleWithFlow(
            onLoading: () -> Unit = {},
            onFailure: (Throwable) -> Unit = { handleErrors(it) },
            onComplete: (() -> Unit) = {},
            onSuccess: (T) -> Unit = {}
    ) = handleWithFlow(
            lifecycleOwner = this@BaseFragment,
            onLoading = onLoading,
            onComplete = onComplete,
            onSuccess = onSuccess,
            onFailure = onFailure
    )

    fun showToolbarAndTitle(title: String? = null, show: Boolean) {
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = title
        (activity as AppCompatActivity).window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(show)
        requireView().findNavController()
    }

    fun removeHomeButton(){
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val toolbar: Toolbar = (activity as AppCompatActivity).toolbar
        if (toolbar != null){
            val navigationView = (activity as AppCompatActivity).nvView
            navigationView.setNavigationItemSelectedListener((activity as HomeActivity))
            val drawerLayout : DrawerLayout = (activity as HomeActivity).drawer_layout
           if (drawerLayout != null){
               val toggle = ActionBarDrawerToggle(
                       (activity as AppCompatActivity), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
               drawerLayout.addDrawerListener(toggle)
               toggle.syncState()
           }
        }
    }

    fun lockNavDrawerMenu() {
        val drawerLayout : DrawerLayout = (activity as HomeActivity).drawer_layout
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    fun setActionBarWithButtonHome(title: String? = null) {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = title
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val toolbar: Toolbar = (activity as AppCompatActivity).toolbar
        toolbar.setNavigationOnClickListener {
            onBackPressed()
            }

    }

    fun createWebViewClientEvent(
            onSuccess: () -> Unit,
            onError: () -> Unit,
            onAnswer: (Uri?) -> Boolean = { startActivity(Intent(Intent.ACTION_VIEW, it)); true }
    ) =
            object : WebViewClientCompat() {
                override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                    return super.shouldInterceptRequest(view, request)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    onSuccess()
                }

                override fun onReceivedError(
                        view: WebView,
                        request: WebResourceRequest,
                        error: WebResourceErrorCompat
                ) {
                    onError()
                    super.onReceivedError(view, request, error)
                }

                override fun shouldOverrideUrlLoading(
                        view: WebView,
                        request: WebResourceRequest
                ): Boolean {
                    return false
                }

            }

    override fun onBackPressed(): Boolean {
        return true
    }
}