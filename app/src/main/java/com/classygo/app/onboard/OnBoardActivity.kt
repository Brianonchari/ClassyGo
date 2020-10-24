package com.classygo.app.onboard

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.classygo.app.R
import com.classygo.app.setup.LoginActivity
import com.classygo.app.utils.launchActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_onboard.*

class OnBoardActivity : AppCompatActivity() {
    private lateinit var sliderAdapter: OnBoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard)

        loadSlider()
        showNextSlide()
    }

    override fun onStart() {
        super.onStart()
        TabLayoutMediator(tvTabLayout, vpSlidePager) { _, _ -> }.attach()
    }

    private fun showNextSlide() {
        mbContinue.setOnClickListener {
            if (vpSlidePager.currentItem.plus(1) < sliderAdapter.itemCount) {
                vpSlidePager.currentItem += 1
            } else {
                launchActivity<LoginActivity>()
                finish()
            }
        }
    }

    private fun loadSlider() {
        val sliderList = listOf(
            SliderList(
                getString(R.string.journey_made_easy),
                getString(R.string.journey_made_easy_body), R.drawable.ic_travelers
            ),
            SliderList(
                getString(R.string.pay_faster),
                getString(R.string.pay_faster_body), R.drawable.ic_online_payments
            ),
            SliderList(
                getString(R.string.ride_with_us),
                getString(R.string.your_safety_our_concern), R.drawable.ic_security_on
            )
        )

        val classyGoEffect = SpannableString(tvHeader.text.toString()).apply {
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        this@OnBoardActivity,
                        R.color.colorBlack
                    )
                ),
                6, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        tvHeader.text = classyGoEffect

        sliderAdapter = OnBoardAdapter(sliderList)
        vpSlidePager.adapter = sliderAdapter
    }
}