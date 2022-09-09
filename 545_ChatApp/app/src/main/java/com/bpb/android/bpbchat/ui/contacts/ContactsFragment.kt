package com.bpb.android.bpbchat.ui.contacts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bpb.android.bpbchat.R
import com.bpb.android.bpbchat.ui.chat.ChatScreenFragment
import com.bpb.android.bpbchat.ui.repository.ChatUser
import com.bpb.android.bpbchat.ui.utils.mapFromFirebaseUser
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_contacts.*

class ContactsFragment : Fragment(R.layout.fragment_contacts) {
    private val userQuery =
        FirebaseDatabase.getInstance().getReference("users_2").limitToLast(50)

    private lateinit var adapter: ContactsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
    }

    /*
    Ask adapter monitor changes to the Firebase query automatically.
    So FirebaseUI will automatically start and stop listening in
    onStart() and onStop().*/

    private fun prepareRecyclerView() {
        // Config for FirebaseRecyclerAdapter
        val options = FirebaseRecyclerOptions.Builder<ChatUser>()
            .setLifecycleOwner(this)
            .setQuery(userQuery, ChatUser::class.java)
            .build()

        adapter = ContactsAdapter(options,
            object : ContactsAdapter.OnItemClickListener {
                override fun onItemClick(chatWith: ChatUser) {
                    // Show ChatScreenFragment, where user can start chatting
                    FirebaseAuth.getInstance().currentUser?.let { user ->
                        val currentUser = mapFromFirebaseUser(user)
                        ChatScreenFragment.getInstance(
                            currentUser,
                            chatWith
                        ).show(
                            childFragmentManager,
                            ChatScreenFragment::class.java.simpleName
                        )
                    }
                }
            })

        // Common setup of RecyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        contactRecyclerView.layoutManager = layoutManager
        contactRecyclerView.setHasFixedSize(true)
        contactRecyclerView.adapter = adapter
        contactRecyclerView.addItemDecoration(
            DividerItemDecoration(
                contactRecyclerView.context,
                layoutManager.orientation
            )
        )
    }
}