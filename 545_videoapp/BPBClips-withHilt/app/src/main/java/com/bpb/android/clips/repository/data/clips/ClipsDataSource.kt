package com.bpb.android.clips.repository.data.clips

import com.bpb.android.clips.repository.data.Result
import com.bpb.android.clips.repository.data.clips.model.ClipsModel

interface ClipsDataSource {
    suspend fun getClips(): Result<List<ClipsModel>>
}