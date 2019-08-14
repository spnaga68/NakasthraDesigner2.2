package pasu.nakshatraDesigners.signIn.viewModel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pasu.nakshatraDesigners.MyApplication
import pasu.nakshatraDesigners.signIn.repository.SignUpRepository

class MobileRegViewModel internal constructor(val signUpRepository: SignUpRepository, val application: MyApplication) : AndroidViewModel(application) {
    val showGetPasswordLayout = MutableLiveData<Boolean>()
    val showLoadingLayout = MutableLiveData<Boolean>()

}