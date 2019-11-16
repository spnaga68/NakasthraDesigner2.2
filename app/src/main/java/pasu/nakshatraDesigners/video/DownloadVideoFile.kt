package pasu.nakshatraDesigners.video

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.Toast
import pasu.nakshatraDesigners.utils.Session
import java.io.*
import java.net.URL
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.SecretKeySpec

class DownloadVideoFile(context: Context,url:String) : AsyncTask<String, String, String>() {
    private var progressDialog: ProgressDialog? = null
    private var fileName: String = ""
    private var folder: String = ""
    internal var data = ByteArray(1024)
    private  var mContext:Context = context
    override fun onPreExecute() {
        super.onPreExecute()
        progressDialog = ProgressDialog(mContext)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }
    override fun doInBackground(vararg f_url: String): String {
        var count: Int
        try {
            val url = URL(f_url[0])
            val connection = url.openConnection()
            connection.connect()
            // getting file length
            val lengthOfFile = connection.contentLength


            // input stream to read file - with 8k buffer
            val input = BufferedInputStream(url.openStream(), 8192)

            val timestamp = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(Date())

            //Extract file name from URL
            fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length)

            //Append timestamp to file name
//            fileName = "Nandhini_$fileName"

            //External directory path to save file
            folder = Environment.getExternalStorageDirectory().toString() + File.separator + "HariBackup/"


            //Create androiddeft folder if it does not exist
            val directory = File(folder)

            if (!directory.exists()) {
                directory.mkdirs()
            }

            Session.save("FILE_NAME", fileName, mContext)
            Session.save("FOLDER_NAME", folder, mContext)
            Session.save("OUTPUT_PATH", folder!! + fileName!!, mContext)

            // Output stream to write file
            val output = FileOutputStream(Session.getSession("OUTPUT_PATH", mContext))
            println("output" + folder + "____" + fileName + "____")
            data = ByteArray(1024)
            //                count = input.read(data);
            var total: Long = 0
            count = input.read(data)
            while ((input.read(data)) != -1) {
                total += input.read(data).toLong()
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (total * 100 / lengthOfFile).toInt())
                Log.d("", "Progress: " + (total * 100 / lengthOfFile).toInt())
                val str = Base64.encodeToString(data, 1024)
                println("data" + Base64.decode(str, 1024))
                // writing data to file
                output.write(data, 0, input.read(data))
            }


            // flushing output
            output.flush()

            // closing streams
            output.close()
            input.close()
            return "Downloaded at: $folder$fileName"

        } catch (e: Exception) {
            Log.e("Error: ", e.message)
        }

        return "Something went wrong"

    }

    override fun onPostExecute(message: String?) {
        // dismiss the dialog after the file was downloaded
        progressDialog!!.dismiss()

        // Display File path after downloading
        Toast.makeText(mContext,
            message, Toast.LENGTH_LONG).show()

        try {
            val str = Session.getSession("OUTPUT_PATH", mContext)
            encryptfile(str, "Hari_2019")
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        }
    }


    fun encryptfile(path: String, password: String) {
        val fis = FileInputStream(path)
        val fos = FileOutputStream("$path.crypt")
        var key = (data.toString() + password).toByteArray(charset("UTF-8"))
        val sha = MessageDigest.getInstance("SHA-1")
        key = sha.digest(key)
        key = Arrays.copyOf(key, 16)
        val sks = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, sks)
        val cos = CipherOutputStream(fos, cipher)
        var b: Int
        //        byte[] d = new byte[8];
        val d = ByteArray(1024 * 1024)
        b = fis.read(d)
        while ((fis.read(d)) != -1) {
            cos.write(d, 0, b)
        }

        cos.flush()
        cos.close()
        fis.close()
        val uri = Uri.parse(Session.getSession("OUTPUT_PATH", mContext))
        val fdelete = File(uri.path)
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                println("file Deleted :" + uri.path!!)
            } else {
                println("file not Deleted :" + uri.path!!)
            }
        }

        //External directory path to save file
        val folder = Environment.getExternalStorageDirectory().toString() + File.separator + "Hari/"

        //Create androiddeft folder if it does not exist
        val directory = File(folder)

        if (!directory.exists()) {
            directory.mkdirs()
        }
        println("folder$folder")

        try {
            decrypt(Session.getSession("OUTPUT_PATH", mContext) + ".crypt", "Hari_2019", folder)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        }

    }

    @Throws(
        IOException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class
    )
    fun decrypt(path: String, password: String, outPath: String) {
        val fis = FileInputStream(path)
        //        FileOutputStream fos = new FileOutputStream(outPath);
        val fos = FileOutputStream(outPath + Session.getSession("FILE_NAME", mContext))
        var key = (data.toString() + password).toByteArray(charset("UTF-8"))
        val sha = MessageDigest.getInstance("SHA-1")
        key = sha.digest(key)
        key = Arrays.copyOf(key, 16)
        val sks = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, sks)
        val cis = CipherInputStream(fis, cipher)
        var b: Int
        //        byte[] d = new byte[8];
        val d = ByteArray(1024 * 1024)
        b = cis.read(d)
        println("decrypt" + b)
        while ((cis.read(d)) != -1) {
            println("decrypt______________" + d)
            fos.write(d, 0, b)
        }
        fos.flush()
        fos.close()
        cis.close()
        Session.save("FILE_NAME", "", mContext)
        Session.save("OUTPUT_PATH", "", mContext)
    }
}