package com.bpb.android.clips.ui.addclips

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bpb.android.clips.repository.data.clips.model.ClipsModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class AddClipsViewModel : ViewModel() {

    enum class State { IN_PROGRESS, ERROR, SUCCESS }

    private val _progressState = MutableLiveData<State>()
    val progressState: LiveData<State> = _progressState

    fun uploadClipsFile(clipsLocalUri: Uri, clipsInfo: ClipsModel) {
        _progressState.postValue(State.IN_PROGRESS)
        val ref = FirebaseStorage.getInstance().reference.child(
            "clips/${System.currentTimeMillis()}"
        )
        ref.putFile(clipsLocalUri)
            .addOnSuccessListener {
                // Clips has been updated to cloud storage.
                // Get uri of that clip and add clips to
                // Realtime Database
                ref.downloadUrl.addOnSuccessListener { uri ->
                    clipsInfo.clipUrl = uri.toString()
                    addClips(clipsInfo)
                }
            }
            .addOnFailureListener { e -> // Error, Image not uploaded
                _progressState.postValue(State.ERROR)
            }
    }

    private fun addClips(clipsInfo: ClipsModel) {
        val database = Firebase.database
        val clipsRef = database.getReference("clips")
        clipsRef.push().setValue(clipsInfo)
            .addOnSuccessListener {
                _progressState.postValue(State.SUCCESS)
            }
            .addOnFailureListener {
                _progressState.postValue(State.ERROR)
            }
    }
}