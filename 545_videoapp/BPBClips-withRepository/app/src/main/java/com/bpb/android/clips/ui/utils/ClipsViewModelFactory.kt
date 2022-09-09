package com.bpb.android.clips.ui.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bpb.android.clips.repository.data.clips.ClipsRepository
import com.bpb.android.clips.ui.addclips.AddClipsViewModel
import com.bpb.android.clips.ui.home.ClipsViewModel

class ClipsViewModelFactory(
    private val repository: ClipsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddClipsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddClipsViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(ClipsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClipsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}