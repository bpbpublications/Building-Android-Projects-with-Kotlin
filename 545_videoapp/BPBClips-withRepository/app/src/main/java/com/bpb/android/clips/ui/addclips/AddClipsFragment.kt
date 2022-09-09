package com.bpb.android.clips.ui.addclips

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bpb.android.clips.BpbClipsApp
import com.bpb.android.clips.BpbClipsBaseFragment
import com.bpb.android.clips.R
import com.bpb.android.clips.repository.data.clips.model.Clips
import com.bpb.android.clips.ui.main.MainActivity
import com.bpb.android.clips.ui.utils.ClipsViewModelFactory
import com.bpb.android.clips.ui.utils.showToast
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_add_clips.*

class AddClipsFragment : BpbClipsBaseFragment(R.layout.fragment_add_clips) {

    private val viewModel by activityViewModels<AddClipsViewModel> {
        ClipsViewModelFactory((activity?.application as BpbClipsApp).repository)
    }

    private val user = FirebaseAuth.getInstance().currentUser
    private lateinit var player: SimpleExoPlayer
    private var currentLocalUri: Uri? = null
    private var captureVideoIntentLaunch = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result?.data?.data?.let {
                currentLocalUri = it
                playVideo(it)
            }
        } else {
            requireContext().showToast("Something went wrong")
            redirectToHomeScreen()
        }
    }

    private fun initializePlayer() {
        if (!::player.isInitialized) {
            player = ExoPlayerFactory.newSimpleInstance(
                context,
                DefaultRenderersFactory(context),
                DefaultTrackSelector()
            )

            playerView.player = player
        }
        player.playWhenReady = true
    }

    private fun playVideo(uri: Uri) {
        val mediaSource: MediaSource = buildMediaSource(uri)
        player.prepare(mediaSource, true, false)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val datasourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(context, Util.getUserAgent(context, "BPB Clips"))
        return ExtractorMediaSource.Factory(datasourceFactory).createMediaSource(uri)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializePlayer()
        captureVideo()
        initListeners()
    }

    private fun initListeners() {
        closePage.setOnClickListener { }
        retakeVideo.setOnClickListener { captureVideo() }
        uploadClips.setOnClickListener {
            currentLocalUri?.let {
                viewModel.createClips(prepareClips(it))
            }
        }
        viewModel.progressState.observe(viewLifecycleOwner) { state ->
            pageProgress.isVisible = state == AddClipsViewModel.State.IN_PROGRESS

            when (state) {
                AddClipsViewModel.State.SUCCESS -> {
                    redirectToHomeScreen()
                }
                AddClipsViewModel.State.ERROR -> {
                    requireContext().showToast("Something went wrong. Please try again")
                }
            }
        }
    }

    private fun prepareClips(uri: Uri): Clips {
        return Clips(
            clipId = System.currentTimeMillis(),
            clipUrl = uri.toString(),
            thumbUrl = "",
            description = clipsDesc.text.toString(),
            coverTitle = "Music cover title",
            coverImageLink = "",
            userId = user.uid,
            profilePicUrl = user.photoUrl?.toString(),
            userName = user.displayName,
            likesCount = 0,
            commentsCount = 0,
            addedWhen = System.currentTimeMillis()
        )
    }

    private fun captureVideo() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(requireActivity().packageManager)?.also {
                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15)
                takeVideoIntent.putExtra(
                    MediaStore.EXTRA_VIDEO_QUALITY,
                    1
                ) // ZERO is for low quality
                captureVideoIntentLaunch.launch(takeVideoIntent)
            }
        }
    }

    private fun redirectToHomeScreen() {
        findNavController().navigate(R.id.navigation_home)
        /*if (activity is MainActivity) {
            (activity as MainActivity).getNavigationView().selectedItemId =
                R.id.navigation_home
        }*/
    }
}