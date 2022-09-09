package com.bpb.android.clips.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bpb.android.clips.BpbClipsBaseActivity
import com.bpb.android.clips.R
import com.bpb.android.clips.ui.home.ClipsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : BpbClipsBaseActivity(), NavController.OnDestinationChangedListener {
    private val homeViewModel by viewModels<ClipsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        nav_view.setupWithNavController(navController)

        navController.addOnDestinationChangedListener(this)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.navigation_home -> {
                changeStatusBarColor(R.color.colorBlack)
                val colorDark = ContextCompat.getColorStateList(
                    this,
                    R.color.bottom_tab_selector_item_dark
                )

                val colorBlack = ContextCompat.getColorStateList(
                    this,
                    R.color.colorBlack
                )

                nav_view.backgroundTintList = colorBlack
                nav_view.itemTextColor = colorDark
                nav_view.itemIconTintList = colorDark
                image_view_add_icon.setImageResource(R.drawable.ic_add_icon_light)
            }
            else -> {
                changeStatusBarColor(R.color.colorWhite)
                val colorDark = ContextCompat.getColorStateList(
                    this,
                    R.color.bottom_tab_selector_item_light
                )

                val colorWhite = ContextCompat.getColorStateList(
                    this,
                    R.color.colorWhite
                )

                nav_view.backgroundTintList = colorWhite
                nav_view.itemTextColor = colorDark
                nav_view.itemIconTintList = colorDark
                image_view_add_icon.setImageResource(R.drawable.ic_add_icon_dark)
            }
        }
    }
}