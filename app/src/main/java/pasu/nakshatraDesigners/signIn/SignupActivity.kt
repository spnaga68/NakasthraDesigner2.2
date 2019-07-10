package pasu.nakshatraDesigners.signIn

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.include_header.*
import nakshatraDesigners.utils.CommonFunctions
import pasu.nakshatraDesigners.MainActivity
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.data.RegisterData
import pasu.nakshatraDesigners.utils.*
import pasu.nakshatraDesigners.viewModel.RegisterViewModel
import pasu.nakshatraDesigners.viewModel.ViewmodelFactory


class SignupActivity : AppCompatActivity(), DialogOnClickInterface {


    override fun onPositiveButtonCLick(dialog: DialogInterface, alertType: Int) {
        binding.phoneEdt.setText("")
        binding.emailEdt.setText("")
        binding.nameEdt.setText("")

        dialog.dismiss()
        if (alertType == 400)
            CommonFunctions.checkUserIdAndRedirect(this@SignupActivity)
    }

    override fun onNegativeButtonCLick(dialog: DialogInterface, alertType: Int) {
        binding.phoneEdt.setText("")
        binding.emailEdt.setText("")
        binding.nameEdt.setText("")
        dialog.dismiss()
        if (alertType == 400)
            CommonFunctions.checkUserIdAndRedirect(this@SignupActivity)
    }

    private lateinit var binding: pasu.nakshatraDesigners.databinding.RegisterLayBinding
    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, pasu.nakshatraDesigners.R.layout.register_lay)
        registerViewModel =
            ViewModelProviders.of(this, ViewmodelFactory(this@SignupActivity)).get(RegisterViewModel::class.java)




        titleText.visibility = View.VISIBLE
        toolbarImage.visibility = View.GONE
        titleText.text = getString(R.string.register)
        tool_back.visibility = View.VISIBLE
        tool_back.setOnClickListener { finish() }



        feedBackRes()

        binding.submit.setOnClickListener {
            if (isFeedBackValid()) {
                binding.layoutLoading.visibility = View.VISIBLE
                binding.submit.visibility = View.GONE
                registerViewModel.signUp(
                    RegisterData(
                        binding.nameEdt.text.toString(),

                        binding.emailEdt.text.toString(),
                        binding.phoneEdt.text.toString(),
                        Session.getSession(DEVICE_ID, this@SignupActivity), binding.pwdEdt.text.toString()
                    )
                )
            }
        }
    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        binding = DataBindingUtil.inflate(inflater, R.layout.register_lay, container, false)
//        registerViewModel =
//            ViewModelProviders.of(this, ViewmodelFactory(this@SignupActivity!!)).get(RegisterViewModel::class.java)
//
//
//        feedBackRes()
//
//        binding.submit.setOnClickListener {
//            if (isFeedBackValid()) {
//                binding.layoutLoading.visibility = View.VISIBLE
//                binding.submit.visibility = View.GONE
//                registerViewModel.signUp(
//                    RegisterData(
//                        binding.nameEdt.text.toString(),
//
//                        binding.emailEdt.text.toString(),
//                        binding.phoneEdt.text.toString(),
//                        Session.getSession(DEVICE_ID, this@SignupActivity), binding.pwdEdt.text.toString()
//                    )
//                )
//            }
//        }
//        return binding.root
//
//    }


    fun feedBackRes() {
        registerViewModel.responseData.observe(this, Observer { response ->

            binding.layoutLoading.visibility = View.GONE
            binding.submit.visibility = View.VISIBLE

            response?.let {data->
                if (data.responseCode == 400) {
                    Session.save(USER_ID, "" + data.data.userid, this@SignupActivity)
                    Session.save(USER_EMAIL, data.data.email, this@SignupActivity)
                    Session.save(USER_NAME, data.data.name, this@SignupActivity)
                    Session.save(USER_PHONE_NO, data.data.phone, this@SignupActivity)
                    Session.save(USER_IMG, data.data.imageUrl, this@SignupActivity)
                    Session.setAccessKey(data.data.accesskeyval, this@SignupActivity)
//                    startActivity(Intent(this@SignupActivity, MainActivity::class.java))
//                    CommonFunctions.alertDialog(
//                        this@SignInActivity,
//                        this@SignInActivity,
//                        Detaildata.Message,
//                        getString(R.string.ok),
//                        "",
//                        true
//                    ).show()
                }
                CommonFunctions.alertDialog(
                    this@SignupActivity!!,
                    this,
                    response.Message,
                    getString(R.string.ok),
                    "",
                    false, response.responseCode, ""
                )
            }


            if (response == null) {

                CommonFunctions.alertDialog(
                    this@SignupActivity!!,
                    this,
                    getString(R.string.server_error),
                    getString(R.string.ok),
                    "",
                    false, 1, ""
                )
            }


        })

    }

    private fun isFeedBackValid(): Boolean {

        if (CommonFunctions.isEmpty(
                binding.nameEdt,
                binding.nameLay,
                this@SignupActivity!!,
                getString(R.string.error_please_enter_name)
            )
        )
            if (CommonFunctions.isValidEmail(binding.emailEdt, binding.emailLay, this@SignupActivity!!))
                if (CommonFunctions.isValidPhone(binding.phoneEdt, binding.phoneEdtLay, this@SignupActivity!!))
                    if (CommonFunctions.isValidPwd(binding.pwdEdt, binding.pwdEdtLay, this@SignupActivity!!))

                        return true

        return false
    }


}