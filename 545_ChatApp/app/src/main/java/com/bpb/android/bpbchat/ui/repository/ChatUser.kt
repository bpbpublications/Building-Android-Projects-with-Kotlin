package com.bpb.android.bpbchat.ui.repository

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatUser(
    val uid: String = "",
    val displayName: String = "",
    val lastSeen: Long = System.currentTimeMillis(),
    var photoUrl: String? = ""
) : Parcelable
