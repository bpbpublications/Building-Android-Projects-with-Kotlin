package com.bpb.android.clips.ui.addclips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bpb.android.clips.repository.data.clips.ClipsRepository
import com.bpb.android.clips.repository.data.clips.model.Clips
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddClipsViewModel(
    private val repository: ClipsRepository
) : ViewModel() {

    enum class State { IN_PROGRESS, ERROR, SUCCESS }

    private val _progressState = MutableLiveData<State>()
    val progressState: LiveData<State> = _progressState

    fun createClips(clips: Clips) = viewModelScope.launch(Dispatchers.IO) {
        _progressState.postValue(State.IN_PROGRESS)
        repository.insertClips(clips)
        _progressState.postValue(State.SUCCESS)
    }
}