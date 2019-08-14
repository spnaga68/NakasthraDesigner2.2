package pasu.nakshatraDesigners.signIn.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import pasu.nakshatraDesigners.network.CoreClient
import pasu.nakshatraDesigners.network.ServiceGenerator
import pasu.nakshatraDesigners.signIn.data.*
import pasu.nakshatraDesigners.signIn.data.apiData.PasswordRequestData
import pasu.nakshatraDesigners.signIn.data.apiData.SignInRequestData
import pasu.nakshatraDesigners.signIn.data.apiData.SignUpResponseData
import pasu.nakshatraDesigners.signIn.data.apiData.SubmitOtpData
import pasu.nakshatraDesigners.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpRepository private constructor( val context: Context) {
    private var TAG = SignUpRepository::class.java.canonicalName

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: SignUpRepository? = null

        fun getInstance( context: Context) =
                instance ?: synchronized(this) {
                    instance
                            ?: SignUpRepository( context).also { instance = it }
                }
    }


    val signUpWithFacebookResult = MutableLiveData<FacebookSingInResponse>()
    private val dataConnectionStatus = MutableLiveData<Boolean>()


    fun submitOtp(phoneNo: String, countryCode: String, userOtp: String,otpUnique:String, submitOtpResult: MutableLiveData<SignUpResponseData>){
        callSubmitOtp(phoneNo, countryCode, userOtp,otpUnique,submitOtpResult)
    }

    fun resendOtp(phoneNo: String, countryCode: String, userOtp: String, resendOtpResult: MutableLiveData<ResendOtpResponseData>){
        callResendOtp(phoneNo, countryCode, userOtp,resendOtpResult)
    }

    fun submitPassword(userId: String, userPhone: String, userCountryCode: String, userPassword: String, submitPasswordResult: MutableLiveData<SignUpResponseData>) {
        callSubmitPassword(userId, userPhone, userCountryCode, userPassword, submitPasswordResult)
    }



    fun forgotPassword(userPhone: String, userCountryCode: String, forgotPasswordResult: MutableLiveData<ForgotPasswordResponseData>){
        callForgotPasswordApi(userPhone, userCountryCode,forgotPasswordResult)
    }

    private fun callForgotPasswordApi(userPhone: String, userCountryCode: String, forgotPasswordResult: MutableLiveData<ForgotPasswordResponseData>) {
        if (NetworkStatus.isOnline(context)) {
            dataConnectionStatus.value = false
            val client = ServiceGenerator(context).createService(CoreClient::class.java)
            val calSignUp = client.forgotPassword(ForgotPasswordRequest(userPhone, userCountryCode))

            calSignUp.enqueue(object : Callback<ForgotPasswordResponseData> {
                override fun onResponse(call: Call<ForgotPasswordResponseData>, response: Response<ForgotPasswordResponseData>) {
                    try {
                        if (response.isSuccessful && response.body() != null) {
                            forgotPasswordResult.value = response.body()
                            println("$TAG onResponse size:${response.body()}")
                        } else {
                            forgotPasswordResult.value = null
                            println("$TAG :${response.isSuccessful} *** ${response.message()}")
                        }
                    } catch (t: Exception) {
                        println("$TAG onResponse :${t.message}   ${t.localizedMessage}")
                        forgotPasswordResult.value = null
                    }
                }

                override fun onFailure(call: Call<ForgotPasswordResponseData>, t: Throwable) {
                    t.printStackTrace()
                    forgotPasswordResult.value = null
                    println("$TAG onFailure :${t.message}   ${t.localizedMessage}")
                }

            })
        } else
            dataConnectionStatus.value = true
    }


    private fun callSubmitPassword(userId: String, userPhone: String, userCountryCode: String, userPassword: String, submitPasswordResult: MutableLiveData<SignUpResponseData>) {
        if (NetworkStatus.isOnline(context)) {
            dataConnectionStatus.value = false
            val client = ServiceGenerator(context).createService(CoreClient::class.java)
            val calSignUp = client.submitPassword(PasswordRequestData(userId, userPhone, userCountryCode, userPassword, Session.getSession(DEVICE_ID, context), Session.getSession(DEVICE_TOKEN, context)))
            calSignUp.enqueue(object : Callback<SignUpResponseData> {

                override fun onResponse(call: Call<SignUpResponseData>, response: Response<SignUpResponseData>) {
                    try {
                        if (response.isSuccessful && response.body() != null) {
                            val desData = response.body() as SignUpResponseData
                            submitPasswordResult.value = desData
                            println("$TAG onResponse size:${response.body()}")
                        } else {
                            submitPasswordResult.value = null
                            println("$TAG :${response.isSuccessful} *** ${response.message()}")
                        }
                    } catch (t: Exception) {
                        println("$TAG onResponse :${t.message}   ${t.localizedMessage}")
                        submitPasswordResult.value = null
                    }
                }

                override fun onFailure(call: Call<SignUpResponseData>, t: Throwable) {
                    t.printStackTrace()
                    submitPasswordResult.value = null
                    println("$TAG onFailure :${t.message}   ${t.localizedMessage}")
                }

            })
        } else
            dataConnectionStatus.value = true
    }

    private fun callResendOtp(phoneNo: String, countryCode: String, userOtp: String, resendOtpResult: MutableLiveData<ResendOtpResponseData>) {
        if (NetworkStatus.isOnline(context)) {
            dataConnectionStatus.value = false
            val client = ServiceGenerator(context).createService(CoreClient::class.java)
            val callResendOtp = client.resendOtp(ResendOtpData(phoneNo, countryCode, userOtp))
            callResendOtp.enqueue(object : Callback<ResendOtpResponseData> {

                override fun onResponse(call: Call<ResendOtpResponseData>, response: Response<ResendOtpResponseData>) {
                    try {
                        if (response.isSuccessful && response.body() != null) {
                            val desData = response.body() as ResendOtpResponseData
                            resendOtpResult.value = desData
                            println("$TAG onResponse size:${response.body()}")
                        } else {
                            resendOtpResult.value = null
                            println("$TAG :${response.isSuccessful} *** ${response.message()}")
                        }
                    } catch (t: Exception) {
                        println("$TAG onResponse :${t.message}   ${t.localizedMessage}")
                        resendOtpResult.value = null
                    }
                }

                override fun onFailure(call: Call<ResendOtpResponseData>, t: Throwable) {
                    t.printStackTrace()
                    resendOtpResult.value = null
                    println("$TAG onFailure :${t.message}   ${t.localizedMessage}")
                }

            })
        } else
            dataConnectionStatus.value = true
    }


    private fun callSubmitOtp(phoneNo: String, countryCode: String, userOtp: String, otpUnique:String,submitOtpResult: MutableLiveData<SignUpResponseData>) {
        if (NetworkStatus.isOnline(context)) {
            dataConnectionStatus.value = false
            val client = ServiceGenerator(context).createService(CoreClient::class.java)
            val calSignUp = client.submitOtp(SubmitOtpData(phoneNo, countryCode, userOtp,otp_unique = otpUnique))
            calSignUp.enqueue(object : Callback<SignUpResponseData> {

                override fun onResponse(call: Call<SignUpResponseData>, response: Response<SignUpResponseData>) {
                    try {
                        if (response.isSuccessful && response.body() != null) {
                            val desData = response.body() as SignUpResponseData
                            submitOtpResult.value = desData
                            println("$TAG onResponse size:${response.body()}")
                        } else {
                            submitOtpResult.value = null
                            println("$TAG :${response.isSuccessful} *** ${response.message()}")
                        }
                    } catch (t: Exception) {
                        println("$TAG onResponse :${t.message}   ${t.localizedMessage}")
                        submitOtpResult.value = null
                    }
                }

                override fun onFailure(call: Call<SignUpResponseData>, t: Throwable) {
                    t.printStackTrace()
                    submitOtpResult.value = null
                    println("$TAG onFailure :${t.message}   ${t.localizedMessage}")
                }

            })
        } else
            dataConnectionStatus.value = true
    }

     fun callSignIn(phoneNo: String, countryCode: String, pwd: String, submitOtpResult: MutableLiveData<SignUpResponseData>) {
        if (NetworkStatus.isOnline(context)) {
            dataConnectionStatus.value = false
            val client = ServiceGenerator(context).createService(CoreClient::class.java)
            val calSignUp = client.signIn(
                SignInRequestData(phoneNo, countryCode, pwd,Session.getSession(DEVICE_ID,context),Session.getSession(
                DEVICE_TOKEN,context),
                DEVICE_TYPE)
            )
            calSignUp.enqueue(object : Callback<SignUpResponseData> {

                override fun onResponse(call: Call<SignUpResponseData>, response: Response<SignUpResponseData>) {
                    try {
                        if (response.isSuccessful && response.body() != null) {
                            val desData = response.body() as SignUpResponseData
                            submitOtpResult.value = desData
                            println("$TAG onResponse size:${response.body()}")
                        } else {
                            submitOtpResult.value = null
                            println("$TAG :${response.isSuccessful} *** ${response.message()}")
                        }
                    } catch (t: Exception) {
                        println("$TAG onResponse :${t.message}   ${t.localizedMessage}")
                        submitOtpResult.value = null
                    }
                }

                override fun onFailure(call: Call<SignUpResponseData>, t: Throwable) {
                    t.printStackTrace()
                    submitOtpResult.value = null
                    println("$TAG onFailure :${t.message}   ${t.localizedMessage}")
                }

            })
        } else
            dataConnectionStatus.value = true
    }
}