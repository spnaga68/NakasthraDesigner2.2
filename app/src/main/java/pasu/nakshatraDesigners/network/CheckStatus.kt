package pasu.nakshatraDesigners.network

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import org.json.JSONObject

//Nodejs Log Code
//408				=>	Header not contains domains					(Need Alert)
//408				=>  DB Not connected,Site Config not retrived 	(Need Alert)
//408				=>  Timezone issue								(Need Alert)
//408				=>  Invalid Auth 								(Need Alert)
//408				=>  MongoDB down.
//408				=>  TryCatch Error								(Need Alert)
//
//Return Detaildata
//811				=>  Driver Get Current Information
//
//Message without action service stop only
//
//409				=>  Force to Update Build
//412				=>  Request to uninstall current build.
//
//Action with Message move to login screen
//
//
//410				=>  Driver App Destory like as new build
//411				=>  Driver Logout
//
//Only action
//601 =>  Web Domain Url Change Request
//602 =>  Node Url Change Request
//603 =>  Token Expired

class CheckStatus(val json: JSONObject, val context: Context) {

    fun isNormal(): Boolean {
        var normal = true

//        if (json.has("token"))
//            SessionSave.saveSession(CommonData.NODE_TOKEN, json.getString("token"), context)
        if (json.has("Message")) {
            var statusCode = json.getInt("responseCode");
            var message = json.getString("Message");


            when (statusCode) {
                408 -> {
                    normal = false
//                Handler(Looper.getMainLooper()).post({ CToast.ShowToast(context, Message); })

                }
                601 -> {
                    normal = false
//                SessionSave.saveSession("base_url", Message, context)
//                CheckUrl().update(context, json.getString("domain"), Message, "base_url")
                }
                602 -> {
                    normal = false
//                SessionSave.saveSession(CommonData.NODE_URL, Message, context)
//                CheckUrl().update(context, json.getString("domain"), Message, CommonData.NODE_URL)

                }
                603 -> {
                    normal = false
//                NodeAuth().getAuth(context)
                }

                409 -> {
                    normal = false
                    val cancelIntent = Intent()
                    val bun = Bundle()
                    bun.putString("Message", message)
                    bun.putBoolean("move_to_playstore", true)
                    cancelIntent.putExtras(bun)
                    cancelIntent.action = Intent.ACTION_MAIN
                    cancelIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                    cancelIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                val cn = ComponentName(context, ShowAlertAct::class.java)
//                cancelIntent.component = cn
//                context.startActivity(cancelIntent)
                }

                410 -> {
                    normal = false
//                SessionSave.clearAllSession(context)
                    forceLogout("")
                    val cancelIntent = Intent()
                    val bun = Bundle()
                    bun.putString("Message", message)
                    cancelIntent.putExtras(bun)
                    cancelIntent.action = Intent.ACTION_MAIN
                    cancelIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                    cancelIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                val cn = ComponentName(context, SplashAct::class.java)
//                cancelIntent.component = cn
                    context.startActivity(cancelIntent)
                }

                412 -> {
                    normal = false
                    val cancelIntent = Intent()
                    val bun = Bundle()
                    bun.putBoolean("move_to_playstore", false)
                    bun.putString("Message", message)
                    cancelIntent.putExtras(bun)
                    cancelIntent.action = Intent.ACTION_MAIN
                    cancelIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                    cancelIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                val cn = ComponentName(context, ShowAlertAct::class.java)
//                cancelIntent.component = cn
//                context.stopService(Intent(context, LocationUpdate::class.java))
                    context.startActivity(cancelIntent)
                }

                411 -> {
                    normal = false
//                MainActivity.clearsession(context)
                    val cancelIntent = Intent()
                    val bun = Bundle()
                    bun.putString("Message", message)
                    bun.putString("alert_message", message)
                    cancelIntent.putExtras(bun)
                    cancelIntent.action = Intent.ACTION_MAIN
                    cancelIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                    cancelIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                val cn = ComponentName(context, UserLoginAct::class.java)
//                cancelIntent.component = cn
                    context.startActivity(cancelIntent)
                }

                811 -> {
                    normal = false
//                SendDriverDeviceInfo().sendInfo(context, "-1")
                }

            }
        }

        return normal
    }


    /**
     * Method to logout user if responseCode -101 and redirect to login page
     * @param message - To intimate user by showing alert Message
     */
    private fun forceLogout(message: String) {
//        CToast.ShowToast(context, Message)
    }
}