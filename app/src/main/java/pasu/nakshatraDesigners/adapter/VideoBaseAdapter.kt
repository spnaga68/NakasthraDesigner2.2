package pasu.nakshatraDesigners.adapter


import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.VideoListActivity
import pasu.nakshatraDesigners.adapter.VideoBaseAdapter.PostItemViewHolder
import pasu.nakshatraDesigners.data.VideoListItem
import pasu.nakshatraDesigners.databinding.CertificateListItemBinding
import pasu.nakshatraDesigners.utils.VIDEO_URL
import pasu.nakshatraDesigners.video.DownloadAndEncryptFileTask
import java.io.File
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class VideoBaseAdapter(val context: Context, val myDataSet: ArrayList<VideoListItem>) :
    RecyclerView.Adapter<PostItemViewHolder>() {

    // Create new views (invoked by the layout manager)
    private val ENCRYPTED_FILE_NAME = ".mp4.crypt"

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
        val item = myDataSet.get(position)
        item.run {
            holder.apply {
                bind(this@run)
                itemView.tag = this@run
                val imgDownload = rootLayout.findViewById<AppCompatImageView>(R.id.imgDownload)

                /* rootLayout.findViewById<View>(R.id.ViewImage).setOnClickListener {

                         val intent = Intent(context, VideoListActivity::class.java)
                         intent.apply {
                             putExtra(VIDEO_URL, myDataSet.get(position)?.getVideoUrl(context))
                             context.startActivity(intent)
                         }

         //                            Navigation.findNavController(context as Activity, R.id.nav_host_frag).navigate(action)
                     }*/

                val url = myDataSet[position].getVideoUrl(context)
                println("textView_download $url")
                createNewDirectory()
                println(
                    "File Nameee${getFilenameFromUrl(url)}_____Format${getFilenameFromUrlWithFormat(
                        url
                    )}"
                )
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
                    val url = myDataSet[position].getVideoUrl(context)
                    val isPresentFile = getFileNameFromFolder(url)
                    val originalFileName = getFilenameFromUrlWithFormat(url)
                    if (!isPresentFile) {
                        val newFileName = getFolderName() + originalFileName
                        encryptVideo(url,File(newFileName),context)
                    }else{

                    }
                }

                rootLayout.findViewById<View>(R.id.imageView).setOnClickListener {
                    val url = myDataSet[position].getVideoUrl(context)
                    val isPresentFile = getFileNameFromFolder(url)

                    val originalFileName = getFilenameFromUrlWithFormat(url)
                    if (isPresentFile) {
                        renameFileName(
                            "$originalFileName.crypt",
                            originalFileName,
                            url,
                            isPresentFile
                        )
                    }else{
                        val intent = Intent(context, VideoListActivity::class.java)
                        intent.apply {
                            putExtra(VIDEO_URL, url)
                            putExtra("FILEPATH", "")
                            putExtra("ISFILEPRESENT", false)
                            context.startActivity(intent)
                        }
                    }
                }
                rootLayout.findViewById<View>(R.id.textViewDownload).setOnClickListener {
                    val url = myDataSet[position].getVideoUrl(context)
                    println("textView_download $url")
                    println(
                        "File Nameee${getFilenameFromUrl(url)}_____Format${getFilenameFromUrlWithFormat(
                            url
                        )}"
                    )
                    val isPresentFile = getFileNameFromFolder(url)
                    val originalFileName = getFilenameFromUrlWithFormat(url)
                    if (isPresentFile) {
//                        moveFile(getFolderName(),getInputFolderName())
                        renameFileName(
                            "$originalFileName.crypt",
                            originalFileName,
                            url,
                            isPresentFile
                        )
                    } else {
                        val newFileName = getFolderName() + originalFileName
                        encryptVideo(url,File(newFileName),context)
                    }
                }
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataSet.size

    class PostItemViewHolder(
        private val binding: CertificateListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        var rootLayout = binding.root
        fun bind(item: VideoListItem) {
            binding.apply {
                listItem = item
                executePendingBindings()
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

    private fun renameFileName(
        name: String,
        originalFileName: String,
        url: String,
        isPresentFile: Boolean
    ) {
        val from = File(getFolderName(), name)
        val to = File(getFolderName(), getFileNameRemove(name))
        println("renameFileName From ${from.absolutePath}__${to.absolutePath}")
        from.renameTo(to)
        val intent = Intent(context, VideoListActivity::class.java)
        intent.apply {
            val filePath = getFolderName() + originalFileName
            println("isPresentFile$filePath")
            putExtra(VIDEO_URL, url)
            putExtra("FILEPATH", filePath)
            putExtra("ISFILEPRESENT", isPresentFile)
            context.startActivity(intent)
        }
    }
}

fun getFileNameRemove(name: String): String {
    if (name.contains(".mp4.crypt")) {
        return name.substring(0, name.lastIndexOf("."))
    }
    return name
}


val AES_ALGORITHM = "AES"
val AES_TRANSFORMATION = "AES/CTR/NoPadding"
private val KEY = "cybervaultsecure"
private var mSecretKeySpec: SecretKeySpec? = null
private var mIvParameterSpec: IvParameterSpec? = null
private fun encryptVideo(mUrl:String, mEncryptedFile : File,context: Context) {
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
        DownloadAndEncryptFileTask(mUrl, mEncryptedFile, encryptionCipher,context).execute()
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

fun getFolderName(): String {
    return Environment.getExternalStorageDirectory().toString() + File.separator + "HariBackup/"
}

private fun getFilenameFromUrl(url: String): String {
    var urlFileName = url.substring(url.lastIndexOf('/') + 1)
    if (urlFileName.contains(".mp4")) {
        urlFileName = urlFileName.substring(0, urlFileName.lastIndexOf("."))
    }
    return urlFileName
}

fun getFilenameFromUrlWithFormat(url: String): String {
    return url.substring(url.lastIndexOf('/') + 1)
}

