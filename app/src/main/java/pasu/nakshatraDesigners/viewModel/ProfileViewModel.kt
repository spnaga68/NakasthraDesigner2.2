package pasu.nakshatraDesigners.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pasu.nakshatraDesigners.data.LogoutRequestData
import pasu.nakshatraDesigners.data.LogoutResponse
import pasu.nakshatraDesigners.data.ProfileRequestData
import pasu.nakshatraDesigners.data.ProfileResponseData
import pasu.nakshatraDesigners.repository.AppRepository
import pasu.nakshatraDesigners.signIn.data.SignUpDetail

class ProfileViewModel(application: Application, val appRepository: AppRepository) : AndroidViewModel(application)  {
    val profileResponseData = MutableLiveData<ProfileResponseData>()
    val updateprofileResponseData = MutableLiveData<ProfileResponseData>()
    val showLoading = MutableLiveData<Boolean>()
    val logoutResponse = MutableLiveData<LogoutResponse>()
    init {
        showLoading.value = true
    }

    fun getProfileData(profileRequestData: ProfileRequestData) {
        appRepository.getProfileData(profileRequestData, profileResponseData)
    }

    fun editProfileData(editProfileData: SignUpDetail) {
        appRepository.editProfile(editProfileData, updateprofileResponseData)
    }
    fun callLogout(logoutRequestData: LogoutRequestData) {
        appRepository.logout(logoutRequestData, logoutResponse)
    }


}