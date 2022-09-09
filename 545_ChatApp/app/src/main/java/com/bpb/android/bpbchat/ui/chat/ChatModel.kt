package com.bpb.android.bpbchat.ui.chat

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ChatModel(
    val senderUid: String? = null,
    val receiverUid: String? = null,
    val message: String? = null,
    val timestamp: Long? = null
)