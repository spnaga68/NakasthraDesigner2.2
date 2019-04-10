package pasu.nakshatraDesigners.network

import pasu.nakshatraDesigners.data.*
import pasu.nakshatraDesigners.signIn.data.*
import pasu.nakshatraDesigners.signIn.data.apiData.PasswordRequestData
import pasu.nakshatraDesigners.signIn.data.apiData.SignInRequestData
import pasu.nakshatraDesigners.signIn.data.apiData.SignUpResponseData
import pasu.nakshatraDesigners.signIn.data.apiData.SubmitOtpData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by developer on 8/31/16.
 */

interface CoreClient {

    @GET("contactdetails.html")
    fun getCoreData(): Call<SplashResponseData>


    @POST("contactus.html")
    fun feedBack(@Body feedBackData: FeedBackData): Call<SimpleResponseData>
    @POST("userregister.html")
    fun signUp(@Body feedBackData: RegisterData): Call<SignUpResponseData>


    @GET("homepagedetails.html")
    fun getDashBoardData(): Call<DashboardResponseData>

    @POST("userdetails.html")
    fun getProfileData(@Body profileRequestData: ProfileRequestData): Call<ProfileResponseData>

    @POST("profileupdate.html")
    fun editProfile(@Body profileData: SignUpDetail): Call<ProfileResponseData>


    @POST("mobileapi100/user_logout")
    fun logout(@Body logoutRequestData: LogoutRequestData): Call<LogoutResponse>

    @POST("aarionlineclasses.html")
    fun getTop(@Body certificateListRequest: CertificateListRequest): Call<CertificateListResponse>

    @POST("aarionlineclasses.html")
    fun getTopAfter(@Body certificateListRequest: CertificateListRequest): Call<CertificateListResponse>
    @POST("mobileapi100/otp_process")
    fun submitOtp(@Body submitOtpData: SubmitOtpData): Call<SignUpResponseData>

    @POST("userlogin.html")
    fun signIn(@Body submitOtpData: SignInRequestData): Call<SignUpResponseData>


    @POST("resendOtp")
    fun resendOtp(@Body resendOtpData: ResendOtpData): Call<ResendOtpResponseData>

    @POST("verifyPassword")
    fun submitPassword(@Body submitOtpData: PasswordRequestData): Call<SignUpResponseData>


    @POST("mobileapi100/forgot_password")
    fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Call<ForgotPasswordResponseData>


}
