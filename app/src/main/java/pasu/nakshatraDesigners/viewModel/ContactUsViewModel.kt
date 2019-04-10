package pasu.nakshatraDesigners.viewModel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pasu.nakshatraDesigners.MyApplication
import pasu.nakshatraDesigners.data.FeedBackData
import pasu.nakshatraDesigners.data.SimpleResponseData
import pasu.nakshatraDesigners.data.SplashResponseData
import pasu.nakshatraDesigners.repository.AppRepository


class ContactUsViewModel(application: MyApplication, val appRepository: AppRepository) : AndroidViewModel(application) {
    var responseData = MutableLiveData<SimpleResponseData>()
    fun sendFeedBack(feedBackData:FeedBackData) {
        appRepository.feedBackData(feedBackData,responseData)
    }
}