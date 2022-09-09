package com.bpb.android.clips.repository.data.clips

import com.bpb.android.clips.repository.data.clips.model.Clips

class ClipsRepository(
    private val dataSource: ClipsDataSource
) {

    suspend fun getClips() = dataSource.getClips()
    suspend fun insertClips(vararg clips: Clips) {
        dataSource.insertClips(*clips)
    }

    suspend fun incrementLikes(clipId: Long) {
        dataSource.incrementLikes(clipId)
    }

    suspend fun decrementLikes(clipId: Long) {
        dataSource.decrementLikes(clipId)
    }

    suspend fun updateComments(clips: Clips) {
        dataSource.updateComments(clips)
    }
}