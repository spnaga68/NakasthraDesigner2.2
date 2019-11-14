package pasu.nakshatraDesigners.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.VideoListActivity
import pasu.nakshatraDesigners.data.VideoListItem
import pasu.nakshatraDesigners.utils.DownloadListener
import pasu.nakshatraDesigners.utils.NetworkState
import pasu.nakshatraDesigners.utils.VIDEO_URL
import pasu.nakshatraDesigners.utils.widgets.CustomTextview
import pasu.nakshatraDesigners.video.DownloadAndEncryptFileTask
import java.io.File
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class CertificateAdapter internal constructor(val context: Context) :
    PagedListAdapter<VideoListItem, RecyclerView.ViewHolder>(DIFF_CALLBACK),DownloadListener {
    private var networkState: NetworkState? = null
    var positions=0


    val AES_ALGORITHM = "AES"
    val AES_TRANSFORMATION = "AES/CTR/NoPadding"
    private val KEY = "cybervaultsecure"
    private var mSecretKeySpec: SecretKeySpec? = null
    private var mIvParameterSpec: IvParameterSpec? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        return when (viewType) {
            R.layout.certificate_list_item -> {
                view = layoutInflater.inflate(R.layout.certificate_list_item, parent, false)
                PostItemViewHolder(
                    pasu.nakshatraDesigners.databinding.CertificateListItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            R.layout.network_state_item -> {
                view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
                NetworkStateItemViewHolder(view)
            }
            R.layout.headerlayout -> {
                view = layoutInflater.inflate(R.layout.headerlayout, parent, false)
                HeaderViewHolder(
                    pasu.nakshatraDesigners.databinding.HeaderlayoutBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            else -> throw IllegalArgumentException("unknown type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.certificate_list_item -> {
//                (holder as PostItemViewHolder).bindTo(getItem(position))
                val item = getItem(position)
                item?.run {
                    (holder as PostItemViewHolder).apply {
                        bind(this@run)
                        itemView.tag = this@run
                        val imgDownload = rootLayout.findViewById<AppCompatImageView>(R.id.imgDownload)

                        val url = getItem(position)!!.getVideoUrl(context)
                        val isPresentFile = getFileNameFromFolder(url)
                        if (isPresentFile) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                imgDownload.setImageDrawable(
                                    context.resources.getDrawable(
                                        R.drawable.ic_check,
                                        context.theme
                                    )
                                )
                            } else {
                                imgDownload.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        context,
                                        R.drawable.ic_check
                                    )
                                )
                            }
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                imgDownload.setImageDrawable(
                                    context.resources.getDrawable(
                                        R.drawable.ic_file_download,
                                        context.theme
                                    )
                                )
                            } else {
                                imgDownload.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        context,
                                        R.drawable.ic_file_download
                                    )
                                )
                            }
                        }

                        rootLayout.findViewById<View>(R.id.imgDownload).setOnClickListener {
                            val url = getItem(position)!!.getVideoUrl(context)
                            val isPresentFile = getFileNameFromFolder(url)
                            val originalFileName = getFilenameFromUrlWithFormat(url)
                            if (!isPresentFile) {
                                val newFileName = getFolderName() + originalFileName
                                encryptVideo(url, File(newFileName), context)
                            } else {

                            }
                        }

                        rootLayout.findViewById<View>(R.id.imageView).setOnClickListener {
                            val url = getItem(position)!!.getVideoUrl(context)
                            val isPresentFile = getFileNameFromFolder(url)

                            val originalFileName = getFilenameFromUrlWithFormat(url)
                            if (isPresentFile) {
                                redirectToPlayerView(
                                    originalFileName,
                                    url,
                                    isPresentFile
                                )
                            } else {
                                val intent = Intent(context, VideoListActivity::class.java)
                                intent.apply {
                                    putExtra(VIDEO_URL, url)
                                    putExtra("FILEPATH", "")
                                    putExtra("ISFILEPRESENT", false)
                                    context.startActivity(intent)
                                }
                            }
                        }
                    }
                }
            }
            R.layout.network_state_item -> (holder as NetworkStateItemViewHolder).bindView(networkState)

            R.layout.headerlayout -> {
//                (holder as PostItemViewHolder).bindTo(getItem(position))
                val item = getItem(position)
                item?.run {
                    (holder as HeaderViewHolder).apply {
                        bind(this@run)
                        itemView.tag = this@run

                        val mAdViewLay = itemView.findViewById<LinearLayout>(R.id.adViewLay)
                        val mAdView=AdView(context)
//                        println("posssitionnn $positions")
                        when (positions) {
                            0 -> mAdView.setAdUnitId(context.getString(R.string.ad_mob_id4));
                            1 -> mAdView.setAdUnitId(context.getString(R.string.ad_mob_id2));
                            2 -> mAdView.setAdUnitId(context.getString(R.string.ad_mob_id3));
                            else-> mAdView.setAdUnitId(context.getString(R.string.ad_mob_id4));
                        }
                        if(position>3)positions=0 else positions++
                        mAdView.adSize = AdSize.LARGE_BANNER;
                        val adRequest = AdRequest.Builder().build()
                        mAdViewLay.addView(mAdView)
                        mAdView.loadAd(adRequest)
                    }
                }
            }
        }
    }

    override fun downloadCompleted() {
        println("downloadCompleted certificate")
        updateDownloadIcon()
    }


    private fun updateDownloadIcon(){
        notifyDataSetChanged()
    }
    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState !== NetworkState.LOADED && networkState !== NetworkState.MAXPAGE
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            if (getItem(position)?.listType == 0)
                R.layout.certificate_list_item
            else
                R.layout.headerlayout
        }
    }

    internal fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val previousExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val newExtraRow = hasExtraRow()
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (newExtraRow && previousState !== newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }


    class PostItemViewHolder(
        private val binding: pasu.nakshatraDesigners.databinding.CertificateListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        var rootLayout = binding.root

        fun bind(item: VideoListItem) {
            binding.apply {
                listItem = item
                executePendingBindings()
            }


        }

//        rootLayout.set
    }


    class HeaderViewHolder(
        private val binding: pasu.nakshatraDesigners.databinding.HeaderlayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        var rootLayout = binding.root

        fun bind(item: VideoListItem) {
            binding.apply {
                listItem = item
                executePendingBindings()
            }


        }

//        rootLayout.set
    }

    internal class NetworkStateItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val progressBar: ProgressBar
        private val errorMsg: CustomTextview
        private val button: Button

        init {
            progressBar = itemView.findViewById(R.id.progress_bar)
            errorMsg = itemView.findViewById(R.id.error_msg)
            button = itemView.findViewById(R.id.retry_button)
            button.setOnClickListener { }
        }

        fun bindView(networkState: NetworkState?) {
            if (networkState != null && networkState.status == NetworkState.Status.RUNNING) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
            if (networkState != null && networkState.status == NetworkState.Status.FAILED) {
                errorMsg.visibility = View.VISIBLE
                errorMsg.text = networkState.msg
            } else if (networkState != null && networkState.status == NetworkState.Status.MAX) {
                errorMsg.visibility = View.VISIBLE
                errorMsg.text = "No More Page to Load"
            } else {
                errorMsg.visibility = View.GONE
            }
        }


    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VideoListItem>() {
            override fun areItemsTheSame(oldItem: VideoListItem, newItem: VideoListItem): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItem: VideoListItem, newItem: VideoListItem): Boolean {
                return false
            }
        }
    }


    private fun getFileNameFromFolder(url: String): Boolean {
        val sdCardRoot = Environment.getExternalStorageDirectory()
        val yourDir = File(sdCardRoot, "HariBackup/")
        var name = ""
        var temp = false
        if (yourDir.exists() && yourDir.listFiles() != null) {
            for (f in yourDir.listFiles()) {
                if (f.isFile) {
                    name = f.name
                    if (name.contains(".mp4.crypt")) {
                        name = name.substring(0, name.lastIndexOf("."))
                    }
                    if (name.contains(".mp4")) {
                        name = name.substring(0, name.lastIndexOf("."))
                    }
                    if (name == getFilenameFromUrl(url)) {
                        temp = true
                    }
                    println("File Name $name")
                } else {
                    if (name == getFilenameFromUrl(url)) {
                        temp = true
                    }
                }
            }
        }
        return temp
    }

    private fun redirectToPlayerView(
        originalFileName: String,
        url: String,
        isPresentFile: Boolean
    ) {
        val intent = Intent(context, VideoListActivity::class.java)
        intent.apply {
            val filePath = getFolderName() + originalFileName
            putExtra(VIDEO_URL, url)
            putExtra("FILEPATH", filePath)
            putExtra("ISFILEPRESENT", isPresentFile)
            context.startActivity(intent)
        }
    }

    private fun encryptVideo(mUrl: String, mEncryptedFile: File, context: Context) {
        //        if (hasFile()) {
        //            Log.d(getClass().getCanonicalName(), "encrypted file found, no need to recreate");
        //            return;
        //        }
        try {
            val encryptionCipher = Cipher.getInstance(AES_TRANSFORMATION)
            encryptionCipher.init(Cipher.ENCRYPT_MODE, mSecretKeySpec, mIvParameterSpec)
            // TODO:
            // you need to encrypt a video somehow with the same key and iv...  you can do that yourself and update
            // the ciphers, key and iv used in this demo, or to see it from top to bottom,
            // supply a url to a remote unencrypted file - this method will download and encrypt it
            // this first argument needs to be that url, not null or empty...
            DownloadAndEncryptFileTask(mUrl, mEncryptedFile, encryptionCipher, context,this).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getFolderName(): String {
        return Environment.getExternalStorageDirectory().toString() + File.separator + "HariBackup/"
    }

    private fun getFilenameFromUrl(url: String): String {
        var urlFileName = url.substring(url.lastIndexOf('/') + 1)
        if (urlFileName.contains(".mp4")) {
            urlFileName = urlFileName.substring(0, urlFileName.lastIndexOf("."))
        }
        return urlFileName
    }

    private fun getFilenameFromUrlWithFormat(url: String): String {
        return url.substring(url.lastIndexOf('/') + 1)
    }

}