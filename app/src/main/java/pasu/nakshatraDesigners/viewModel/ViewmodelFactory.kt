package pasu.nakshatraDesigners.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pasu.nakshatraDesigners.MyApplication
import pasu.nakshatraDesigners.PagedDataSource.DataSourceFactory
import pasu.nakshatraDesigners.repository.AppRepository

class ViewmodelFactory(val context: Context,val courseUrl:String = "") : ViewModelProvider.Factory {


    private fun getAppRepository(context: Context): AppRepository {
        return AppRepository.getInstance(context)
    }

    private fun getDataSourceFactory(context: Context,courseUrl:String): DataSourceFactory {
        return DataSourceFactory(context,courseUrl)
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when (modelClass.simpleName) {
            "DashboardViewModel" ->
                return DashboardViewModel(getAppRepository(context), context.applicationContext as MyApplication) as T
            "CertificateListViewModel" ->
                return CertificateListViewModel(
                    context.applicationContext as MyApplication,
                    getDataSourceFactory(context,courseUrl)
                ) as T
            "ProfileViewModel" ->
                return ProfileViewModel(context.applicationContext as MyApplication, getAppRepository(context)) as T
            "SplashViewModel" ->
                return SplashViewModel(context.applicationContext as MyApplication, getAppRepository(context)) as T
            "ContactUsViewModel" ->
                return ContactUsViewModel(context.applicationContext as MyApplication, getAppRepository(context)) as T
            "RegisterViewModel" ->
                return RegisterViewModel(context.applicationContext as MyApplication, getAppRepository(context)) as T
        }
        return DashboardViewModel(getAppRepository(context), context.applicationContext as MyApplication) as T

    }
}