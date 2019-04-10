package pasu.nakshatraDesigners.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pasu.nakshatraDesigners.data.DashboardRequest
import pasu.nakshatraDesigners.data.DashboardResponse
import pasu.nakshatraDesigners.data.DashboardResponseData
import pasu.nakshatraDesigners.repository.AppRepository

class DashboardViewModel( val appRepository: AppRepository,application: Application) : AndroidViewModel(application) {
    val dashboardResponse = MutableLiveData<DashboardResponseData>()
    val showLoading = MutableLiveData<Boolean>()

    init {
        showLoading.value = true
    }

    fun getDashboardData() {
        appRepository.dashboardData( dashboardResponse)
    }

}