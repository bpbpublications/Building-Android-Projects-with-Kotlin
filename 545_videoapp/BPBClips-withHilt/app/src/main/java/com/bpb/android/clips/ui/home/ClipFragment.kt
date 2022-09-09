package com.bpb.android.clips.ui.home

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bpb.android.clips.BpbClipsApp
import com.bpb.android.clips.R
import com.bpb.android.clips.repository.data.clips.model.ClipsModel
import com.bpb.android.clips.ui.utils.*
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.layout_story_view.*

class ClipFragment : Fragment(R.layout.fragment_clips_view) {
    private var clipsUrl: String? = null
    private var clipsDataModel: ClipsModel? = null

    private var simplePlayer: SimpleExoPlayer? = null
    private var cacheDataSourceFactory: CacheDataSourceFactory? = null
    private val simpleCache = BpbClipsApp.simpleCache
    private var toPlayVideoPosition: Int = -1

    companion object {
        fun newInstance(storiesDataModel: ClipsModel) = ClipFragment()
            .apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.KEY_CLIPS_DATA, storiesDataModel)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clipsDataModel = arguments?.getParcelable(Constants.KEY_CLIPS_DATA)
        setData()
    }

    private fun setData() {
        text_view_account_handle.setTextOrHide(value = clipsDataModel?.userName)
        text_view_video_description.setTextOrHide(value = clipsDataModel?.clipDescription)
        text_view_music_title.setTextOrHide(value = clipsDataModel?.musicCoverTitle)

        image_view_option_comment_title?.text =
            clipsDataModel?.commentsCount?.formatNumberAsReadableFormat()
        image_view_option_like_title?.text =
            clipsDataModel?.likesCount?.formatNumberAsReadableFormat()

        image_view_profile_pic?.loadCenterCropImageFromUrl(clipsDataModel?.userProfilePicUrl)

        text_view_music_title.isSelected = true

        val simplePlayer = getPlayer()
        player_view_story.player = simplePlayer

        clipsUrl = clipsDataModel?.clipUrl
        clipsUrl?.let { prepareMedia(it) }
    }

    override fun onPause() {
        pauseVideo()
        super.onPause()
    }

    override fun onResume() {
        restartVideo()
        super.onResume()
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    private val playerCallback: Player.EventListener? = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            logError("onPlayerStateChanged playbackState: $playbackState")
        }

        override fun onPlayerError(error: com.google.android.exoplayer2.ExoPlaybackException?) {
            super.onPlayerError(error)
        }
    }

    private fun prepareVideoPlayer() {
        simplePlayer = ExoPlayerFactory.newSimpleInstance(context)
        cacheDataSourceFactory = CacheDataSourceFactory(
            simpleCache,
            DefaultHttpDataSourceFactory(
                Util.getUserAgent(
                    context,
                    "exo"
                )
            )
        )
    }

    private fun getPlayer(): SimpleExoPlayer? {
        if (simplePlayer == null) {
            prepareVideoPlayer()
        }
        return simplePlayer
    }

    private fun prepareMedia(linkUrl: String) {
        logError("prepareMedia linkUrl: $linkUrl")

        val uri = Uri.parse(linkUrl)

        val mediaSource =
            ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(uri)

        simplePlayer?.prepare(mediaSource, true, true)
        simplePlayer?.repeatMode = Player.REPEAT_MODE_ONE
        simplePlayer?.playWhenReady = true
        simplePlayer?.addListener(playerCallback)

        toPlayVideoPosition = -1
    }

    private fun setArtwork(drawable: Drawable, playerView: PlayerView) {
        playerView.useArtwork = true
        playerView.defaultArtwork = drawable
    }

    private fun playVideo() {
        simplePlayer?.playWhenReady = true
    }

    private fun restartVideo() {
        if (simplePlayer == null) {
            clipsUrl?.let { prepareMedia(it) }
        } else {
            simplePlayer?.seekToDefaultPosition()
            simplePlayer?.playWhenReady = true
        }
    }

    private fun pauseVideo() {
        simplePlayer?.playWhenReady = false
    }

    private fun releasePlayer() {
        simplePlayer?.stop(true)
        simplePlayer?.release()
    }
}