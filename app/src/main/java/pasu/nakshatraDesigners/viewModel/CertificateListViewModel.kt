package pasu.nakshatraDesigners.viewModel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import pasu.nakshatraDesigners.MyApplication
import pasu.nakshatraDesigners.PagedDataSource.DataSourceFactory
import pasu.nakshatraDesigners.data.VideoListItem
import pasu.nakshatraDesigners.utils.NetworkState

class CertificateListViewModel(val application: MyApplication,val itemDataSourceFactory: DataSourceFactory):AndroidViewModel(application) {


    var itemPagedList: LiveData<PagedList<VideoListItem>>? = null
    var networkState: LiveData<NetworkState>? = null


    fun getSearchedData() {
        //Getting PagedList config
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(10).setInitialLoadSizeHint(1).build()
        //Getting live Detaildata from datasource
        networkState = Transformations.switchMap(itemDataSourceFactory.itemLiveDataSource) {
            it.networkState
        }

        itemPagedList = LivePagedListBuilder(itemDataSourceFactory, pagedListConfig)
            .build()
    }
}
