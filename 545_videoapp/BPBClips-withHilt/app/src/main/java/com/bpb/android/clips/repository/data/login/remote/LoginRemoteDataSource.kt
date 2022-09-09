package com.bpb.android.clips.repository.data.login.remote

import com.bpb.android.clips.repository.data.login.LoginDataSource
import com.bpb.android.clips.repository.data.login.model.LoggedInUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginRemoteDataSource : LoginDataSource {
    override fun createOrUpdateUser(user: LoggedInUser) {
        // Using firebase database KTX Lib
        val database = Firebase.database
        val userDbRef = database.getReference("clips_user")
        val userNodeRef = userDbRef.child(user.uid)
        userNodeRef.setValue(user)
    }
}