package com.bpb.android.clips.repository.data.login

import com.bpb.android.clips.repository.data.login.model.LoggedInUser

class LoginRepository(private val dataSource: LoginDataSource) : LoginDataSource {
    override fun createOrUpdateUser(user: LoggedInUser) = dataSource.createOrUpdateUser(user)
}