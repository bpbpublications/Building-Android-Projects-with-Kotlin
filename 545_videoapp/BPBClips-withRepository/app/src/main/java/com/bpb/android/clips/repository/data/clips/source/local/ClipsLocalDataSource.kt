package com.bpb.android.clips.repository.data.clips.source.local

import com.bpb.android.clips.repository.data.clips.ClipsDataSource
import com.bpb.android.clips.repository.data.clips.model.Clips
import com.bpb.android.clips.repository.database.ClipsDao

class ClipsLocalDataSource(
    private val clipsDao: ClipsDao
) : ClipsDataSource {

    override suspend fun getClips(): List<Clips> {
        return clipsDao.getAllClips()
    }

    override suspend fun insertClips(vararg clips: Clips) {
        return clipsDao.insertClips(*clips)
    }

    override suspend fun incrementLikes(clipId: Long) {
        return clipsDao.incrementLikes(clipId)
    }

    override suspend fun decrementLikes(clipId: Long) {
        return clipsDao.decrementLikes(clipId)
    }

    override suspend fun updateComments(clips: Clips) {
        return clipsDao.updateComments(clips)
    }
}