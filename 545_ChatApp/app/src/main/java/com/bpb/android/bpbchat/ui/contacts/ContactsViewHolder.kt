package com.bpb.android.bpbchat.ui.contacts

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bpb.android.bpbchat.R
import com.bpb.android.bpbchat.ui.repository.ChatUser
import com.squareup.picasso.Picasso

class ContactsViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val name: TextView = itemView.findViewById(R.id.contactTextView)
    private val pic: ImageView = itemView.findViewById(R.id.ivUserImage)

    fun bind(user: ChatUser) {
        name.text = user.displayName
        user.photoUrl?.let { _photoUrl ->
            if (_photoUrl.isNotEmpty()) {
                Picasso.get().load(_photoUrl)
                    .placeholder(R.drawable.ic_anon_user_48dp)
                    .into(pic)
            }
        }
    }
}