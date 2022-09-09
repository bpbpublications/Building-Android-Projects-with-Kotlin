package com.bpb.android.clips.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bpb.android.clips.BpbClipsBaseFragment
import com.bpb.android.clips.R
import com.bpb.android.clips.repository.data.login.model.LoggedInUser
import com.bpb.android.clips.ui.login.LoginActivity
import com.bpb.android.clips.ui.login.LoginViewModel
import com.bpb.android.clips.ui.login.LoginViewModelFactory
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BpbClipsBaseFragment(R.layout.fragment_profile) {

    private var profilePicUri: Uri? = null
    private lateinit var viewModel: LoginViewModel
    private var loggedInUser: LoggedInUser? = null

    private var profilePicChooser =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { imageUri ->
                    ivUserImage.setImageURI(imageUri.data)
                    profilePicUri = imageUri.data
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getCurrentUserByUid(FirebaseAuth.getInstance().currentUser.uid)
        viewModel.profileInfo.observe(viewLifecycleOwner, {
            if (it != null) {
                showUserDetails(it, FirebaseAuth.getInstance().currentUser)
            }
        })

        ivUserImage.setOnClickListener {
            chooseProfilePicture()
        }

        btnUpdateImage.setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.uid?.let { userId ->
                updateProfileImageOnStorage(userId)
            }
        }

        nameTextInputLayout.setEndIconOnClickListener {
            updateName()
        }

        emailTextInputLayout.setEndIconOnClickListener {
            updateEmail()
        }

        etPasswordTextInputLayout.setEndIconOnClickListener {
            // update the password here
            FirebaseAuth.getInstance().currentUser?.updatePassword(etPassword.text.toString())
        }

        logout.setOnClickListener {
            AuthUI.getInstance().signOut(requireContext())
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
    }

    private fun showUserDetails(loggedOnUser: LoggedInUser, firebaseUser: FirebaseUser) {
        loggedInUser = loggedOnUser
        etName.setText(loggedOnUser.displayName)
        loggedOnUser.photoUrl?.let { _photoUrl ->
            if (_photoUrl.isNotEmpty()) {
                Picasso.get().load(_photoUrl).placeholder(R.drawable.ic_anon_user_48dp)
                    .into(ivUserImage)
            }
        }

        etEmail.setText(firebaseUser.email)
        pageProgress.isVisible = false
    }

    private fun updateName() {
        if (TextUtils.isEmpty(etName.text)) {
            etName.error = "Name can not be empty."
            return
        }
        etName.error = null

        // update the name here
        val nameUpdate = UserProfileChangeRequest.Builder()
            .setDisplayName(etName.text.toString())
            .build()

        FirebaseAuth.getInstance().currentUser?.updateProfile(nameUpdate)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUserAtRealtimeDatabase()
                }
            }
    }

    private fun updateEmail() {
        // update the email here
        etEmail.error = null
        etEmail.text?.let { email ->
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                FirebaseAuth.getInstance().currentUser?.updateEmail(etEmail.text.toString())
            } else {
                etEmail.error = "Email is not valid."
            }
        }
    }

    private fun chooseProfilePicture() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        profilePicChooser.launch(intent)
    }

    private fun updateUserAtRealtimeDatabase(photoUri: String = loggedInUser?.photoUrl ?: "") {
        val user = LoggedInUser(
            displayName = etName.text.toString(),
            uid = FirebaseAuth.getInstance().currentUser!!.uid,
            photoUrl = photoUri
        )

        viewModel.createOrUpdateUser(user)
    }

    private fun updateProfileImageOnStorage(userId: String) {
        pageProgress.isVisible = true
        val ref: StorageReference = FirebaseStorage.getInstance().reference
            .child("profile_pics/${userId}")

        profilePicUri?.let { profileUri ->
            ref.putFile(profileUri)
                .addOnSuccessListener {
                    // Picture has been updated to Database.
                    // Get uri of that picture and update it to user profile,
                    // so we can show new image on profile page from now
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        updateFirebaseUserPicture(uri)
                        updateUserAtRealtimeDatabase(uri.toString())
                        pageProgress.isVisible = false
                    }
                }
                .addOnFailureListener { e -> // Error, Image not uploaded
                    // progressDialog.dismiss()
                }
        }
    }

    private fun updateFirebaseUserPicture(uri: Uri) {
        val picUpdate = UserProfileChangeRequest.Builder()
            .setPhotoUri(uri)
            .build()

        FirebaseAuth.getInstance().currentUser?.updateProfile(picUpdate)
        /*?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updateUserAtRealtimeDatabase()
            }
        }*/
    }
}