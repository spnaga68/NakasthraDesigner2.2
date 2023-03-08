package pasu.nakshatraDesigners.network

import android.content.Context
import android.text.TextUtils
import android.util.Base64
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import okio.BufferedSink
import pasu.nakshatraDesigners.BuildConfig
import pasu.nakshatraDesigners.utils.BASEURL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.GeneralSecurityException
import java.util.concurrent.TimeUnit


class ServiceGenerator(context: Context,  baseUrl: String = BASEURL, timeOut: Long = 30L) {
    private val httpClient: OkHttpClient.Builder
    private val builder: Retrofit.Builder

    init {
        val logging = HttpLoggingInterceptor()
        val b = RequestInterceptor(context)
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient = OkHttpClient.Builder().connectTimeout(timeOut, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
        val d = DecryptInterceptor(context)
        httpClient.interceptors().add(b)
        httpClient.addInterceptor(d)
        if (BuildConfig.DEBUG)
            httpClient.interceptors().add(logging)
        builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
    }

    inner class RequestInterceptor internal constructor(internal var c: Context) : Interceptor {


        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val originalRequest = chain.request()
            var builder: Request.Builder = originalRequest.newBuilder()
            if (originalRequest.method.equals("POST", ignoreCase = true)) {
                builder = originalRequest.newBuilder()
                    .method(originalRequest.method, originalRequest.body)
//                        .method(originalRequest.method(), encode((originalRequest.body()!!)))
            }
            builder.addHeader("Authorization","Grocery")
            builder.addHeader("Content-type", "application/json")
            builder.addHeader("version", "${BuildConfig.VERSION_CODE}")



            val originalHttpUrl = originalRequest.url
            val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("v", "" + BuildConfig.VERSION_CODE)
                    .build()
            builder.url(url)

            return chain.proceed(builder.build())
        }

    }


    inner class DecryptInterceptor internal constructor(internal var c: Context) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {

            val response = chain.proceed(chain.request())
            if (response.isSuccessful) {
                val newResponse = response.newBuilder()
                var contentType = response.header("Content-Type")
                if (TextUtils.isEmpty(contentType)) contentType = "application/json"
                val data = response.body
                if (data != null) {
                    val cryptedStream = data.byteStream()
                    var decrypted: String? = null
                    val result = ByteArrayOutputStream()
                    val buffer = ByteArray(1024)
                    var length: Int = cryptedStream.read(buffer)
                    while (length != -1) {
                        result.write(buffer, 0, length)
                        length = cryptedStream.read(buffer)
                    }

                    try {
//                        if (!result.toString("UTF-8").isEmpty())
//                            decrypted = AA().dd(result.toString("UTF-8"))
                        decrypted = result.toString("UTF-8")


                        newResponse.body(decrypted.toResponseBody(contentType!!.toMediaTypeOrNull()))
                        val ress = newResponse.build()
//                        if (CheckStatus(JSONObject(decrypted), c).isNormal())
                            return ress
//                        else
//                            return response
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                }

            }
            return response
        }


    }

    fun <S> createService(serviceClass: Class<S>): S {
        val retrofit = builder.client(httpClient.build()).build()
        return retrofit.create(serviceClass)
    }


    private fun encode(body: RequestBody): RequestBody {
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return body.contentType()
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                val buffer = Buffer()
                body.writeTo(buffer)

                val to_encode =String(buffer.readByteArray());
                var encodedString = ""
                try {
                    encodedString =to_encode.encode()
                } catch (e: GeneralSecurityException) {
                    e.printStackTrace()
                }

                System.out.println("TOENCODE $to_encode Encoded   $encodedString")
                sink.write(encodedString.toByteArray(charset("ISO-8859-1")))
                buffer.close()
                sink.close()
                // Systems.out.println("*******eeee" + encodedString.toString());
            }
        }
    }


    fun String.encode(): String {
        return Base64.encodeToString(this.toByteArray(charset("UTF-8")), Base64.DEFAULT)
    }


}