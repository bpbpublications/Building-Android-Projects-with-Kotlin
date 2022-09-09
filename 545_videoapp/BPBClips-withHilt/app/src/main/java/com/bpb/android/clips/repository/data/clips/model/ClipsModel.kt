package com.bpb.android.clips.repository.data.clips.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class ClipsModel(
    @SerializedName("clipId") val clipId: String? = null,
    @SerializedName("clipUrl") var clipUrl: String? = null,
    @SerializedName("clipThumbUrl") val clipThumbUrl: String? = null,
    @SerializedName("clipDescription") val clipDescription: String? = null,
    @SerializedName("musicCoverTitle") val musicCoverTitle: String? = null,
    @SerializedName("musicCoverImageLink") val musicCoverImageLink: String? = null,
    @SerializedName("userId") val userId: String? = null,
    @SerializedName("userProfilePicUrl") val userProfilePicUrl: String? = null,
    @SerializedName("userName") val userName: String? = null,
    @SerializedName("likesCount") var likesCount: Long? = null,
    @SerializedName("commentsCount") var commentsCount: Long? = null,
    @SerializedName("addedWhen") var addedWhen: Long? = System.currentTimeMillis()
) : Parcelable
