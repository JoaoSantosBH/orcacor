package com.jomar.senhorpintor.ui.home

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.TERMS_URL
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolba.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)
        drawerLayout = drawer_layout

        setSupportActionBar(toolbar)
        this.title = getString(R.string.app_name)

        val navigationView = findViewById<NavigationView>(R.id.nvView)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        nvView.setNavigationItemSelectedListener(this)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_new_orcament -> findNavController(R.id.myNavHostFragment).navigate(R.id.action_homeFragment_to_badgeDestinationFragment)
            R.id.nav_history -> findNavController(R.id.myNavHostFragment).navigate(R.id.action_homeFragment_to_historicFragment)
            R.id.nav_instructions -> findNavController(R.id.myNavHostFragment).navigate(R.id.action_homeFragment_to_instructionsFragment)
            R.id.nav_my_info -> findNavController(R.id.myNavHostFragment).navigate(R.id.action_homeFragment_to_editMyInfoFragment)
            R.id.use_terms -> findNavController(R.id.myNavHostFragment).navigate(HomeFragmentDirections.actionHomeFragmentToTermsOfUseFragmentHome2(TERMS_URL))
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}


