package com.bpb.android.clips.repository.data.clips.remote

import com.bpb.android.clips.repository.data.Result
import com.bpb.android.clips.repository.data.clips.ClipsDataSource
import com.bpb.android.clips.repository.data.clips.model.ClipsModel
import com.bpb.android.clips.repository.ext.getResult
import com.bpb.android.clips.repository.network.api.ClipsApi

class ClipsRemoteDataSource(private val api: ClipsApi) : ClipsDataSource {
    override suspend fun getClips(): Result<List<ClipsModel>> {
        return api.getClips().getResult()
    }
}