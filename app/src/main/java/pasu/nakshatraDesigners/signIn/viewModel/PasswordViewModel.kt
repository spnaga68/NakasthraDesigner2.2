package pasu.nakshatraDesigners.signIn.viewModel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pasu.nakshatraDesigners.MyApplication
import pasu.nakshatraDesigners.signIn.data.ForgotPasswordResponseData
import pasu.nakshatraDesigners.signIn.data.apiData.SignUpResponseData
import pasu.nakshatraDesigners.signIn.repository.SignUpRepository

class PasswordViewModel internal constructor(private val signUpRepository: SignUpRepository, val application: MyApplication) : AndroidViewModel(application) {
    val showGetPasswordLayout = MutableLiveData<Boolean>()
    val showLoadingLayout = MutableLiveData<Boolean>()
    val submitPasswordResult = MutableLiveData<SignUpResponseData>()
    val forgotPasswordResult = MutableLiveData<ForgotPasswordResponseData>()

    fun submitPassword(userId: String, userPhone: String, userCountryCode: String, userPassword: String): MutableLiveData<SignUpResponseData> {
        signUpRepository.submitPassword(userId, userPhone, userCountryCode, userPassword, submitPasswordResult)
        return submitPasswordResult
    }

    fun forgotPassword(userPhone: String, userCountryCode: String): MutableLiveData<ForgotPasswordResponseData> {
        signUpRepository.forgotPassword(userPhone, userCountryCode,forgotPasswordResult)
        return forgotPasswordResult
    }
}