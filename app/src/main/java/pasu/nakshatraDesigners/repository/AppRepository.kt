package pasu.nakshatraDesigners.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import pasu.nakshatraDesigners.data.*
import pasu.nakshatraDesigners.network.CoreClient
import pasu.nakshatraDesigners.network.ServiceGenerator
import pasu.nakshatraDesigners.utils.DEVICE_ID
import pasu.nakshatraDesigners.utils.NetworkStatus
import pasu.nakshatraDesigners.utils.Session
import nakshatraDesigners.utils.CommonFunctions.getDeviceID
import pasu.nakshatraDesigners.signIn.data.SignUpDetail
import pasu.nakshatraDesigners.signIn.data.apiData.SignUpResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepository private constructor(val context: Context) {
    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppRepository? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: AppRepository(context).also { instance = it }
            }
    }









     fun dashboardData( sendFeedBackResult: MutableLiveData<DashboardResponseData>) {
        if (NetworkStatus.isOnline(context)) {
            val client = ServiceGenerator(context).createService(CoreClient::class.java)
            val calSignUp = client.getDashBoardData()
            calSignUp.enqueue(object : Callback<DashboardResponseData> {

                override fun onResponse(call: Call<DashboardResponseData>, response: Response<DashboardResponseData>) {
                    if (response.isSuccessful && response.body() != null) {
                        sendFeedBackResult.value = response.body()
                    } else {
                        sendFeedBackResult.value = null
                    }
                }

                override fun onFailure(call: Call<DashboardResponseData>, t: Throwable) {
                    t.printStackTrace()
                    sendFeedBackResult.value = null
                }

            })
        } else
            sendFeedBackResult.value = null

    }






    fun editProfile(profileData: SignUpDetail, sendFeedBackResult: MutableLiveData<ProfileResponseData>) {
        if (NetworkStatus.isOnline(context)) {
            val client = ServiceGenerator(context).createService(CoreClient::class.java)
            val calSignUp = client.editProfile(profileData)
            calSignUp.enqueue(object : Callback<ProfileResponseData> {

                override fun onResponse(call: Call<ProfileResponseData>, response: Response<ProfileResponseData>) {
                    if (response.isSuccessful && response.body() != null) {
                        sendFeedBackResult.value = response.body()
                    } else {
                        sendFeedBackResult.value = null
                    }
                }

                override fun onFailure(call: Call<ProfileResponseData>, t: Throwable) {
                    t.printStackTrace()
                    sendFeedBackResult.value = null
                }

            })
        } else
            sendFeedBackResult.value = null

    }





    fun getProfileData(profileRequestData: ProfileRequestData, sendFeedBackResult: MutableLiveData<ProfileResponseData>) {
        if (NetworkStatus.isOnline(context)) {
            val client = ServiceGenerator(context).createService(CoreClient::class.java)
            val calSignUp = client.getProfileData(profileRequestData)
            calSignUp.enqueue(object : Callback<ProfileResponseData> {

                override fun onResponse(call: Call<ProfileResponseData>, response: Response<ProfileResponseData>) {
                    if (response.isSuccessful && response.body() != null) {
                        sendFeedBackResult.value = response.body()
                    } else {
                        sendFeedBackResult.value = null
                    }
                }

                override fun onFailure(call: Call<ProfileResponseData>, t: Throwable) {
                    t.printStackTrace()
                    sendFeedBackResult.value = null
                }

            })
        } else
            sendFeedBackResult.value = null

    }



    fun getCoreData(sendFeedBackResult: MutableLiveData<SplashResponseData>) {
        if (NetworkStatus.isOnline(context)) {
            val client = ServiceGenerator(context).createService(CoreClient::class.java)
            val calSignUp = client.getCoreData()
            calSignUp.enqueue(object : Callback<SplashResponseData> {

                override fun onResponse(call: Call<SplashResponseData>, response: Response<SplashResponseData>) {
                    if (response.isSuccessful && response.body() != null) {
                        sendFeedBackResult.value = response.body()
                    } else {
                        sendFeedBackResult.value = null
                    }
                }

                override fun onFailure(call: Call<SplashResponseData>, t: Throwable) {
                    t.printStackTrace()
                    sendFeedBackResult.value = null
                }

            })
        } else
            sendFeedBackResult.value = null

    }

    fun feedBackData(feedBackData: FeedBackData,sendFeedBackResult: MutableLiveData<SimpleResponseData>) {
        if (NetworkStatus.isOnline(context)) {
            val client = ServiceGenerator(context).createService(CoreClient::class.java)
            val calSignUp = client.feedBack(feedBackData)
            calSignUp.enqueue(object : Callback<SimpleResponseData> {

                override fun onResponse(call: Call<SimpleResponseData>, response: Response<SimpleResponseData>) {
                    if (response.isSuccessful && response.body() != null) {
                        sendFeedBackResult.value = response.body()
                    } else {
                        sendFeedBackResult.value = null
                    }
                }

                override fun onFailure(call: Call<SimpleResponseData>, t: Throwable) {
                    t.printStackTrace()
                    sendFeedBackResult.value = null
                }

            })
        } else
            sendFeedBackResult.value = null

    }






    fun signUp(feedBackData: RegisterData,sendFeedBackResult: MutableLiveData<SignUpResponseData>) {
        if (NetworkStatus.isOnline(context)) {
            val client = ServiceGenerator(context).createService(CoreClient::class.java)
            val calSignUp = client.signUp(feedBackData)
            calSignUp.enqueue(object : Callback<SignUpResponseData> {

                override fun onResponse(call: Call<SignUpResponseData>, response: Response<SignUpResponseData>) {
                    if (response.isSuccessful && response.body() != null) {
                        sendFeedBackResult.value = response.body()
                    } else {
                        sendFeedBackResult.value = null
                    }
                }

                override fun onFailure(call: Call<SignUpResponseData>, t: Throwable) {
                    t.printStackTrace()
                    sendFeedBackResult.value = null
                }

            })
        } else
            sendFeedBackResult.value = null

    }








    fun logout(logoutRequestData: LogoutRequestData, sendFeedBackResult: MutableLiveData<LogoutResponse>) {
        if (NetworkStatus.isOnline(context)) {
            val client = ServiceGenerator(context).createService(CoreClient::class.java)
            val calSignUp = client.logout(logoutRequestData)
            calSignUp.enqueue(object : Callback<LogoutResponse> {

                override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        sendFeedBackResult.value = response.body()
                    } else {
                        sendFeedBackResult.value = null
                    }
                }

                override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                    t.printStackTrace()
                    sendFeedBackResult.value = null
                }

            })
        } else
            sendFeedBackResult.value = null

    }



}