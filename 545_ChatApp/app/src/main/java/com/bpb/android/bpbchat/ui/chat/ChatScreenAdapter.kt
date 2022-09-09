package com.bpb.android.bpbchat.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bpb.android.bpbchat.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class ChatScreenAdapter(
    private val options: FirebaseRecyclerOptions<ChatModel>,
    private val onItemClickListener: OnItemClickListener?
) : FirebaseRecyclerAdapter<ChatModel, ChatViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val holder = ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_view_chat,
                parent,
                false
            )
        )

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(options.snapshots[holder.adapterPosition])
        }

        return holder
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int, chat: ChatModel) {
        holder.bind(chat)
    }

    override fun onDataChanged() {
        // If there are no chat messages, show a view that invites the user to add a message.
        // emptyTextView.setVisibility(if (itemCount == 0) View.VISIBLE else View.GONE)
    }

    interface OnItemClickListener {
        fun onItemClick(chatWith: ChatModel)
    }
}