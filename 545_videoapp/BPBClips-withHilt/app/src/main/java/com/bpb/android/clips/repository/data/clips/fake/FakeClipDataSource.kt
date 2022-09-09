package com.bpb.android.clips.repository.data.clips.fake

import com.bpb.android.clips.repository.data.Result
import com.bpb.android.clips.repository.data.clips.ClipsDataSource
import com.bpb.android.clips.repository.data.clips.model.ClipsModel
import javax.inject.Inject

class FakeClipDataSource @Inject constructor(private val mock: FakeData) : ClipsDataSource {
    override suspend fun getClips(): Result<List<ClipsModel>> {
        return Result.success(mock.loadMockData())
    }
}