package com.classygo.app.onboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.classygo.app.R
import com.classygo.app.setup.LoginActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_onboard.*

class OnBoardActivity : AppCompatActivity() {
    private lateinit var sliderAdapter : OnBoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard)

        loadSlider()
        showNextSlide()
    }

    override fun onStart() {
        super.onStart()
        vpSlidePager.registerOnPageChangeCallback(pageChangeListener)
        TabLayoutMediator(tvTabLayout,vpSlidePager) { _, _ ->  }.attach()
    }

    private val pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            when (position) {
                0 -> {
                    // resize image
                }
                1 -> {
                    // resize image
                }
                else -> {
                    // resize image
                }
            }
        }
    }

    private fun showNextSlide(){
        mbContinue.setOnClickListener {
            if(vpSlidePager.currentItem.plus(1) < sliderAdapter.itemCount){
                vpSlidePager.currentItem += 1
            }else{
                // Navigate to Login Screen
                Intent(this, LoginActivity::class.java).apply {
                    this.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(this)
                }
            }
        }
    }

    private fun loadSlider(){
        val sliderList =  listOf(
            SliderList(getString(R.string.journey_made_easy),
                getString(R.string.journey_made_easy_body), R.drawable.ic_travelers),
            SliderList(getString(R.string.pay_faster),
                getString(R.string.pay_faster_body), R.drawable.ic_online_payments),
            SliderList(getString(R.string.ride_with_us),
                getString(R.string.your_safety_our_concern), R.drawable.ic_security_on)
        )

        sliderAdapter = OnBoardAdapter(sliderList)
        vpSlidePager.adapter = sliderAdapter
    }
}