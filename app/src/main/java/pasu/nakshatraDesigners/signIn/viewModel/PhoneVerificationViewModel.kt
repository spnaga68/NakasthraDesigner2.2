package pasu.nakshatraDesigners.signIn.viewModel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pasu.nakshatraDesigners.MyApplication
import pasu.nakshatraDesigners.signIn.data.ResendOtpResponseData
import pasu.nakshatraDesigners.signIn.data.apiData.SignUpResponseData
import pasu.nakshatraDesigners.signIn.repository.SignUpRepository

class PhoneVerificationViewModel internal constructor(private val signUpRepository: SignUpRepository, val application: MyApplication) : AndroidViewModel(application) {
    val showGetPasswordLayout = MutableLiveData<Boolean>()
    val showLoadingLayout = MutableLiveData<Boolean>()
    private val resendOtpResult = MutableLiveData<ResendOtpResponseData>()
    val submitOtpResult = MutableLiveData<SignUpResponseData>()
    fun submitOtp(phoneNo: String, countryCode: String, userOtp: String,otpUnique:String): MutableLiveData<SignUpResponseData> {
        signUpRepository.submitOtp(phoneNo, countryCode, userOtp,otpUnique, submitOtpResult)
        return submitOtpResult
    }

    fun resendOtp(phoneNo: String, countryCode: String, userOtp: String): MutableLiveData<ResendOtpResponseData> {
        signUpRepository.resendOtp(phoneNo, countryCode, userOtp, resendOtpResult)
        return resendOtpResult
    }

}