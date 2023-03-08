package pasu.nakshatraDesigners

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import pasu.nakshatraDesigners.utils.VIDEO_URL
import pasu.nakshatraDesigners.video.EncryptedFileDataSourceFactory
import java.io.File
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class VideoListActivity : AppCompatActivity() {

    private var player: SimpleExoPlayer? = null
    private lateinit var playerView: PlayerView

    private var playbackPosition: Long = 0
    private var currentWindow: Int = 0
    private var playWhenReady = true

    private var mProgressBar: ProgressBar? = null

    var mUrl: String = ""
    var filePath: String = ""
    var isFilePresent = false
    lateinit var mAdView: AdView


    val AES_ALGORITHM = "AES"
    val AES_TRANSFORMATION = "AES/CTR/NoPadding"
    private val KEY = "cybervaultsecure"


    private lateinit var mCipher: Cipher
    private var mSecretKeySpec: SecretKeySpec? = null
    private var mIvParameterSpec: IvParameterSpec? = null

    private lateinit var mEncryptedFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)



        mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView.visibility = View.GONE

        playerView = findViewById(R.id.video_view)
        mProgressBar = findViewById(R.id.progressBar)
        playerView.setFastForwardIncrementMs(30000)
        playerView.setRewindIncrementMs(30000)
        findViewById<View>(R.id.back).setOnClickListener { finish() }
        if (intent != null) {
            if (intent.getStringExtra(VIDEO_URL) != null) {
                mUrl = intent!!.getStringExtra(VIDEO_URL)!!
                println("SIZE ImageURL $mUrl")
            }
            if (intent.getStringExtra("FILEPATH") != null) {
                filePath = intent!!.getStringExtra("FILEPATH")!!
            }
            init()
        }

    }

    fun init() {

        val secureRandom = SecureRandom()
        var key = ByteArray(16)
        var iv = ByteArray(16)
        secureRandom.nextBytes(key)
        secureRandom.nextBytes(iv)
        key = KEY.toByteArray()
        iv = KEY.toByteArray()
        mSecretKeySpec = SecretKeySpec(key, AES_ALGORITHM)
        mIvParameterSpec = IvParameterSpec(iv)

        if (intent.getBooleanExtra("ISFILEPRESENT", false) != null) {
            isFilePresent = intent.getBooleanExtra("ISFILEPRESENT", false)
            mEncryptedFile = File(filePath)

                var mediaSource: MediaSource = if (isFilePresent) {
                    buildMediaSource(Uri.parse(filePath))
                } else {
                    buildMediaSource(Uri.parse(mUrl))
                }
                if (player == null) {
                    initializePlayer(mediaSource)
                }
                if (isFilePresent) {
                    playVideo()
                } else {
                    var mediaSource: MediaSource = buildMediaSource(Uri.parse(mUrl))
                    if ( player == null) {
                        initializePlayer(mediaSource)
                    }
                }
        } else {
            var mediaSource: MediaSource = buildMediaSource(Uri.parse(mUrl))
            if ( player == null) {
                initializePlayer(mediaSource)
            }
        }
    }

    private fun playVideo() {

        var key = ByteArray(16)
        var iv = ByteArray(16)
        key = KEY.toByteArray()
        iv = KEY.toByteArray()
        mSecretKeySpec = SecretKeySpec(key, AES_ALGORITHM)
        mIvParameterSpec = IvParameterSpec(iv)

        try {
            mCipher = Cipher.getInstance(AES_TRANSFORMATION)
            mCipher.init(Cipher.DECRYPT_MODE, mSecretKeySpec, mIvParameterSpec)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val bandwidthMeter = DefaultBandwidthMeter()
        val trackSelector = DefaultTrackSelector()
        val loadControl = DefaultLoadControl()
//        val player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl)
        playerView.player = player
        val dataSourceFactory = EncryptedFileDataSourceFactory(
            mCipher,
            mSecretKeySpec,
            mIvParameterSpec,
            bandwidthMeter
        )
        val extractorsFactory = DefaultExtractorsFactory()
        try {
            val uri = Uri.fromFile(mEncryptedFile)
            val videoSource =
                ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null)
            player!!.prepare(videoSource)
            player!!.playWhenReady = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStart() {
        super.onStart()
//        if (Util.SDK_INT > 23) {
//            initializePlayer()
//        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
//        if ((Util.SDK_INT <= 23 || player == null)) {
//            initializePlayer()
//        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }


    fun initializePlayer(mediaSource: MediaSource) {

        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(this),
                DefaultTrackSelector(), DefaultLoadControl()
            )
            playerView.player = player
            player!!.playWhenReady = playWhenReady
            player!!.seekTo(currentWindow, playbackPosition)
        }
        player!!.prepare(mediaSource, true, false)
        
        player!!.addListener(object : Player.EventListener{
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
                 //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSeekProcessed() {
                 //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
                 //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPlayerError(error: ExoPlaybackException?) {
                 //To change body of created functions use File | Settings | File Templates.
            }

            override fun onLoadingChanged(isLoading: Boolean) {
                 //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPositionDiscontinuity(reason: Int) {
                 //To change body of created functions use File | Settings | File Templates.
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                 //To change body of created functions use File | Settings | File Templates.
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                 //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
                 //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                 //To change body of created functions use File | Settings | File Templates.

                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        println("iddleeeebbbb")
                        playerView.alpha = 0.5f
                        Log.e("", "onPlayerStateChanged: Buffering ")
                        if (mProgressBar != null) {
                            mProgressBar!!.visibility = View.VISIBLE
                        }
                    }
                    Player.STATE_ENDED -> player!!.seekTo(0)
                    Player.STATE_IDLE -> {
                        println("iddleeee")
                    }
                    Player.STATE_READY -> {
                        println("iddleeeerrrrr $playWhenReady")
                        if (!playWhenReady) {
                            mAdView.visibility = View.VISIBLE
                        } else {
                            mAdView.visibility = View.GONE
                        }
                        Log.e("", "onPlayerStateChanged: Ready ")
                        if (mProgressBar != null) {
                            mProgressBar!!.visibility = View.GONE
                        }
                        playerView.visibility = View.VISIBLE
                        playerView.alpha = 1f
                    }
                    else -> {
                        println("iddleeeellll")
                    }
                }

            }

        })

/*
        if(player != null) {
            player!!.prepare(mediaSource, true, false)


            player!!.addListener(object : Player.EventListener {
                override fun onTimelineChanged(timeline: Timeline, manifest: Any, reason: Int) {

                }

                override fun onTracksChanged(
                    trackGroups: TrackGroupArray,
                    trackSelections: TrackSelectionArray
                ) {

                }

                override fun onLoadingChanged(isLoading: Boolean) {

                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    when (playbackState) {

                        Player.STATE_BUFFERING -> {
                            println("iddleeeebbbb")
                            playerView.alpha = 0.5f
                            Log.e("", "onPlayerStateChanged: Buffering ")
                            if (mProgressBar != null) {
                                mProgressBar!!.visibility = View.VISIBLE
                            }
                        }
                        Player.STATE_ENDED -> player!!.seekTo(0)
                        Player.STATE_IDLE -> {
                            println("iddleeee")

                        }
                        Player.STATE_READY -> {
                            println("iddleeeerrrrr $playWhenReady")
                            if (!playWhenReady) {
                                mAdView.visibility = View.VISIBLE
                            } else {
                                mAdView.visibility = View.GONE
                            }
                            Log.e("", "onPlayerStateChanged: Ready ")
                            if (mProgressBar != null) {
                                mProgressBar!!.visibility = View.GONE
                            }
                            playerView.visibility = View.VISIBLE
                            playerView.alpha = 1f
                        }
                        else -> {
                            println("iddleeeellll")
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
        }*/

    }

    fun releasePlayer() {
        if (player != null) {
//            playbackPosition = player!!.currentPosition
//            currentWindow = player!!.currentWindowIndex
            playWhenReady = false
            player!!.release()
            player!!.seekTo(0)
            player = null
        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        // these are reused for both media sources we create below
        val extractorsFactory = DefaultExtractorsFactory()
        val dataSourceFactory = DefaultHttpDataSourceFactory("user-agent")
//    val dataSourceFactory : DataSource.Factory =  FileDataSourceFactory()

        val videoSource = ExtractorMediaSource.Factory(
            /*DefaultHttpDataSourceFactory("exoplayer-codelab")*/
            dataSourceFactory
        ).createMediaSource(uri)
/*
        val audioSource = ExtractorMediaSource.Factory(
            dataSourceFactory
            *//*DefaultHttpDataSourceFactory("exoplayer-codelab")*//*
        ).createMediaSource(uri)*/

        return videoSource
    }

    fun hideSystemUi() {
        playerView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
}
