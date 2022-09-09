package com.bpb.android.bpbchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bpb.android.bpbchat.ui.login.ChatAuthStateListener
import com.bpb.android.bpbchat.ui.login.LoginFragment
import com.bpb.android.bpbchat.ui.main.ChatLandingFragment
import com.bpb.android.bpbchat.ui.utils.replaceFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), ChatAuthStateListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        showFragment()
    }

    override fun onAuthStateChanged() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            showFragment()
        }
    }

    private fun showFragment() {
        // If user is not logged-in then show login page,
        // then show login page, else show home page.
        if (FirebaseAuth.getInstance().currentUser == null) {
            replaceFragment(R.id.fragmentContainer, LoginFragment())
        } else {
            replaceFragment(R.id.fragmentContainer, ChatLandingFragment())
        }
    }

    override fun onBackPressed() {
        // To handle back press to avoid blank screen after last fragment.
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}
