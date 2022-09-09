package com.bpb.android.clips.repository.database

import androidx.room.*
import com.bpb.android.clips.repository.data.clips.model.Clips

@Dao
interface ClipsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClips(vararg clips: Clips)

    @Query("SELECT * FROM clips")
    fun getAllClips(): List<Clips>

    @Query(
        "UPDATE clips SET likes = likes + 1, isLiked = 1 " +
                "WHERE clipId = :clipId"
    )
    suspend fun incrementLikes(clipId: Long)

    @Query(
        "UPDATE clips SET likes = likes - 1,  isLiked = 0  " +
                "WHERE clipId = :clipId"
    )
    suspend fun decrementLikes(clipId: Long)

    @Update
    suspend fun updateComments(clips: Clips)

    @Delete
    suspend fun delete(clip: Clips)
}