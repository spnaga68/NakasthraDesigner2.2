package pasu.nakshatraDesigners

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.utils.VIDEO_URL


class VideoListActivity : AppCompatActivity() {

    private var player: SimpleExoPlayer? = null
    private lateinit var playerView: PlayerView

    private var playbackPosition: Long = 0
    private var currentWindow: Int = 0
    private var playWhenReady = true

    private var mProgressBar: ProgressBar? = null

    var mUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        );
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        playerView = findViewById(R.id.video_view)
        mProgressBar = findViewById(R.id.progressBar)
        playerView.setFastForwardIncrementMs(30000)
        playerView.setRewindIncrementMs(30000)
        findViewById<View>(R.id.back).setOnClickListener { finish() }
        if (intent != null) {
            if (intent.getStringExtra(VIDEO_URL) != null) {
                mUrl = intent.getStringExtra(VIDEO_URL)
                println("SIZE ImageURL ${mUrl}")
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    fun initializePlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(this),
                DefaultTrackSelector(), DefaultLoadControl()
            );
            playerView.setPlayer(player);
            player!!.setPlayWhenReady(playWhenReady);
            player!!.seekTo(currentWindow, playbackPosition);
        }
        var mediaSource = buildMediaSource(Uri.parse(mUrl));
        player!!.prepare(mediaSource, true, false);



        player!!.addListener(object : Player.EventListener {
            override fun onTimelineChanged(timeline: Timeline, manifest: Any, reason: Int) {

            }

            override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {

            }

            override fun onLoadingChanged(isLoading: Boolean) {

            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {

                    Player.STATE_BUFFERING -> {
                        playerView.setAlpha(0.5f)
                        Log.e("", "onPlayerStateChanged: Buffering ")
                        if (mProgressBar != null) {
                            mProgressBar!!.setVisibility(View.VISIBLE)
                        }
                    }
                    Player.STATE_ENDED -> player!!.seekTo(0)
                    Player.STATE_IDLE -> {
                    }
                    Player.STATE_READY -> {
                        Log.e("", "onPlayerStateChanged: Ready ")
                        if (mProgressBar != null) {
                            mProgressBar!!.setVisibility(View.GONE)
                        }
                        playerView.setVisibility(View.VISIBLE)
                        playerView.setAlpha(1f)
                    }
                    else -> {
                    }
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {

            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

            }

            override fun onPlayerError(error: ExoPlaybackException) {

            }

            override fun onPositionDiscontinuity(reason: Int) {

            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {

            }

            override fun onSeekProcessed() {

            }
        })

    }

    fun releasePlayer() {
        if (player != null) {
            playbackPosition = player!!.getCurrentPosition();
            currentWindow = player!!.getCurrentWindowIndex();
            playWhenReady = player!!.getPlayWhenReady();
            player!!.release();
            player = null;
        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        // these are reused for both media sources we create below
        val extractorsFactory = DefaultExtractorsFactory()
        val dataSourceFactory = DefaultHttpDataSourceFactory("user-agent")

        val videoSource = ExtractorMediaSource.Factory(
            DefaultHttpDataSourceFactory("exoplayer-codelab")
        ).createMediaSource(uri)

        val audioUri = Uri.parse(mUrl)
        val audioSource = ExtractorMediaSource.Factory(
            DefaultHttpDataSourceFactory("exoplayer-codelab")
        ).createMediaSource(audioUri)

        return ConcatenatingMediaSource(audioSource, videoSource)
    }

//    fun buildMediaSource(uri:Uri):MediaSource {
//        return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory("exoplayer-codelab"))
//            .createMediaSource(uri);
//    }

    fun hideSystemUi() {
        playerView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
}
