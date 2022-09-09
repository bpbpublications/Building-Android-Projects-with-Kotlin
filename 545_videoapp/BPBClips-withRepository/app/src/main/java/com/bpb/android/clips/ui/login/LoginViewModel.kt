package com.bpb.android.clips.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.bpb.android.clips.repository.data.login.LoginRepository
import com.bpb.android.clips.repository.data.Result

import com.bpb.android.clips.R
import com.bpb.android.clips.repository.data.login.model.LoggedInUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _profileInfo = MutableLiveData<LoggedInUser?>()
    val profileInfo: LiveData<LoggedInUser?> = _profileInfo

    fun createOrUpdateUser(userInfo: LoggedInUser) {
        // loginRepository.createOrUpdateUser(userInfo)
        // Using firebase database KTX Lib
        val database = Firebase.database
        val userDbRef = database.getReference("clips_user")
        val userNodeRef = userDbRef.child(userInfo.uid)
        userNodeRef.setValue(userInfo)
    }

    fun getCurrentUserByUid(uid: String) {
        // Using firebase database KTX Lib
        val database = Firebase.database
        val userDbRef = database.getReference("clips_user")

        userDbRef.child(uid).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get user value
                    val user = dataSnapshot.getValue<LoggedInUser>()
                    _profileInfo.postValue(user)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    return _profileInfo.postValue(null)
                }
            })
    }
}