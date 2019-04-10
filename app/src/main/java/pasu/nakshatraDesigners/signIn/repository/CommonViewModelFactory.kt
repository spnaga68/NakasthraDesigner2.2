package pasu.nakshatraDesigners.signIn.repository

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pasu.nakshatraDesigners.MyApplication
import pasu.nakshatraDesigners.repository.AppRepository
import pasu.nakshatraDesigners.signIn.PasswordActivity
import pasu.nakshatraDesigners.signIn.PhoneVerificationActivity
import pasu.nakshatraDesigners.signIn.viewModel.MobileRegViewModel
import pasu.nakshatraDesigners.signIn.viewModel.PasswordViewModel
import pasu.nakshatraDesigners.signIn.viewModel.PhoneVerificationViewModel
import pasu.nakshatraDesigners.signIn.viewModel.SignInViewModel
import pasu.nakshatraDesigners.viewModel.DashboardViewModel

class CommonViewModelFactory(val context: Context) : ViewModelProvider.Factory {

    private fun getFilterItemsRepository(context: Context): SignUpRepository {
        return SignUpRepository.getInstance(context)
    }


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.simpleName=="PasswordViewModel")
            return PasswordViewModel(getFilterItemsRepository(context), context.applicationContext as MyApplication) as T
        else if (modelClass.simpleName=="SignInViewModel")
            return SignInViewModel(getFilterItemsRepository(context), context.applicationContext as MyApplication) as T
        else if (modelClass.simpleName=="DashboardViewModel")
            return DashboardViewModel(AppRepository.getInstance(context), context.applicationContext as MyApplication) as T

        return when (context) {
            is PasswordActivity -> PasswordViewModel(getFilterItemsRepository(context), context.applicationContext as MyApplication) as T
            is PhoneVerificationActivity -> PhoneVerificationViewModel(getFilterItemsRepository(context), context.applicationContext as MyApplication) as T
           // is SignInActivity -> PasswordViewModel(getFilterItemsRepository(context), context.applicationContext as MyApplication) as T
            else -> MobileRegViewModel(getFilterItemsRepository(context), context.applicationContext as MyApplication) as T
        }
    }
}