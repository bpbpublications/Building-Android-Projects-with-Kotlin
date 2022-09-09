package com.bpb.android.bpbchat.ui.utils

import com.bpb.android.bpbchat.ui.repository.ChatUser
import com.google.firebase.auth.FirebaseUser

fun mapFromFirebaseUser(user: FirebaseUser): ChatUser {
    val id = user.uid
    val name = user.displayName ?: ""
    val lastSeen = System.currentTimeMillis()
    val photoUrl = user.photoUrl?.toString() ?: ""
    return ChatUser(id, name, lastSeen, photoUrl)
}

fun getChatRoot(senderId: String, receiverId: String): String {
    return if (senderId.compareTo(receiverId) > 1) {
        "${senderId}_${receiverId}"
    } else {
        "${receiverId}_${senderId}"
    }
}

interface FetchChatUserListener {
    fun onFetchUser(chatUser: ChatUser)
}