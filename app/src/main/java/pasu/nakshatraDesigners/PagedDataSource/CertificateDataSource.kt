package pasu.nakshatraDesigners.PagedDataSource

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.google.gson.Gson
import nakshatraDesigners.utils.CommonFunctions
import pasu.nakshatraDesigners.data.VideoListItem
import pasu.nakshatraDesigners.data.CertificateListRequest
import pasu.nakshatraDesigners.network.CoreClient
import pasu.nakshatraDesigners.network.ServiceGenerator
import pasu.nakshatraDesigners.utils.*
import java.io.IOException


class CertificateDataSource(val context: Context, val courseUrl: String) : PageKeyedDataSource<String, VideoListItem>() {


    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, VideoListItem>) {
//        networkState.postValue(NetworkState.LOADING)
//        ServiceGenerator(context).createService(CoreClient::class.java).getTopAfter(
//            CertificateListRequest(Session.getSession(USER_ID, context), 10, params.key)
//        ).enqueue(object : Callback<CertificateListResponse> {
//            override fun onFailure(call: Call<CertificateListResponse>, t: Throwable) {
//
//            }
//
//            override fun onResponse(
//                call: Call<CertificateListResponse>,
//                item: Response<CertificateListResponse>
//            ) {
//                if (item.isSuccessful) {
//
//                    val Detaildata = item.body()
//                    val items = Detaildata?.Detaildata?.map { it }?: emptyList()
//                    println("dataCameee2 ${items} ${Detaildata}")
//
//
//                    if (item.isSuccessful) {
//                        Detaildata?.let {
//                            val nextKey = items.get(items.size - 1).aari_name
//
//                            callback.onResult(items, nextKey)
//                            networkState.postValue(NetworkState.LOADED)
//                        }
//
//
//                    } else
//                        networkState.postValue(NetworkState(NetworkState.Status.FAILED,item.Message()))
//
//
////                    callback.onResult(items, "1")
//                    networkState.postValue(NetworkState.LOADED)
//                } else {
//
//                }
//            }
//
//        })
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, VideoListItem>) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val initialLoading = MutableLiveData<NetworkState>()
    val networkState = MutableLiveData<NetworkState>()


    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, VideoListItem>
    ) {
        initialLoading.postValue(NetworkState.LOADING)
        networkState.postValue(NetworkState.LOADING)


        val request = ServiceGenerator(context).createService(CoreClient::class.java).getTop(courseUrl,
            CertificateListRequest(Session.getSession(USER_ID, context), Session.getAccessKey(context),
                10, "1",CommonFunctions.getDeviceID(context))
        )



        networkState.postValue(NetworkState.LOADING)
        initialLoading.postValue(NetworkState.LOADING)

        // triggered by a refresh, we better execute sync
        try {
            val response = request.execute()
            val data = response.body()
            val arrayList = ArrayList<VideoListItem>()
            Session.save(VIDEO_URL, data?.data?.videourl, context)
            Session.save(IMAGE_URL, data?.data?.imageurl, context)
            Session.save("ses_showExp", data?.data?.timedisplay, context)
            if (data?.responseCode == 401)
                Session.save("ses_video_401", Gson().toJson(data?.data), context)
            else
                Session.save("ses_video_401", "", context)
            //convertFromDuration(((socialmedialink?.socialmedialink?.expirydatetime ?:0)-(socialmedialink?.socialmedialink?.currenttime ?:0))).toString()
            Session.save(
                EXPIRY_TIME,
                ((data?.data?.expirydatetime ?: 0) - (data?.data?.currenttime ?: 0)).toString(),
                context
            )
            if (data?.data?.start != null) {
                arrayList.add(VideoListItem(data.data.start_title, "", "", 1))
                arrayList.addAll(data?.data?.start)
            }
            if (data?.data?.basic != null) {
                arrayList.add(VideoListItem(data.data.basic_title, "", "", 1))
                arrayList.addAll(data?.data?.basic)
            }
            if (data?.data?.advance != null) {
                arrayList.add(VideoListItem(data.data.advance_title, "", "", 1))
                arrayList.addAll(data?.data?.advance)
            }

            val items = arrayList.map { it } ?: emptyList()

            println("dataCameee ${items}  ${data}")
            networkState.postValue(NetworkState.LOADED)
            initialLoading.postValue(NetworkState.LOADED)
            callback.onResult(items, "1", "3")
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }


    }


}