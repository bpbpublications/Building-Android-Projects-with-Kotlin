package com.bpb.android.clips.repository.network.api

import com.bpb.android.clips.repository.data.clips.model.Clips
import com.bpb.android.clips.repository.network.ApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ClipsApi {
    @GET("/v1/clips")
    fun getClipsAsync(): Deferred<ApiResponse<List<Clips>>>
    fun insertClips(vararg clips: Clips)
    fun incrementLikes(clipId: Long)
    fun decrementLikes(clipId: Long)
    fun updateComments(clips: Clips)
    fun delete(clip: Clips)
}