package pasu.nakshatraDesigners.signIn.smsVerifier

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import pasu.nakshatraDesigners.R


class SmsVerifier {
    private val PERMISSION_REQUEST_CODE = 12

    private val activity: Activity
    private lateinit var fragment: Fragment
    private val onSmsCatchListener: OnSmsCatchListener<String>
    private var smsReceiver: SmsReceiver? = null
    private var phoneNumber: String? = null
    private var filter: String? = null

    private var dialog: Dialog? = null

    constructor(activity: Activity, onSmsCatchListener: OnSmsCatchListener<String>) {
        this.activity = activity
        this.onSmsCatchListener = onSmsCatchListener
        smsReceiver = SmsReceiver()
        smsReceiver!!.setCallback(this.onSmsCatchListener)
    }

    constructor(activity: Activity, fragment: Fragment, onSmsCatchListener: OnSmsCatchListener<String>) : this(activity, onSmsCatchListener) {
        this.fragment = fragment
    }

    fun onStart() {
        if (isSMSPermissionGranted(activity)) {
            registerReceiver()
        }
    }

    private fun registerReceiver() {
        /* smsReceiver = SmsReceiver()
         smsReceiver!!.setCallback(onSmsCatchListener)
         smsReceiver!!.setPhoneNumberFilter(email!!)
         smsReceiver!!.setFilter(filter!!)*/
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED")
        activity.registerReceiver(smsReceiver, intentFilter)
    }

    fun setPhoneNumberFilter(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }

    fun onStop() {
        try {
            activity.unregisterReceiver(smsReceiver)
        } catch (ignore: IllegalArgumentException) {
            ignore.printStackTrace()
        }

    }

    fun setFilter(regexp: String) {
        this.filter = regexp
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                registerReceiver()
            }
        }
    }


    //For fragments
    fun isSMSPermissionGranted(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                requestPermission(activity)
                return false
            }
        } else {
            return true
        }
    }

    private fun requestPermission(activity: Activity) {
        val alertBuilder = AlertDialog.Builder(activity);
        alertBuilder.setCancelable(true);
        alertBuilder.setMessage(activity.getString(R.string.str_sms))
        alertBuilder.setPositiveButton(android.R.string.yes) { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
            ActivityCompat.requestPermissions(activity,
                    arrayOf(
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.READ_SMS
                    ), PERMISSION_REQUEST_CODE)
        }
        alertBuilder.setNegativeButton(android.R.string.no) { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
            activity.finish()
        }
        val alert = alertBuilder.create()

        alert.setOnShowListener {
            @Override
            fun onShow(arg0: DialogInterface) {
                if (alert != null) {
                    alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(activity.getResources().getColor(R.color.colorAccent));
                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.black));
                }
            }
        };
        alert.show()
    }
}