package pasu.nakshatraDesigners

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.splashlay.*
import pasu.nakshatraDesigners.signIn.SignInActivity
import pasu.nakshatraDesigners.utils.*
import pasu.nakshatraDesigners.viewModel.SplashViewModel
import pasu.nakshatraDesigners.viewModel.ViewmodelFactory
import nakshatraDesigners.utils.CommonFunctions
import pasu.nakshatraDesigners.data.NavData

class SplashActivity : AppCompatActivity(), DialogOnClickInterface {
    override fun onPositiveButtonCLick(dialog: DialogInterface, alertType: Int) {
        layoutLoading.visibility = View.VISIBLE
        splashViewModel.callGetCore()
        dialog.dismiss()
    }

    override fun onNegativeButtonCLick(dialog: DialogInterface, alertType: Int) {
        finish()
        dialog.dismiss()
    }


    lateinit var splashViewModel: SplashViewModel
    val requireGetCore = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashlay)
        if (requireGetCore) {
            val factory = ViewmodelFactory(this@SplashActivity)
            splashViewModel = ViewModelProviders.of(this, factory).get(SplashViewModel::class.java)
            splashViewModel.callGetCore()
            splashViewModel.coreData.observe(this, Observer { response ->
                response?.let {
                    var data = response.data.Detaildata
                    if (response.responseCode == 400) {
                        data?.let {
                            layoutLoading.visibility = View.GONE
                            Session.save(APP_NAME, data.appName, this@SplashActivity)
                            Session.save(APP_LOGO, data.appLogo, this@SplashActivity)
                            Session.save(LOGIN_LOGO, data.loginLogo, this@SplashActivity)
                            Session.save(LANG_FILE, data.langFile, this@SplashActivity)
                            Session.save(COUNTRY_CODE, data.countryCode, this@SplashActivity)
                            Session.save(ANDROID_KEY, data.androidKey, this@SplashActivity)
                            Session.save(TERMS, data.terms, this@SplashActivity)
                            Session.save(ABOUT_US, data.aboutUs, this@SplashActivity)
                            Session.save(NO_IMAGE_URL, data.noImageUrl, this@SplashActivity)
                            Session.save(ERROR_REPORT_CASE, data.errorReportCase, this@SplashActivity)
                            Session.save(SOCKET_URL, data.socketUrl, this@SplashActivity)

                        }
                        Session.saveSplashDATA(this@SplashActivity,response.data)
                        Session.saveSocialLink(this@SplashActivity, response.data.socialmedialink)
                        var link=response.data.webpageurl
                        link.add(0, NavData("Dashboard",-6,""))
                        Session.saveNavData(this@SplashActivity,link)
//                        if (Session.getSession(USER_ID, this@SplashActivity).equals(""))
//                            startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
//                        else
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))

                        finish()

                    } else {
                        CommonFunctions.alertDialog(
                            this@SplashActivity,
                            this,
                            response.Message,
                            getString(R.string.retry),
                            getString(R.string.cancel),
                            false
                        ).show()


                    }
                }
                if (response == null) {

                    CommonFunctions.alertDialog(
                        this@SplashActivity,
                        this,
                        getString(R.string.server_error),
                        getString(R.string.retry),
                        getString(R.string.cancel),
                        false
                    ).show()
                }
            })

        } else {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}