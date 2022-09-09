package com.bpb.android.clips.ui.main

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bpb.android.clips.BpbClipsBaseActivity
import com.bpb.android.clips.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BpbClipsBaseActivity(), NavController.OnDestinationChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.navHost)
        navView.setupWithNavController(navController)

        // navController.addOnDestinationChangedListener(this)
        setDefaultBackground()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.navigation_home -> {
                setDarkBackground()
            }
            else -> {
                setDefaultBackground()
            }
        }
    }

    private fun setDefaultBackground() {
        changeStatusBarColor(R.color.colorWhite)
        val colorDark = ContextCompat.getColorStateList(
            this,
            R.color.bottom_tab_selector_item_light
        )

        val colorWhite = ContextCompat.getColorStateList(
            this,
            R.color.colorWhite
        )

        navView.backgroundTintList = colorWhite
        navView.itemTextColor = colorDark
        navView.itemIconTintList = colorDark
        ivAddIcon.setImageResource(R.drawable.ic_add_icon_dark)
    }

    private fun setDarkBackground() {
        changeStatusBarColor(R.color.colorBlack)
        val colorDark = ContextCompat.getColorStateList(
            this,
            R.color.bottom_tab_selector_item_dark
        )

        val colorBlack = ContextCompat.getColorStateList(
            this,
            R.color.colorBlack
        )

        navView.backgroundTintList = colorBlack
        navView.itemTextColor = colorDark
        navView.itemIconTintList = colorDark
        ivAddIcon.setImageResource(R.drawable.ic_add_icon_light)
    }
}