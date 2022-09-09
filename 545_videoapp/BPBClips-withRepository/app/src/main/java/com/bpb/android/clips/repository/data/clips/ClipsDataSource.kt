package com.bpb.android.clips.repository.data.clips

import com.bpb.android.clips.repository.data.clips.model.Clips

interface ClipsDataSource {
    suspend fun getClips(): List<Clips>
    suspend fun insertClips(vararg clips: Clips)
    suspend fun incrementLikes(clipId: Long)
    suspend fun decrementLikes(clipId: Long)
    suspend fun updateComments(clips: Clips)
}