package com.jomar.senhorpintor.ui.onboarding


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.jomar.senhorpintor.FadePageTransformer
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.extensions.hide
import com.jomar.senhorpintor.makeItVisible
import kotlinx.android.synthetic.main.fragment_on_boarding.*


class OnBoardingFragment : Fragment() , ViewPager.OnPageChangeListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_on_boarding, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.requireActivity().setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
        this.requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setUpViewPager()
        tv_begin.setOnClickListener {
            findNavController().navigate(R.id.action_onFragmentBoarding_to_fragmentRegister)
        }
        iv_begin.setOnClickListener {
            findNavController().navigate(R.id.action_onFragmentBoarding_to_fragmentRegister)
        }
    }
    private fun setUpViewPager() {
        onbPager.adapter = OnboardingAdapter()
        onbPager.offscreenPageLimit = 4
        onbPager.setPageTransformer(true, FadePageTransformer())
        tab.setupWithViewPager(onbPager, true)
        onbPager.addOnPageChangeListener(this)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }
    override fun onPageSelected(position: Int) {
        if (position ==3){
            tv_begin.makeItVisible()
            iv_begin.makeItVisible()
        } else{
            tv_begin.hide()
            iv_begin.hide()
        }
    }
}
