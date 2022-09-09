package com.bpb.android.clips.repository.data.login

import com.bpb.android.clips.repository.data.login.model.LoggedInUser

interface LoginDataSource {
    fun createOrUpdateUser(user: LoggedInUser)
    // fun getCurrentUserByUid(userId: String)
}