package pasu.nakshatraDesigners.viewModel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pasu.nakshatraDesigners.MyApplication
import pasu.nakshatraDesigners.data.FeedBackData
import pasu.nakshatraDesigners.data.RegisterData
import pasu.nakshatraDesigners.data.SimpleResponseData
import pasu.nakshatraDesigners.repository.AppRepository
import pasu.nakshatraDesigners.signIn.data.apiData.SignUpResponseData

class RegisterViewModel(application: MyApplication, val appRepository: AppRepository) : AndroidViewModel(application) {
    var responseData = MutableLiveData<SignUpResponseData>()
    fun signUp(feedBackData: RegisterData) {
        appRepository.signUp(feedBackData,responseData)
    }
}