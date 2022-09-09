package com.bpb.android.bpbchat.ui.repository

import com.bpb.android.bpbchat.ui.utils.FetchChatUserListener
import com.bpb.android.bpbchat.ui.utils.mapFromFirebaseUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

val CHAT_USER_TABLE = "users"

class UserRepository {

    // get access to the root folder of the storage
    private val rootRef = FirebaseStorage.getInstance().reference

    //create a new folder called user_feedback inside the root folder
    private val feedbackRef = rootRef.child("user_feedback")

    fun createOrUpdateUser(user: ChatUser) {
        // Using firebase database KTX Lib
        val database = Firebase.database
        val userDbRef = database.getReference("users_2")
        val userNodeRef = userDbRef.child(user.uid)
        userNodeRef.setValue(user)
    }

    fun getCurrentUserByUid(firebaseUser: FirebaseUser, listener: FetchChatUserListener) {
        // Using firebase database KTX Lib
        val database = Firebase.database
        val userDbRef = database.getReference("users_2")
        userDbRef.child(firebaseUser.uid).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get user value
                    val user = dataSnapshot.getValue<ChatUser>()
                        ?: mapFromFirebaseUser(FirebaseAuth.getInstance().currentUser!!)
                    listener.onFetchUser(user)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    val user = mapFromFirebaseUser(FirebaseAuth.getInstance().currentUser!!)
                    listener.onFetchUser(user)
                }
            })
    }
}