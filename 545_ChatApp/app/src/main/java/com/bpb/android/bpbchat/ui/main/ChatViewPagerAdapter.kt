package com.bpb.android.bpbchat.ui.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bpb.android.bpbchat.R
import com.bpb.android.bpbchat.ui.contacts.ContactsFragment
import com.bpb.android.bpbchat.ui.profile.UserProfileFragment

val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3
)

/**
 * A [FragmentStateAdapter] that returns a fragment corresponding to
 * one of the pages/ tabs . In our case tabs are Chats, Calls and Profile
 */
class ChatViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    /**
     * Return the fragment object for specific position.
     */
    override fun createFragment(position: Int) = when (position) {
        0 -> {
            // Show contact list page
            ContactsFragment()
        }
        1 -> {
            // Show dummy fragment where you can add calls feature later.
            PlaceholderFragment()
        }
        2 -> {
            // Show user profile page
            UserProfileFragment()
        }
        else -> ContactsFragment()
    }

    override fun getItemCount() = TAB_TITLES.size
}