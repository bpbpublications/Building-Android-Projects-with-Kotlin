package com.bpb.android.clips.ui.discover

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bpb.android.clips.BpbClipsBaseFragment
import com.bpb.android.clips.R

class DiscoverFragment : BpbClipsBaseFragment(R.layout.fragment_discover) {
    private lateinit var viewModel: DiscoverViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DiscoverViewModel::class.java)
    }
}