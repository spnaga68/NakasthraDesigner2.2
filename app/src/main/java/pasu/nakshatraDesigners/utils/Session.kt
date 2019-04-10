package pasu.nakshatraDesigners.utils

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import pasu.nakshatraDesigners.data.SocialMediaData
import com.google.gson.reflect.TypeToken
import pasu.nakshatraDesigners.data.NavData
import pasu.nakshatraDesigners.data.SplashResponseData


/**
 * This common class to store the require Detaildata by using SharedPreferences.
 */
object Session {
    fun save(key: String?, value: String?, context: Context?) {
        if (context != null && value != null && key != null) {
            val editor = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE).edit()

            editor.putString(key, value)
            editor.apply()
        }
        return
    }

    fun save(key: String?, value: Boolean, context: Context?) {
        if (context != null && key != null) {
            val editor = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE).edit()

            editor.putBoolean(key, value)
            editor.apply()
        }
//        return
    }

    fun getSession(key: String?, context: Context?): String {
        if (context != null && key != null) {
            val prefs = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE)

            return prefs.getString(key, "") ?: ""
        }
        return ""
    }

    fun getSession(key: String, context: Context?, defaultValue: Boolean): Boolean {
        context?.run {
            val prefs = getSharedPreferences("KEY", Activity.MODE_PRIVATE)
            return prefs.getBoolean(key, defaultValue)
        }
        return defaultValue
    }

    fun clearAllSession(context: Context) {
        val editor = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }

    fun getUserID(context: Context): String {
        return Session.getSession(USER_ID, context)
    }

    fun setAccessKey(accesskey: String, context: Context) {
        Session.save("ses_accessKey", accesskey, context)

    }

    fun getAccessKey(context: Context): String {
        return Session.getSession("ses_accessKey", context)

    }

    val socialLink = "ses_sociallink"
    fun saveSocialLink(context: Context, data: ArrayList<SocialMediaData>) {
        Session.save(socialLink, Gson().toJson(data), context)
    }

    fun getSocialLink(context: Context): ArrayList<SocialMediaData> {
        return Gson().fromJson(
            Session.getSession(socialLink, context),
            object : TypeToken<ArrayList<SocialMediaData>>() {
            }.type
        )
    }

    val NAVDATA = "SES_NavData"

    fun saveNavData(context: Context, data: ArrayList<NavData>) {
        Session.save(NAVDATA, Gson().toJson(data), context)
    }

    fun getNavData(context: Context): ArrayList<NavData> {
        return Gson().fromJson(
            Session.getSession(NAVDATA, context),
            object : TypeToken<ArrayList<NavData>>() {
            }.type
        )
    }

    val SplashDATA = "SES_SplashData"
    fun saveSplashDATA(context: Context, data: SplashResponseData.SplashData) {
        Session.save(SplashDATA, Gson().toJson(data), context)
    }

    fun getSplashDATA(context: Context): SplashResponseData.SplashData {
        return Gson().fromJson(
            Session.getSession(SplashDATA, context),
            SplashResponseData.SplashData::class.java
        )
    }
}
