package com.bpb.android.clips.ui.search

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bpb.android.clips.BpbClipsBaseFragment
import com.bpb.android.clips.R

class SearchClipsFragment : BpbClipsBaseFragment(R.layout.fragment_search_clips) {
    private lateinit var viewModel: SearchClipsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchClipsViewModel::class.java)
    }
}