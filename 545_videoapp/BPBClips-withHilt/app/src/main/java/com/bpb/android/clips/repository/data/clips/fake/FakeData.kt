package com.bpb.android.clips.repository.data.clips.fake

import android.content.Context
import com.bpb.android.clips.R
import com.bpb.android.clips.repository.data.clips.model.ClipsModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class FakeData @Inject constructor(private val context: Context) {
    fun loadMockData(): ArrayList<ClipsModel> {
        val fakeData = context.resources.openRawResource(R.raw.clips_data)
        val jsonString = fakeData.bufferedReader().readText()
        val gson = Gson()
        val clipsType = object : TypeToken<ArrayList<ClipsModel>>() {}.type
        return gson.fromJson(jsonString, clipsType)
    }
}