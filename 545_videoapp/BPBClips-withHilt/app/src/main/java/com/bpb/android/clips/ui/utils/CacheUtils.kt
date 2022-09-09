package com.bpb.android.clips.ui.utils

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bpb.android.clips.repository.data.clips.model.ClipsModel

class CacheUtils {

    companion object {
        @JvmStatic
        fun startPreCaching(context: Context, dataList: List<ClipsModel>) {
            val urlList = arrayOfNulls<String>(dataList.size)
            dataList.mapIndexed { index, storiesDataModel ->
                urlList[index] = storiesDataModel.clipUrl
            }
            val inputData =
                Data.Builder().putStringArray(Constants.KEY_CLIPS_LIST_DATA, urlList).build()
            val preCachingWork =
                OneTimeWorkRequestBuilder<ClipsCachingCoroutineWorker>().setInputData(inputData)
                    .build()
            WorkManager.getInstance(context)
                .enqueue(preCachingWork)
        }
    }
}