package pasu.nakshatraDesigners.viewModel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pasu.nakshatraDesigners.MyApplication
import pasu.nakshatraDesigners.data.CoreData
import pasu.nakshatraDesigners.data.SplashResponseData
import pasu.nakshatraDesigners.repository.AppRepository

class SplashViewModel(application: MyApplication, val appRepository: AppRepository) : AndroidViewModel(application) {
    var coreData = MutableLiveData<SplashResponseData>()
    fun callGetCore() {
        appRepository.getCoreData(coreData)
    }
}