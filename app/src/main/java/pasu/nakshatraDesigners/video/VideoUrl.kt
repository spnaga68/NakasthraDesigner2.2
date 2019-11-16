//package developer.rxandroidex
//
//import android.app.ProgressDialog
//import android.content.SharedPreferences
//import android.net.Uri
//import android.os.AsyncTask
//import android.os.Bundle
//import android.os.Environment
//import android.support.v7.app.AppCompatActivity
//import android.util.Base64
//import android.util.Log
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.google.android.exoplayer2.*
//import pasu.nakshatraDesigners.R
//
//import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
//import com.google.android.exoplayer2.source.ExtractorMediaSource
//import com.google.android.exoplayer2.source.MediaSource
//import com.google.android.exoplayer2.source.TrackGroupArray
//import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
//import com.google.android.exoplayer2.trackselection.TrackSelection
//import com.google.android.exoplayer2.trackselection.TrackSelectionArray
//import com.google.android.exoplayer2.ui.SimpleExoPlayerView
//import com.google.android.exoplayer2.upstream.BandwidthMeter
//import com.google.android.exoplayer2.upstream.DataSource
//import com.google.android.exoplayer2.upstream.DataSpec
//import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
//import pasu.nakshatraDesigners.utils.Session
//import pasu.nakshatraDesigners.video.MByteArrayDataSource
//
//import java.io.BufferedInputStream
//import java.io.File
//import java.io.FileInputStream
//import java.io.FileOutputStream
//import java.io.IOException
//import java.io.InputStream
//import java.io.OutputStream
//import java.net.URL
//import java.net.URLConnection
//import java.security.InvalidKeyException
//import java.security.Key
//import java.security.MessageDigest
//import java.security.NoSuchAlgorithmException
//import java.security.SignatureException
//import java.text.SimpleDateFormat
//import java.util.Arrays
//import java.util.Date
//import java.util.Formatter
//
//import javax.crypto.Cipher
//import javax.crypto.CipherInputStream
//import javax.crypto.CipherOutputStream
//import javax.crypto.Mac
//import javax.crypto.NoSuchPaddingException
//import javax.crypto.spec.SecretKeySpec
//
//class VideoUrl : AppCompatActivity /*implements EasyPermissions.PermissionCallbacks*/() {
//    private var url: String? = null
//    private val editTextUrl: EditText? = null
//    private val file_url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
//    internal var data = ByteArray(1024)
//
//
//    private var fileName: String? = null
//    private var folder: String? = null
//    private val isDownloaded: Boolean = false
//
//
//    private var video_view: SimpleExoPlayerView? = null
//
//    private var exoPlayer: SimpleExoPlayer? = null
//
//    private lateinit var simpleExoplayer: SimpleExoPlayer
//    private var playbackPosition = 0L
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        video_view = findViewById(R.id.video_view)
//        val downloadButton = findViewById<Button>(R.id.btnDownload)
//        downloadButton.setOnClickListener {
//            url = file_url
//            DownloadFile().execute(url)
//        }
//
//    }
//
//    private val eventListener = object : ExoPlayer.EventListener {
//        override fun onPositionDiscontinuity(reason: Int) {
//            Log.i("", "onPositionDiscontinuity")
//        }
//
//        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
//        }
//
//        override fun onSeekProcessed() {
//        }
//
//        override fun onRepeatModeChanged(repeatMode: Int) {
//        }
//
//        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
//        }
//
//        override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
//
//        }
//
//        override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
//            Log.i("", "onTracksChanged")
//        }
//
//        override fun onLoadingChanged(isLoading: Boolean) {
//            Log.i("", "onLoadingChanged")
//        }
//
//        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//            Log.i(
//                "", "onPlayerStateChanged: playWhenReady = " + playWhenReady.toString()
//                        + " playbackState = " + playbackState
//            )
//            when (playbackState) {
//                ExoPlayer.STATE_ENDED -> {
//                    Log.i("", "Playback ended!")
//                    //Stop playback and return to start position
////                        setPlayPause(false)
//                    exoPlayer!!.seekTo(0)
//                }
//                ExoPlayer.STATE_READY -> {
////                        Log.i("", "ExoPlayer ready! pos: " + exoPlayer!!.currentPosition
////                                + " max: " + stringForTime(exoPlayer!!.duration.toInt()))
////                        setProgress()
//
//
//                    video_view!!.player = exoPlayer
//
//                }
//                ExoPlayer.STATE_BUFFERING -> Log.i("", "Playback buffering!")
//                ExoPlayer.STATE_IDLE -> Log.i("", "ExoPlayer idle!")
//            }
//        }
//
//        override fun onPlayerError(error: ExoPlaybackException) {
//            Log.i("", "onPlaybackError: " + error.message)
//        }
//
//    }
//
//    /*    btnEncry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                try {
////                    String str = folder+fileName;
////                    System.out.println("folder_fileName"+str);
////                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
////                    SharedPreferences.Editor editor = pref.edit();
////                    editor.putString("FOLDER",folder);
////                    editor.putString("FILENAME",fileName);
////                    encryptfile("/storage/emulated/0/androiddeft/Nandhini_BigBuckBunny.mp4","NdoT_2017TaX1#23");
////                } catch (IOException e) {
////                    e.printStackTrace();
////                } catch (NoSuchAlgorithmException e) {
////                    e.printStackTrace();
////                } catch (NoSuchPaddingException e) {
////                    e.printStackTrace();
////                } catch (InvalidKeyException e) {
////                    e.printStackTrace();
////                }
//
////                try {
////                    System.out.println("TESTTTTTTTTTTTTTT" + hashMac("NANDHINI","NdoT_2017TaX1#23"));
////                } catch (SignatureException e) {
////                    e.printStackTrace();
////                }
//            }
//        });
//
//        btnDecry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                String folder = Environment.getExternalStorageDirectory() + File.separator + "Nandhu/";
////
////                //Create androiddeft folder if it does not exist
////                File directory = new File(folder);
////
////                if (!directory.exists()) {
////                    directory.mkdirs();
////                }
////                System.out.println("folder" + folder);
////                try {
////                    decrypt("/storage/emulated/0/androiddeft/Nandhini_BigBuckBunny.mp4.crypt", "Hari_2019", folder);
////                } catch (IOException e) {
////                    e.printStackTrace();
////                } catch (NoSuchAlgorithmException e) {
////                    e.printStackTrace();
////                } catch (NoSuchPaddingException e) {
////                    e.printStackTrace();
////                } catch (InvalidKeyException e) {
////                    e.printStackTrace();
////                }
//
//                File dir = getFilesDir();
//                System.out.println("DIRRRRRRRRRRR"+dir);
//
//            }
//        });*/
//
//
//    @Throws(NoSuchAlgorithmException::class)
//    fun SHA256(text: String): String {
//
//        val md = MessageDigest.getInstance("SHA-256")
//
//        md.update(text.toByteArray())
//        val digest = md.digest()
//
//        return Base64.encodeToString(digest, Base64.DEFAULT)
//    }
//
//    @Throws(SignatureException::class)
//    fun hashMac(text: String, secretKey: String): String {
//
//        try {
//            val sk = SecretKeySpec(secretKey.toByteArray(), HASH_ALGORITHM)
//            val mac = Mac.getInstance(sk.algorithm)
//            mac.init(sk)
//            val hmac = mac.doFinal(text.toByteArray())
//            return toHexString(hmac)
//        } catch (e1: NoSuchAlgorithmException) {
//            // throw an exception or pick a different encryption method
//            throw SignatureException(
//                "error building signature, no such algorithm in device $HASH_ALGORITHM"
//            )
//        } catch (e: InvalidKeyException) {
//            throw SignatureException(
//                "error building signature, invalid key $HASH_ALGORITHM"
//            )
//        }
//
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        //        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, MainActivity.this);
//    }
//    //
//    //    @Override
//    //    public void onPermissionsGranted(int requestCode, List<String> perms) {
//    //        //Download the file once permission is granted
//    //        url = editTextUrl.getText().toString();
//    //        new DownloadFile().execute(url);
//    //    }
//
//    //    @Override
//    //    public void onPermissionsDenied(int requestCode, List<String> perms) {
//    //        Log.d(TAG, "Permission has been denied");
//    //    }
//
//
//    @Throws(
//        IOException::class,
//        NoSuchAlgorithmException::class,
//        NoSuchPaddingException::class,
//        InvalidKeyException::class
//    )
//    fun encryptfile(path: String, password: String) {
//        val fis = FileInputStream(path)
//        val fos = FileOutputStream("$path.crypt")
//        var key = (data.toString() + password).toByteArray(charset("UTF-8"))
//        val sha = MessageDigest.getInstance("SHA-1")
//        key = sha.digest(key)
//        key = Arrays.copyOf(key, 16)
//        val sks = SecretKeySpec(key, "AES")
//        val cipher = Cipher.getInstance("AES")
//        cipher.init(Cipher.ENCRYPT_MODE, sks)
//        val cos = CipherOutputStream(fos, cipher)
//        var b: Int
//        //        byte[] d = new byte[8];
//        val d = ByteArray(1024 * 1024)
//        b = fis.read(d)
//        while ((fis.read(d)) != -1) {
//            cos.write(d, 0, fis.read(d))
//        }
//
//        cos.flush()
//        cos.close()
//        fis.close()
//        val uri = Uri.parse(Session.getSession("OUTPUT_PATH", this@VideoUrl))
//        val fdelete = File(uri.path)
//        if (fdelete.exists()) {
//            if (fdelete.delete()) {
//                println("file Deleted :" + uri.path!!)
//            } else {
//                println("file not Deleted :" + uri.path!!)
//            }
//        }
//
//        //External directory path to save file
//        val folder = Environment.getExternalStorageDirectory().toString() + File.separator + "Hari/"
//
//        //Create androiddeft folder if it does not exist
//        val directory = File(folder)
//
//        if (!directory.exists()) {
//            directory.mkdirs()
//        }
//        println("folder$folder")
//
//        try {
//            decrypt(SessionSave.getSession("OUTPUT_PATH", this@VideoUrl) + ".crypt", "Hari_2019", folder)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } catch (e: NoSuchAlgorithmException) {
//            e.printStackTrace()
//        } catch (e: NoSuchPaddingException) {
//            e.printStackTrace()
//        } catch (e: InvalidKeyException) {
//            e.printStackTrace()
//        }
//
//    }
//
//    @Throws(
//        IOException::class,
//        NoSuchAlgorithmException::class,
//        NoSuchPaddingException::class,
//        InvalidKeyException::class
//    )
//    fun decrypt(path: String, password: String, outPath: String) {
//        val fis = FileInputStream(path)
//        //        FileOutputStream fos = new FileOutputStream(outPath);
//        val fos = FileOutputStream(outPath + Session.getSession("FILE_NAME", this@VideoUrl))
//        var key = (data.toString() + password).toByteArray(charset("UTF-8"))
//        val sha = MessageDigest.getInstance("SHA-1")
//        key = sha.digest(key)
//        key = Arrays.copyOf(key, 16)
//        val sks = SecretKeySpec(key, "AES")
//        val cipher = Cipher.getInstance("AES")
//        cipher.init(Cipher.DECRYPT_MODE, sks)
//        val cis = CipherInputStream(fis, cipher)
//        var b: Int
//        //        byte[] d = new byte[8];
//        val d = ByteArray(1024 * 1024)
//        b = cis.read(d)
//        println("decrypt" + b)
//        while ((cis.read(d)) != -1) {
//            println("decrypt______________" + d)
//            fos.write(d, 0, b)
//        }
////        test(d)
//        fos.flush()
//        fos.close()
//        cis.close()
//    }
//
//    fun test(data: ByteArray) {
//        var byteArrayDataSource: MByteArrayDataSource = MByteArrayDataSource(data);
//
//        val factory = DataSource.Factory { byteArrayDataSource }
//
////          exoPlayer = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector(null), DefaultLoadControl());
//        exoPlayer!!.addListener(eventListener);
//
//        Log.i("", "ByteArrayDataSource constructed.");
//
//
//        Log.i("", "DataSource.Factory constructed.");
//
//
////        exoPlayer!!.prepare(audioSource);
//        /*  var trackSelector = DefaultTrackSelector()
////        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, DefaultLoadControl())
//        val byteArrayDataSource = MByteArrayDataSource(data)
//        val factory = DataSource.Factory { byteArrayDataSource }
//        exoPlayer = ExoPlayerFactory.newSimpleInstance(
//                 DefaultRenderersFactory(this),
//                 DefaultTrackSelector(DefaultBandwidthMeter()),  DefaultLoadControl());
//
//        Log.i("", "DataSource.Factory constructed.")
//
//        val audioSource = ExtractorMediaSource(byteArrayDataSource.uri,
//                factory, DefaultExtractorsFactory(), null, null)
//        Log.i("", "Audio source constructed.")
//        exoPlayer!!.prepare(audioSource)*/
//    }
//
//
//}
//    /**
//     * Async Task to download file from URL
//     */
//    class DownloadFile : AsyncTask<String, String, String>() {
//
//        private var progressDialog: ProgressDialog? = null
//
//        /**
//         * Before starting background thread
//         * Show Progress Bar Dialog
//         */
//        override fun onPreExecute() {
//            super.onPreExecute()
//            progressDialog = ProgressDialog(this)
//            progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
//            progressDialog!!.setCancelable(false)
//            progressDialog!!.show()
//        }
//
//        /**
//         * Downloading file in background thread
//         */
//        override fun doInBackground(vararg f_url: String): String {
//            var count: Int
//            try {
//                val url = URL(f_url[0])
//                val connection = url.openConnection()
//                connection.connect()
//                // getting file length
//                val lengthOfFile = connection.contentLength
//
//
//                // input stream to read file - with 8k buffer
//                val input = BufferedInputStream(url.openStream(), 8192)
//
//                val timestamp = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(Date())
//
//                //Extract file name from URL
//                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length)
//
//                //Append timestamp to file name
//                fileName = "Nandhini_$fileName"
//
//                //External directory path to save file
//                folder = Environment.getExternalStorageDirectory().toString() + File.separator + "HariBackup/"
//
//
//                //Create androiddeft folder if it does not exist
//                val directory = File(folder)
//
//                if (!directory.exists()) {
//                    directory.mkdirs()
//                }
//
//                Session.save("FILE_NAME", fileName, this@VideoUrl)
//                Session.save("FOLDER_NAME", folder, this@VideoUrl)
//                Session.save("OUTPUT_PATH", folder!! + fileName!!, this@VideoUrl)
//
//                // Output stream to write file
//                val output = FileOutputStream(Session.getSession("OUTPUT_PATH", this@VideoUrl))
//                println("output" + folder + "____" + fileName + "____")
//                data = ByteArray(1024)
//                //                count = input.read(data);
//                var total: Long = 0
//                count = input.read(data)
//                while ((input.read(data)) != -1) {
//                    total += input.read(data).toLong()
//                    // publishing the progress....
//                    // After this onProgressUpdate will be called
//                    publishProgress("" + (total * 100 / lengthOfFile).toInt())
//                    Log.d("", "Progress: " + (total * 100 / lengthOfFile).toInt())
//                    val str = Base64.encodeToString(data, 1024)
//                    println("data" + Base64.decode(str, 1024))
//                    // writing data to file
//                    output.write(data, 0, input.read(data))
//                }
//
//
//                // flushing output
//                output.flush()
//
//                // closing streams
//                output.close()
//                input.close()
//                return "Downloaded at: $folder$fileName"
//
//            } catch (e: Exception) {
//                Log.e("Error: ", e.message)
//            }
//
//            return "Something went wrong"
//        }
//
//        /**
//         * Updating progress bar
//         */
//        override fun onProgressUpdate(vararg progress: String) {
//            // setting progress percentage
//            progressDialog!!.progress = Integer.parseInt(progress[0])
//        }
//
//
//        override fun onPostExecute(message: String) {
//            // dismiss the dialog after the file was downloaded
//            progressDialog!!.dismiss()
//
//            // Display File path after downloading
//            Toast.makeText(applicationContext,
//                    message, Toast.LENGTH_LONG).show()
//
//            try {
//                val str = Session.getSession("OUTPUT_PATH", this@VideoUrl)
//                encryptfile(str, "Hari_2019")
//            } catch (e: IOException) {
//                e.printStackTrace()
//            } catch (e: NoSuchAlgorithmException) {
//                e.printStackTrace()
//            } catch (e: NoSuchPaddingException) {
//                e.printStackTrace()
//            } catch (e: InvalidKeyException) {
//                e.printStackTrace()
//            }
//
//        }
//    }
///*
//
//    companion object {
//        private val WRITE_REQUEST_CODE = 300
////        private val TAG = MainActivity::class.java.simpleName
//
//        private val HASH_ALGORITHM = "HmacSHA256"
//
//        fun toHexString(bytes: ByteArray): String {
//            val sb = StringBuilder(bytes.size * 2)
//
//            val formatter = Formatter(sb)
//            for (b in bytes) {
//                formatter.format("%02x", b)
//            }
//
//            return sb.toString()
//        }
//    }
//*/
