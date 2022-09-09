package com.bpb.android.clips.repository.data.clips

class ClipsRepository(private val dataSource: ClipsDataSource) : ClipsDataSource {
    override suspend fun getClips() = dataSource.getClips()
}