package pasu.nakshatraDesigners.adapter


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import nakshatraDesigners.utils.CommonFunctions
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.VideoListActivity
import pasu.nakshatraDesigners.adapter.VideoBaseAdapter.PostItemViewHolder
import pasu.nakshatraDesigners.data.VideoListItem
import pasu.nakshatraDesigners.databinding.CertificateListItemBinding
import pasu.nakshatraDesigners.utils.DialogOnClickInterface
import pasu.nakshatraDesigners.utils.DownloadListener
import pasu.nakshatraDesigners.utils.VIDEO_URL
import pasu.nakshatraDesigners.video.DownloadAndEncryptFileTask
import java.io.File
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class VideoBaseAdapter(val context: Context, val myDataSet: ArrayList<VideoListItem>) :
    RecyclerView.Adapter<PostItemViewHolder>(), DownloadListener,DialogOnClickInterface{

    val AES_ALGORITHM = "AES"
    val AES_TRANSFORMATION = "AES/CTR/NoPadding"
    private val KEY = "cybervaultsecure"
    private var mSecretKeySpec: SecretKeySpec? = null
    private var mIvParameterSpec: IvParameterSpec? = null

    private lateinit var file:File
    init {
        val secureRandom = SecureRandom()
        var key = ByteArray(16)
        var iv = ByteArray(16)
        secureRandom.nextBytes(key)
        secureRandom.nextBytes(iv)
        key = KEY.toByteArray()
        iv = KEY.toByteArray()
        mSecretKeySpec = SecretKeySpec(key, AES_ALGORITHM)
        mIvParameterSpec = IvParameterSpec(iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemViewHolder {
        return PostItemViewHolder(
            CertificateListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    //
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: PostItemViewHolder, position: Int) {
        val item = myDataSet[position]
        item.run {
            holder.apply {
                bind(this@run)
                itemView.tag = this@run
                val imgDownload = holder.imgDownload
                val url = myDataSet[position].getVideoUrl(context)
                val uniqueName = myDataSet[position].getVideoUniqueName(context)
                println("UniqueName $uniqueName "+"______ $url")
                createNewDirectory()
                bindImageFromUrl(holder.imageView,item.aari_url+".png")
                imgDownload.visibility = View.GONE
                downloadProgress.visibility = View.GONE
                val isPresentFile = getFileNameFromFolder(uniqueName)
                if (isPresentFile) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imgDownload.setImageDrawable(
                            context.resources.getDrawable(
                                R.drawable.cross_icon,
                                context.theme
                            )
                        )
                    } else {
                        imgDownload.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.cross_icon
                            )
                        )
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imgDownload.setImageDrawable(
                            context.resources.getDrawable(
                                R.drawable.download_icon,
                                context.theme
                            )
                        )
                    } else {
                        imgDownload.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.download_icon
                            )
                        )
                    }
                }

                imgDownload.setOnClickListener {
                    val url = myDataSet[position].getVideoUrl(context)
                    val fileUniqueName = myDataSet[position].getVideoUniqueName(context)
                    val isPresentFile = getFileNameFromFolder(fileUniqueName)
                    if (!isPresentFile) {
                        imgDownload.visibility = View.GONE
                        downloadProgress.visibility = View.VISIBLE
                        val newFileName = getFolderName() + getFileUniqueName(position)
                        encryptVideo(url, File(newFileName), context)
                    } else {
                        val sdCardRoot = Environment.getExternalStorageDirectory()
                        val yourDir = File(sdCardRoot, "HariBackup/")
                        var name = ""
                        if (yourDir.exists() && yourDir.listFiles() != null) {
                            for (f in yourDir.listFiles()) {
                                if (f.isFile) {
                                    file = f
                                    name = f.name
                                    println("NAMEEEEEEEE $name")
                                    if (name == fileUniqueName) {

                                        CommonFunctions.alertDialog(
                                            context,
                                            this@VideoBaseAdapter,
                                            context.getString(R.string.delete_file),
                                            context.getString(R.string.btn_yes),
                                            context.getString(R.string.btn_no),
                                            false, 1, ""
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                rootLayout.findViewById<View>(R.id.imageView).setOnClickListener {
                    val url = myDataSet[position].getVideoUrl(context)
                    val fileUniqueNameImg = myDataSet[position].getVideoUniqueName(context)
                    val isPresentFile = getFileNameFromFolder(fileUniqueNameImg)
                    if (isPresentFile) {
                        redirectToPlayerView(
                            getFileUniqueName(position),
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



    override fun downloadCompleted() {
        println("downloadCompleted")
        updateDownloadIcon()
    }

    override fun onPositiveButtonCLick(dialog: DialogInterface, alertType: Int) {
        file.delete()
        notifyDataSetChanged()
        dialog.dismiss()
    }

    override fun onNegativeButtonCLick(dialog: DialogInterface, alertType: Int) {
        dialog.dismiss()
    }
    private fun updateDownloadIcon() {
        notifyDataSetChanged()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataSet.size

    class PostItemViewHolder(
        private val binding: CertificateListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        var rootLayout = binding.root
        var imageView = binding.imageView
        val imgDownload = binding.imgDownload
        val downloadProgress = binding.downloadProgress
        fun bind(item: VideoListItem) {
            binding.apply {
                listItem = item
                executePendingBindings()
            }
        }
    }

    fun getFileUniqueName(position: Int) : String{
        return myDataSet[position].getVideoUniqueName(context)
    }

    private fun getFileNameFromFolder(uniqueName: String): Boolean {
        val sdCardRoot = Environment.getExternalStorageDirectory()
        val yourDir = File(sdCardRoot, "HariBackup/")
        var name = ""
        var temp = false
        if (yourDir.exists() && yourDir.listFiles() != null) {
            for (f in yourDir.listFiles()) {
                if (f.isFile) {
                    name = f.name
                    if (name == uniqueName) {
                        temp = true
                    }
                    println("File Name $name")
                } else {
                    if (name == uniqueName) {
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
            // you need to encrypt a video somehow with the same key and iv...  you can do that yourself and update
            // the ciphers, key and iv used in this demo, or to see it from top to bottom,
            // supply a url to a remote unencrypted file - this method will download and encrypt it
            // this first argument needs to be that url, not null or empty...
            DownloadAndEncryptFileTask(
                mUrl,
                mEncryptedFile,
                encryptionCipher,
                context,
                this
            ).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun createNewDirectory() {
        val folder =
            Environment.getExternalStorageDirectory().toString() + File.separator + "HariBackup/"
        val mEncryptedFile = File(folder)
        if (!mEncryptedFile.exists()) {
            mEncryptedFile.mkdirs()
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

