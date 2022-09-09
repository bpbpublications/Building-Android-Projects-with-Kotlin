package com.bpb.android.clips.repository.data.login.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Parcelize
data class LoggedInUser(
    val uid: String = "",
    val displayName: String = "",
    var photoUrl: String? = ""
) : Parcelable