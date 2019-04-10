package pasu.nakshatraDesigners.signIn.viewModel


import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pasu.nakshatraDesigners.MyApplication
import pasu.nakshatraDesigners.signIn.data.apiData.SignUpResponseData
import pasu.nakshatraDesigners.signIn.repository.SignUpRepository

class SignInViewModel internal constructor(private val signUpRepository: SignUpRepository, val application: MyApplication) : AndroidViewModel(application) {
    val showLoadingLayout = MutableLiveData<Boolean>()
    val data = MutableLiveData<SignUpResponseData>()
    fun callSignIn(phoneNo: String, countryCode: String, pwd: String): MutableLiveData<SignUpResponseData> {
        signUpRepository.callSignIn(phoneNo, countryCode, pwd, data)
        return data
    }


}