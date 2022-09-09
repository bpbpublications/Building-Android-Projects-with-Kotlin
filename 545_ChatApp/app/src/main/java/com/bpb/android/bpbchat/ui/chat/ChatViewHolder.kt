package com.bpb.android.bpbchat.ui.chat

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bpb.android.bpbchat.R
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.auth.FirebaseAuth

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.timeTv)
    private val msg: TextView = itemView.findViewById(R.id.messageTv)
    private val msgItemView: LinearLayout = itemView.findViewById(R.id.message)

    fun bind(chat: ChatModel) {
        name.text = chat.timestamp?.let {
            TimeAgo.using(it)
        } ?: run {
            TimeAgo.using(System.currentTimeMillis())
        }
        msg.text = chat.message
        val currentUser = FirebaseAuth.getInstance().currentUser
        setIsSender(currentUser != null && chat.senderUid == currentUser.uid)
    }

    private fun setIsSender(isSender: Boolean) {
        (msgItemView.layoutParams as FrameLayout.LayoutParams).gravity =
            if (isSender) Gravity.END else Gravity.START

        msgItemView.background = ContextCompat.getDrawable(
            msgItemView.context,
            if (isSender)
                R.drawable.shape_outgoing_msg
            else
                R.drawable.shape_incoming_msg
        )
    }
}