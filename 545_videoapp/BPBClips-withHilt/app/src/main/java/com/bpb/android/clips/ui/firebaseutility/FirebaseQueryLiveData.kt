package com.bpb.android.clips.ui.firebaseutility

import android.util.Log
import androidx.lifecycle.LiveData
import com.bpb.android.clips.repository.data.clips.model.ClipsModel
import com.google.firebase.database.*

class FirebaseQueryLiveData(private val query: Query) : LiveData<MutableList<ClipsModel>>() {
    companion object {
        private val TAG = FirebaseQueryLiveData::class.java.name
    }

    private val listenerReg = DataEventListener()

    private inner class DataEventListener : ValueEventListener {
        override fun onCancelled(err: DatabaseError) {
            Log.e("PANKAJ", ": $TAG -> $err")
        }

        override fun onDataChange(ds: DataSnapshot) {
            val clips = mutableListOf<ClipsModel>()
            for (data in ds.children){
                val listData = data?.getValue(ClipsModel::class.java)
                clips.add(listData ?: return)
            }
            value = clips
        }

    }

    override fun onActive() {
        super.onActive()
        query.addValueEventListener(listenerReg)
    }

    override fun onInactive() {
        super.onInactive()
        query.removeEventListener(listenerReg)
    }
}