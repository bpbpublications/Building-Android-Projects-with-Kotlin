package com.bpb.android.clips.ui.home

import androidx.lifecycle.ViewModel
import com.bpb.android.clips.ui.firebaseutility.FirebaseQueryLiveData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClipsViewModel @Inject constructor() : ViewModel() {

    private val clips = FirebaseQueryLiveData(Firebase.database.getReference("clips"))
    fun getClipsLiveData(): FirebaseQueryLiveData {
        return clips
    }
}