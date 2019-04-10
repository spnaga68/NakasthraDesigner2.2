package pasu.nakshatraDesigners.signIn.smsVerifier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import pasu.nakshatraDesigners.signIn.smsVerifier.OnSmsCatchListener


const val INTENT_ACTION = "com.google.android.gms.auth.api.email.SMS_RETRIEVED"

class SmsReceiver : BroadcastReceiver() {
    private val TAG = SmsReceiver::class.java.simpleName
    private var callback: OnSmsCatchListener<String>? = null

    /**
     * Set result callback
     * @param callback OnSmsCatchListener
     */
    fun setCallback(callback: OnSmsCatchListener<String>) {
        this.callback = callback
    }

    override fun onReceive(context: Context, intent: Intent) {
        println("SmsReceiver onReceive ${intent.action}")
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            intent.extras?.apply {
                val status = get(SmsRetriever.EXTRA_STATUS) as Status
                when (status.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        println("SmsReceiver CommonStatusCodes ${intent.action}")
                        callback?.onSmsCatch(get(SmsRetriever.EXTRA_SMS_MESSAGE) as String)
                    }
                    CommonStatusCodes.TIMEOUT -> {
                        Log.v(TAG, "CommonStatusCodes.TIMEOUT ${CommonStatusCodes.TIMEOUT}")
                    }
                }
            }
        }
    }
}