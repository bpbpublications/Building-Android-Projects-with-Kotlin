package com.bpb.android.bpbchat.ui.login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bpb.android.bpbchat.R
import com.bpb.android.bpbchat.ui.repository.UserRepository
import com.bpb.android.bpbchat.ui.utils.mapFromFirebaseUser
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var chatAuthStateListener: ChatAuthStateListener? = null

    // Choose authentication providers
    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
        // AuthUI.IdpConfig.FacebookBuilder().build(),
        // AuthUI.IdpConfig.TwitterBuilder().build()
    )

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ChatAuthStateListener) {
            chatAuthStateListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Logout to avoid autologin feature of Firebase Login
        FirebaseAuth.getInstance().signOut()
        doLogin()
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            // On success add this user to chat user firebase database
            FirebaseAuth.getInstance().currentUser?.also { user ->
                UserRepository().createOrUpdateUser(
                    mapFromFirebaseUser(user)
                )
                chatAuthStateListener?.onAuthStateChanged()
            } ?: showError(
                requireActivity(),
                "Something went wrong"
            )
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            if (result.idpResponse == null) {
                requireActivity().finish()
            } else {
                result.idpResponse?.error?.message?.let {
                    showError(requireActivity(), it)
                }
            }
        }
    }

    private fun doLogin() {

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            // .setLogo(R.drawable.logo)
            .setTheme(R.style.ChatAppLogin)
            .setAlwaysShowSignInMethodScreen(true)
            .setIsSmartLockEnabled(false)
            .setTosAndPrivacyPolicyUrls(
                "https://in.bpbonline.com/policies/terms-of-service",
                "https://in.bpbonline.com/pages/privacy-policy"
            )
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun showError(activity: Activity, errorMsg: String) {
        val builder = AlertDialog.Builder(activity)
        builder.apply {
            setPositiveButton(
                R.string.retry
            ) { dialog, id ->
                doLogin()
            }
            setNegativeButton(
                R.string.exit
            ) { dialog, id ->
                activity.finish()
            }
            setMessage(errorMsg)
        }
        // Create the AlertDialog
        builder.create().show()
    }
}