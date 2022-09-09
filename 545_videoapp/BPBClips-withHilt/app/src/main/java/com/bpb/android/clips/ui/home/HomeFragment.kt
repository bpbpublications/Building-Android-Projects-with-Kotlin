package com.bpb.android.clips.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.bpb.android.clips.BpbClipsBaseFragment
import com.bpb.android.clips.R
import com.bpb.android.clips.ui.utils.CacheUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : BpbClipsBaseFragment(R.layout.fragment_home) {
    private val clipsViewModel by activityViewModels<ClipsViewModel>()

    private lateinit var clipsPagerAdapter: ClipsPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clipsPagerAdapter = ClipsPagerAdapter(this, arrayListOf())
        view_pager_stories.adapter = clipsPagerAdapter
        val liveData = clipsViewModel.getClipsLiveData()
        liveData.observe(viewLifecycleOwner) { clips ->

            val size = clipsPagerAdapter.dataList.size
            clipsPagerAdapter.dataList.addAll(clips)
            clipsPagerAdapter.notifyItemRangeInserted(size, clips.size)
            CacheUtils.startPreCaching(requireContext(), clips)
        }
    }
}