package com.bpb.android.clips.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.bpb.android.clips.BpbClipsApp
import com.bpb.android.clips.BpbClipsBaseFragment
import com.bpb.android.clips.R
import com.bpb.android.clips.repository.data.clips.model.Clips
import com.bpb.android.clips.ui.utils.CacheUtils
import com.bpb.android.clips.ui.utils.ClipsViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BpbClipsBaseFragment(R.layout.fragment_home),
    OnClipsListener {

    private val clipsViewModel by activityViewModels<ClipsViewModel> {
        ClipsViewModelFactory(
            (activity?.application as BpbClipsApp)
                .repository
        )
    }

    private lateinit var clipsPagerAdapter: ClipsPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Create adapter with empty data set
        clipsPagerAdapter = ClipsPagerAdapter(this, arrayListOf())
        vpClips.adapter = clipsPagerAdapter
        // Fetch clips
        clipsViewModel.getClips()
        // Observe for clips data. Update adapter to show newly fetched clips
        clipsViewModel.clipsLiveData.observe(viewLifecycleOwner) { clips ->
            val size = clipsPagerAdapter.dataList.size
            clipsPagerAdapter.dataList.addAll(clips)
            clipsPagerAdapter.notifyItemRangeInserted(size, clips.size)
            CacheUtils.startPreCaching(requireContext(), clips)
        }
    }

    override fun onLikeClicked(position: Int, liked: Boolean, clips: Clips) {
        // Update clips in database
        if (liked) {
            clipsViewModel.incrementLikes(clips.clipId)
        } else {
            clipsViewModel.decrementLikes(clips.clipId)
        }

        // Update clips in adapter. Note that we will not notify adapter
        // for this change. Notifying adapter will reloads the fragment
        // again and cause video to load and play again.
        clipsPagerAdapter.updateClipsAt(position, clips)
    }

    override fun onCommentsClicked(position: Int, clips: Clips) {
        clipsViewModel.addComments(clips)
        clipsPagerAdapter.updateClipsAt(position, clips)
    }
}

interface OnClipsListener {
    fun onLikeClicked(position: Int, liked: Boolean, clips: Clips)
    fun onCommentsClicked(position: Int, clips: Clips)
}