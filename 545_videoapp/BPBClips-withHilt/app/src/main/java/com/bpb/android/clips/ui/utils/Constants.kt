package com.bpb.android.clips.ui.utils

import java.util.*

object Constants {
    val POST_FIX = object : TreeMap<Long, String>() {
        init {
            this[1_000L] = "k"
            this[1_000_000L] = "M"
            this[1_000_000_000L] = "B"
            this[1_000_000_000_000L] = "T"
            this[1_000_000_000_000_000L] = "P"
            this[1_000_000_000_000_000_000L] = "E"
        }
    }

    const val KEY_CLIPS_DATA = "key_clips_data"
    const val KEY_CLIPS_LIST_DATA = "key_clips_list_data"
}