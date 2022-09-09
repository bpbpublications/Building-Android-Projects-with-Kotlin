package com.bpb.android.clips.ui.home

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bpb.android.clips.BpbClipsApp
import com.bpb.android.clips.R
import com.bpb.android.clips.repository.data.clips.model.Clips
import com.bpb.android.clips.ui.utils.formatNumberAsReadableFormat
import com.bpb.android.clips.ui.utils.loadUsingGlide
import com.bpb.android.clips.ui.utils.setTextOrHide
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.clips_view.*

class ClipFragment : Fragment(R.layout.fragment_clips_view) {
    private lateinit var simplePlayer: SimpleExoPlayer
    private lateinit var cacheDataSourceFactory: CacheDataSourceFactory
    private val simpleCache = BpbClipsApp.simpleCache
    var listener: OnClipsListener? = null
    private var clipsData: Clips? = null
    private var positionInAdapter = 0

    companion object {
        const val EXTRA_CLIPS = "extra_clips"
        const val EXTRA_POS = "extra_position_in_adapter"
        fun newInstance(position: Int, clips: Clips) = ClipFragment()
            .apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_CLIPS, clips)
                    putInt(EXTRA_POS, position)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            clipsData = args.getParcelable(EXTRA_CLIPS)
            positionInAdapter = args.getInt(EXTRA_POS, 0)
        }
        clipsData?.let {
            setClipData(it)
            setListeners(it)
            showLikes(it)
            showComments(it)
            playClips(it)
        }
    }

    private fun setClipData(clips: Clips) {
        tvAccountHandle.setTextOrHide(value = clips.userName)
        tvVideoDescription.setTextOrHide(value = clips.description)
        tvMusicTitle.setTextOrHide(value = clips.coverTitle)

        ivProfilePic?.loadUsingGlide(clips.profilePicUrl)
        tvMusicTitle.isSelected = true
    }

    private fun setListeners(clips: Clips) {
        ivLikeClips.setOnClickListener {
            // 1. Update clips object
            updateLikes(clips)
            // 2. Update database
            listener?.onLikeClicked(positionInAdapter, clips.isLiked, clips)
            // 3. Update UI
            showLikes(clips)
        }

        ivComment.setOnClickListener {
            updateComments(clips)
            listener?.onCommentsClicked(positionInAdapter, clips)
        }

        ivShare.setOnClickListener {
            shareClips(clips)
        }
    }

    private fun updateLikes(clips: Clips) {
        clips.isLiked = !clips.isLiked
        if (clips.isLiked) clips.likesCount++ else clips.likesCount--
    }

    private fun showLikes(clips: Clips) {
        ivLikeClips.setImageResource(
            if (clips.isLiked) {
                R.drawable.ic_heart_select_icon
            } else {
                R.drawable.ic_heart_icon
            }
        )

        tvLikeClipsTitle?.text =
            clips.likesCount.formatNumberAsReadableFormat()
    }

    private fun updateComments(clips: Clips) {
        clips.commentsCount++
        showComments(clips)
    }

    private fun showComments(clips: Clips) {
        tvCommentTitle?.text =
            clips.commentsCount.formatNumberAsReadableFormat()
    }

    private fun playClips(clips: Clips) {
        val simplePlayer = getPlayer()
        playerView.player = simplePlayer

        clips.clipUrl?.let { prepareMediaAndPlay(it) }
    }

    private fun shareClips(clips: Clips) {
        val text = "I am sharing you this amazing clip ${clips.clipUrl} " +
                "to watch. You can find many amazing clips at " +
                "https://play.google.com/store/apps/details?id=com.bpb.android.clips"

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, "Have you seen this Clip?")
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share this clip via")
        startActivity(shareIntent)
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

    /*private val playerCallback: Player.EventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            logError("onPlayerStateChanged playbackState: $playbackState")
        }

        override fun onPlayerError(error: com.google.android.exoplayer2.ExoPlaybackException?) {
            super.onPlayerError(error)
        }
    }*/

    /**
     * Get the player
     */
    private fun getPlayer(): SimpleExoPlayer {
        // Initialise it, if not done already
        if (!::simplePlayer.isInitialized) {
            initPlayer()
        }
        return simplePlayer
    }

    private fun initPlayer() {
        simplePlayer = ExoPlayerFactory.newSimpleInstance(context)
        cacheDataSourceFactory = CacheDataSourceFactory(
            simpleCache, // Cache to provide better experience to user.
            DefaultHttpDataSourceFactory(
                Util.getUserAgent(
                    context,
                    "BPB Clips"
                )
            )
        )
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val datasourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(context, Util.getUserAgent(context, "BPB Clips"))
        return ExtractorMediaSource.Factory(datasourceFactory).createMediaSource(uri)
    }

    private fun prepareMediaAndPlay(videoUrl: String) {
        val uri = Uri.parse(videoUrl)

        /*val mediaSource = ProgressiveMediaSource.Factory(
            cacheDataSourceFactory
        ).createMediaSource(uri)*/

        val mediaSource: MediaSource = buildMediaSource(uri)
        simplePlayer.prepare(mediaSource, true, true)
        simplePlayer.repeatMode = Player.REPEAT_MODE_ONE
        simplePlayer.playWhenReady = true
        // simplePlayer.addListener(playerCallback)
    }

    private fun setArtwork(drawable: Drawable, playerView: PlayerView) {
        playerView.useArtwork = true
        playerView.defaultArtwork = drawable
    }

    private fun playVideo() {
        simplePlayer.playWhenReady = true
    }

    private fun restartVideo() {
        if (::simplePlayer.isInitialized) {
            simplePlayer.seekToDefaultPosition()
            simplePlayer.playWhenReady = true
        }
    }

    private fun pauseVideo() {
        simplePlayer.playWhenReady = false
    }

    private fun releasePlayer() {
        simplePlayer.stop(true)
        simplePlayer.release()
    }
}