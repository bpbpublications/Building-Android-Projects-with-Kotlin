package com.bpb.android.clips.repository.data.clips.source.remote

import com.bpb.android.clips.repository.data.Result
import com.bpb.android.clips.repository.data.clips.ClipsDataSource
import com.bpb.android.clips.repository.data.clips.model.Clips
import com.bpb.android.clips.repository.ext.getResult
import com.bpb.android.clips.repository.network.api.ClipsApi

class ClipsRemoteDataSource(
    private val api: ClipsApi
) : ClipsDataSource {
    override suspend fun getClips(): List<Clips> {

        return when (val result = api.getClipsAsync().getResult()) {
            is Result.Success -> result.data
            else -> listOf()
        }
    }

    override suspend fun insertClips(vararg clips: Clips) {
        api.insertClips(*clips)
    }

    override suspend fun incrementLikes(clipId: Long) {
        api.incrementLikes(clipId)
    }

    override suspend fun decrementLikes(clipId: Long) {
        api.decrementLikes(clipId)
    }

    override suspend fun updateComments(clips: Clips) {
        api.updateComments(clips)
    }
}