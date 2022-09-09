package com.bpb.android.clips.ui.utils

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bpb.android.clips.repository.data.clips.model.Clips

class CacheUtils {

    companion object {
        @JvmStatic
        fun startPreCaching(context: Context, clipsData: List<Clips>) {
            val urlList = arrayOfNulls<String>(clipsData.size)
            clipsData.mapIndexed { index, dataModel ->
                urlList[index] = dataModel.clipUrl
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