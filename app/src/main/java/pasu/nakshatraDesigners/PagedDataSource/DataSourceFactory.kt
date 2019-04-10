package pasu.nakshatraDesigners.PagedDataSource

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import pasu.nakshatraDesigners.data.VideoListItem


class DataSourceFactory(val context: Context) : DataSource.Factory<String, VideoListItem>() {


    //getter for itemlivedatasource
    var itemLiveDataSource = MutableLiveData<CertificateDataSource>()
    internal lateinit var itemDataSource: CertificateDataSource

    override fun create(): DataSource<String, VideoListItem> {
        //getting our Detaildata source object
        itemDataSource = CertificateDataSource(context)

        //posting the datasource to get the values
        itemLiveDataSource.postValue(itemDataSource);
        //returning the datasource
        return itemDataSource
    }


}