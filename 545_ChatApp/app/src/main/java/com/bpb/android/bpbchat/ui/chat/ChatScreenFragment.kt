package com.bpb.android.bpbchat.ui.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.bpb.android.bpbchat.R
import com.bpb.android.bpbchat.ui.repository.ChatUser
import com.bpb.android.bpbchat.ui.utils.getChatRoot
import com.firebase.ui.auth.util.ui.ImeHelper
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_chat.*

/**
 * A [DialogFragment] which shows chat screen. You can use normal fragment as well.
 */
class ChatScreenFragment : DialogFragment(R.layout.fragment_chat) {
    private lateinit var currentUser: ChatUser // Current user
    private lateinit var chatWith: ChatUser // User which will receive the chat
    private val chatsHeadRef = FirebaseDatabase.getInstance().reference.child(
        "chats_pk"
    )
    private lateinit var chatQuery: Query

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readArguments()
        chatQuery = chatsHeadRef.child(
            getChatRoot(currentUser.uid, chatWith.uid)
        ).limitToLast(50)

        setupToolbar()
        setupSendMsgView()
        attachRecyclerViewAdapter()
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener { dismiss() }
        userName.text = chatWith.displayName
        if (!chatWith.photoUrl.isNullOrEmpty()) {
            Picasso.get().load(chatWith.photoUrl).placeholder(R.drawable.ic_anon_user_48dp)
                .into(contactPic)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setupSendMsgView() {
        ImeHelper.setImeOnDoneListener(msgEditText) { onSendMsgClick() }
        msgTextInputLayout.setEndIconOnClickListener { onSendMsgClick() }
    }

    private fun onSendMsgClick() {
        onAddMessage(
            ChatModel(
                senderUid = currentUser.uid,
                receiverUid = chatWith.uid,
                message = msgEditText.text.toString(),
                timestamp = System.currentTimeMillis()
            )
        )
        msgEditText.setText("")
    }

    private fun readArguments() {
        arguments?.let { args ->
            args.getParcelable<ChatUser>(EXTRA_CHAT_WITH)?.let { chatWith = it }
            args.getParcelable<ChatUser>(EXTRA_CURRENT_USER)?.let { currentUser = it }
        }
    }

    private fun onAddMessage(chat: ChatModel) {
        chatQuery.ref.push().setValue(chat) { error: DatabaseError?, _: DatabaseReference? ->
            if (error != null) {
                Toast.makeText(requireContext(), "Failed to write message", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun attachRecyclerViewAdapter() {
        val options: FirebaseRecyclerOptions<ChatModel> =
            FirebaseRecyclerOptions.Builder<ChatModel>()
                .setQuery(chatQuery, ChatModel::class.java)
                .setLifecycleOwner(this)
                .build()

        val adapter = ChatScreenAdapter(options, null)

        // Scroll to bottom on new messages
        adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                messageRecyclerView.smoothScrollToPosition(adapter.itemCount)
            }
        })
        messageRecyclerView.adapter = adapter
    }

    override fun getTheme(): Int {
        return R.style.Dialog_App_Fullscreen
    }

    companion object {
        private const val EXTRA_CURRENT_USER = "com.bpb.android.bpbchat.EXTRA_CURRENT_USER"
        private const val EXTRA_CHAT_WITH = "com.bpb.android.bpbchat.EXTRA_CHAT_WITH"

        fun getInstance(currentUser: ChatUser, chatWith: ChatUser): ChatScreenFragment {
            val args = Bundle()
            args.putParcelable(EXTRA_CHAT_WITH, chatWith)
            args.putParcelable(EXTRA_CURRENT_USER, currentUser)
            val fragment = ChatScreenFragment()
            fragment.arguments = args
            return fragment
        }
    }
}