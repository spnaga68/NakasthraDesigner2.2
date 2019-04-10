package pasu.nakshatraDesigners.signIn

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.include_header.*
import pasu.nakshatraDesigners.MainActivity
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.signIn.fragments.CustomBottomSheetDialogFragment
import pasu.nakshatraDesigners.signIn.repository.CommonViewModelFactory
import pasu.nakshatraDesigners.signIn.viewModel.SignInViewModel
import pasu.nakshatraDesigners.utils.*
import nakshatraDesigners.utils.CommonFunctions
import android.view.inputmethod.EditorInfo
import android.view.KeyEvent
import android.widget.TextView


class SignInActivity : AppCompatActivity(), DialogOnClickInterface {
    override fun onPositiveButtonCLick(dialog: DialogInterface, alertType: Int) {
        dialog.dismiss()
    }

    override fun onNegativeButtonCLick(dialog: DialogInterface, alertType: Int) {
        dialog.dismiss()
    }

    private lateinit var viewModel: SignInViewModel
    private lateinit var binding: pasu.nakshatraDesigners.databinding.ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, CommonViewModelFactory(this)).get(SignInViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, pasu.nakshatraDesigners.R.layout.activity_signin)
        binding.apply {
            signInViewModel = viewModel
            lifecycleOwner = this@SignInActivity
            executePendingBindings()
        }
        forgotpswdTxt.setOnClickListener {
            val bottomSheetDialog = CustomBottomSheetDialogFragment.getInstance(true)
            bottomSheetDialog.show(supportFragmentManager, "CustomBottomSheetDialog")
        }
        signInButton.setOnClickListener {
            callSignIn()
        }



        setToolBar()



        viewModel.data.observe(this, Observer {
            print("ResponseData $it")
            layoutLoading.visibility = View.GONE
            it?.let { data ->
                if (data.responseCode == 400) {
                    Session.save(USER_ID, "" + data.data.userid, this@SignInActivity)
                    Session.save(USER_EMAIL, data.data.email, this@SignInActivity)
                    Session.save(USER_NAME, data.data.name, this@SignInActivity)
                    Session.save(USER_PHONE_NO, data.data.phone, this@SignInActivity)
                    Session.save(USER_IMG, data.data.imageUrl, this@SignInActivity)
                    Session.setAccessKey(data.data.accesskeyval, this@SignInActivity)
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                    finish()
//                    CommonFunctions.alertDialog(
//                        this@SignInActivity,
//                        this@SignInActivity,
//                        Detaildata.Message,
//                        getString(R.string.ok),
//                        "",
//                        true
//                    ).show()
                }else{
                    CommonFunctions.alertDialog(
                        this@SignInActivity,
                        this@SignInActivity,
                        data.Message,
                        getString(R.string.ok),
                        "",
                        true
                    ).show()
                }
            }

            if (it == null) {
                CommonFunctions.alertDialog(
                    this@SignInActivity,
                    this@SignInActivity,
                    getString(R.string.server_error),
                    getString(R.string.ok),
                    "",
                    true
                ).show()
            }
        })



        et_password.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do here your stuff f
                    callSignIn()
                    return true;
                }
                return false;
            }


        })

    }

    private fun callSignIn() {
        if (CommonFunctions.isValidPhone(binding.phoneEdt, binding.phoneEdtLay, this)) {
            if (CommonFunctions.isValidPwd(binding.etPassword, binding.pwdLay, this)) {
                layoutLoading.visibility = View.VISIBLE
                viewModel.callSignIn(phoneEdt.text.toString(), "+91", et_password.text.toString())
            }
        }
    }

    private fun setToolBar() {
        toolbarImage.visibility = View.GONE
        titleText.text = getString(R.string.signin)
        titleText.visibility = View.VISIBLE
    }
}