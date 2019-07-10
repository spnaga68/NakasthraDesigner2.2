package pasu.nakshatraDesigners

import androidx.multidex.MultiDexApplication
import com.google.android.gms.ads.MobileAds


//{http://52.66.30.50/assets/admin/base/images/category/14.jpg?1550319358

class MyApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
//        Fabric.with(this, Crashlytics())
        MobileAds.initialize(this, getString(R.string.ad_mob_id))
    }


}