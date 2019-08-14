package nakshatraDesigners.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.google.android.material.textfield.TextInputLayout
import pasu.nakshatraDesigners.utils.DialogOnClickInterface
import pasu.nakshatraDesigners.MainActivity
import pasu.nakshatraDesigners.signIn.SignInActivity
import pasu.nakshatraDesigners.utils.*
import android.graphics.Bitmap
import pasu.nakshatraDesigners.R
import android.provider.MediaStore.Images.Media.getBitmap
import android.graphics.drawable.BitmapDrawable
import android.text.Html
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import android.net.Uri
import java.net.URLEncoder


object CommonFunctions {
    private var fromChosser: Boolean = false
    private var redirectToCart: Boolean = false


    fun clearLoginSession(context: Context) {
        Session.save(USER_EMAIL, "", context)
        Session.save(USER_ID, "", context)
        Session.save(USER_NAME, "", context)
        Session.save(USER_PHONE_NO, "", context)
        Session.save(FACEBOOK_ID, "", context)
        Session.save(IS_PHONE_VERIFIED, false, context)
    }


    fun smallMarker(context: Context): Bitmap {

        val height = 120
        val width = 120
        val bitmapdraw =
            context.getResources().getDrawable(pasu.nakshatraDesigners.R.drawable.truckicon) as BitmapDrawable
        val b = bitmapdraw.bitmap
        return Bitmap.createScaledBitmap(b, width, height, false)

    }


    fun getDeviceID(context: Context): String {
        return Settings.Secure.getString(
            context.getContentResolver(),
            Settings.Secure.ANDROID_ID
        );
    }


    private fun checkIdAndRedirect(context: Context, mBundle: Bundle) {
        if (Session.getSession(USER_PHONE_NO, context).isEmpty() || !fromChosser)
            checkPhnoAndRedirect(context, mBundle)
        context.startActivity(Intent(context, MainActivity::class.java))

    }

    fun checkUserIdAndRedirect(context: Context) {
        fromChosser = true
        if (Session.getSession(USER_ID, context).isEmpty()) {
            context.startActivity(Intent(context, SignInActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            })
        } else {
            if (Session.getSession(USER_PHONE_NO, context).isEmpty() || fromChosser)
                checkPhnoAndRedirect(context, Bundle().apply {
                    putBoolean(REDIRECT_TO_CART, false)
                })
            else
                context.startActivity(Intent(context, MainActivity::class.java))
        }
        /*
            context.startActivity(Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            })*/
    }

    private fun checkPhnoAndRedirect(context: Context, mBundle: Bundle) {
        if (Session.getSession(
                USER_PHONE_NO,
                context
            ).isEmpty() /*|| (Session.getSession(USER_PHONE_NO, context).isNotEmpty() && !Session.getSession(IS_PHONE_VERIFIED, context, false))*/) {

            context.startActivity(Intent(context, SignInActivity::class.java).apply {
                putExtras(mBundle.apply {
                    putBoolean(IS_FACEBOOK_LOGIN, fromChosser)
                    putBoolean(SHOW_PASSWORD_PAGE, false)
                })
            }, ActivityOptionsCompat.makeSceneTransitionAnimation(context as AppCompatActivity).toBundle())
        } else {
            context.startActivity(Intent(context, MainActivity::class.java))

        }


    }

    fun fromHtml(html: String?): Spanned {
        return if (html == null) {
            // return an empty spannable if the html is null
            SpannableString("")
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
            // we are using this flag to give a consistent behaviour
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
    }

    fun alertDialog(
        context: Context,
        dialogOnClickInterface: DialogOnClickInterface,
        alertMessage: String,
        positiveButtonText: String = context.resources.getString(R.string.ok),
        negativeButtonText: String = context.resources.getString(R.string.cancel),
        isCancelable: Boolean,
        alertType: Int = 0,
        alertTitle: String = ""
    ): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setMessage(alertMessage)
            setCancelable(isCancelable)
            if (positiveButtonText.isEmpty()) {
                setPositiveButton(R.string.ok) { dialog, id ->
                    dialogOnClickInterface.onPositiveButtonCLick(dialog, alertType)
                }
            } else {
                setPositiveButton(positiveButtonText) { dialog, id ->
                    dialogOnClickInterface.onPositiveButtonCLick(dialog, alertType)
                }
            }
            if (!negativeButtonText.isEmpty()) {
                setNegativeButton(negativeButtonText) { dialog, id ->
                    dialogOnClickInterface.onNegativeButtonCLick(dialog, alertType)
                }
            }
        }
        val alertDialog = builder.create()
        alertDialog.show()
        return alertDialog
    }


    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    fun isValidEmail(editView: EditText, textInputLayout: TextInputLayout, context: Context): Boolean {
        if (!isValidEmail(editView.text.toString())) {
            textInputLayout.error =context.getString(R.string.email_validataion)
            editView.requestFocus()
            return false
        } else {
            textInputLayout.error = null
            return true
        }
    }

    fun isEmpty(editView: EditText, textInputLayout: TextInputLayout, context: Context,errorMessage:String): Boolean {
        if (TextUtils.isEmpty(editView.text.toString().trim())) {
            textInputLayout.error =errorMessage
            editView.requestFocus()
            return false
        } else {
            textInputLayout.error = null
            return true
        }
    }


    fun isValidPhone(editView: EditText, textInputLayout: TextInputLayout, context: Context): Boolean {
        if (TextUtils.isEmpty(editView.text.toString().trim())) {
            textInputLayout.error = context.getString(R.string.error_please_enter_phone_number)
            editView.requestFocus()
            return false
        } else if (editView.text.toString().trim().length >= context.resources.getInteger(R.integer.phMinNoLength)) {
            textInputLayout.error = null
            return true
        } else {
            textInputLayout.error = context.getString(R.string.error_valid_length_phone_number)
            editView.requestFocus()
            return false
        }
    }

    fun isValidPwd(editView: EditText, textInputLayout: TextInputLayout, context: Context): Boolean {
        if (TextUtils.isEmpty(editView.text.toString().trim())) {
            textInputLayout.error = context.getString(R.string.error_valid_password)
            editView.requestFocus()
            return false
        } else if (editView.text.toString().trim().length >= context.resources.getInteger(R.integer.pwdMinLength)) {
            textInputLayout.error = null
            return true
        } else {
            textInputLayout.error = context.getString(R.string.error_valid_length_password)
            editView.requestFocus()
            return false
        }
    }

    private fun openWhatsApp(context: Context, phone: String) {
        var smsNumber = "91$phone"; // E164 format without '+' sign
//    var sendIntent =  Intent(Intent.ACTION_SEND);
//    sendIntent.setType("text/plain");
//    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send. $smsNumber");
//    sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
//    sendIntent.setPackage("com.whatsapp");


        var packageManager = context.getPackageManager();
        var i = Intent(Intent.ACTION_VIEW);

        try {
            var url = "https://api.whatsapp.com/send?phone=" + smsNumber + "&text=" + URLEncoder.encode(
                "Hi Mam, I Want Aari Embroidery Online Classes Details. Can You Share Me..?",
                "UTF-8"
            );
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                context.startActivity(i);
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }

//    if (sendIntent.resolveActivity(context.getPackageManager()) == null) {
//        Toast.makeText(context, "Error/n" , Toast.LENGTH_SHORT).show();
//        return;
//    }
//    context.startActivity(sendIntent);
    }

    fun onClickWhatsApp(context: Context, number: String) {

        var isWhatsappInstalled = whatsappInstalledOrNot(context, "com.whatsapp");
        if (isWhatsappInstalled) {
            openWhatsApp(context, number)
//            var uri = Uri.parse("smsto:$number")
//            var sendIntent = Intent();
//
//
//
//            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//            sendIntent.setType("text/plain");
//            context.startActivity(sendIntent);
//            sendIntent.setPackage("com.whatsapp");
//            context.startActivity(sendIntent);
        } else {
            Toast.makeText(
                context, "WhatsApp not Installed",
                Toast.LENGTH_SHORT
            ).show();
            var uri = Uri.parse("market://details?id=com.whatsapp");
            var goToMarket = Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(goToMarket);

        }

    }

    fun whatsappInstalledOrNot(context: Context, uri: String): Boolean {
        var pm = context.getPackageManager();
        var app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (e: PackageManager.NameNotFoundException) {
            app_installed = false;
        }
        return app_installed;
    }

    fun callClicked(context: Context, phone: String) {
        var intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        context.startActivity(intent);
    }

//    val address: String = "",
//    val city: String = "",
//    val state: String = "",
//    val zip: String = "",
//    val password: String = ""


}