package com.bpb.android.clips.ui.login

import android.content.Intent
import android.os.Bundle
import com.bpb.android.clips.BpbClipsBaseActivity
import com.bpb.android.clips.R
import com.bpb.android.clips.ui.main.MainActivity
import com.bpb.android.clips.ui.utils.replaceFragment
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BpbClipsBaseActivity(), AuthStateListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)
        navigate()
    }

    private fun navigate() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            replaceFragment(R.id.loginContainer, LoginFragment())
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onAuthStateChanged() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}