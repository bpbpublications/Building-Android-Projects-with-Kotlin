package com.bpb.android.clips.ui.home

import androidx.lifecycle.*
import com.bpb.android.clips.repository.data.clips.ClipsRepository
import com.bpb.android.clips.repository.data.clips.model.Clips
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClipsViewModel(
    private val repository: ClipsRepository
) : ViewModel() {

    private val _clipsLiveData = MutableLiveData<List<Clips>>()
    val clipsLiveData: LiveData<List<Clips>> = _clipsLiveData

    fun incrementLikes(clipsId: Long) = viewModelScope.launch {
        repository.incrementLikes(clipsId)
    }

    fun decrementLikes(clipsId: Long) = viewModelScope.launch {
        repository.decrementLikes(clipsId)
    }

    fun getClips() = viewModelScope.launch(Dispatchers.IO) {
        val clips = repository.getClips()
        _clipsLiveData.postValue(clips)
    }

    fun addComments(clips: Clips) = viewModelScope.launch {
        repository.updateComments(clips)
    }
}