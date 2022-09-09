package com.bpb.android.clips.repository.data.clips.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "clips")
data class Clips(
    @PrimaryKey(autoGenerate = true) val clipId: Long,
    @ColumnInfo(name = "url") var clipUrl: String? = null,
    @ColumnInfo(name = "thumbUrl") val thumbUrl: String? = null,
    @ColumnInfo(name = "desc") val description: String? = null,
    @ColumnInfo(name = "musicTitle") val coverTitle: String? = null,
    @ColumnInfo(name = "musicImageUrl") val coverImageLink: String? = null,
    @ColumnInfo(name = "userid") val userId: String? = null,
    @ColumnInfo(name = "profilePicUrl") val profilePicUrl: String? = null,
    @ColumnInfo(name = "userName") val userName: String? = null,
    @ColumnInfo(name = "likes") var likesCount: Long = 0,
    @ColumnInfo(name = "isLiked") var isLiked: Boolean = false,
    @ColumnInfo(name = "comments") var commentsCount: Long = 0,
    @ColumnInfo(name = "createdOn")
    var addedWhen: Long? = System.currentTimeMillis()
) : Parcelable