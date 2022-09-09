package com.bpb.android.clips.repository.network.api

import com.bpb.android.clips.repository.data.clips.model.ClipsModel
import com.bpb.android.clips.repository.network.ApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ClipsApi {
    @GET("/v1/clips")
    fun getClips(): Deferred<ApiResponse<List<ClipsModel>>>
}