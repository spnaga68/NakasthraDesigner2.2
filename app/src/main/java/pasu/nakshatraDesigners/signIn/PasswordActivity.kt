package pasu.nakshatraDesigners.signIn

import android.annotation.TargetApi
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.transition.Slide
import android.transition.Transition
import android.view.Gravity
import android.view.Gravity.END
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_password.*
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.signIn.fragments.CustomBottomSheetDialogFragment
import pasu.nakshatraDesigners.signIn.repository.CommonViewModelFactory
import pasu.nakshatraDesigners.signIn.viewModel.PasswordViewModel
import pasu.nakshatraDesigners.utils.*
import nakshatraDesigners.utils.CommonFunctions

class PasswordActivity : AppCompatActivity(), DialogOnClickInterface {
    private lateinit var binding: pasu.nakshatraDesigners.databinding.ActivityPasswordBinding
    private lateinit var viewModel: PasswordViewModel
    private var alertDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password)
        viewModel = ViewModelProviders.of(this, CommonViewModelFactory(this)).get(PasswordViewModel::class.java)
        binding.apply {
            passwordViewModel = viewModel
            setLifecycleOwner( this@PasswordActivity)
            executePendingBindings()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setEnterAnimations()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setEnterAnimations() {
        binding.layoutPassword.transitionName = resources.getString(R.string.transition_llPhone)
        binding.ivBack.transitionName = resources.getString(R.string.transition_arrow)
        val enterSlide = Slide(END)
        enterSlide.duration = 700
        enterSlide.addTarget(binding.layoutPassword)
        enterSlide.addListener(enterTransitionListener)
        enterSlide.interpolator = DecelerateInterpolator(2f)
        window.enterTransition = enterSlide

        val returnSlide = Slide(END)
        returnSlide.duration = 700
        returnSlide.addTarget(binding.layoutPassword)
        returnSlide.interpolator = DecelerateInterpolator()
        window.returnTransition = returnSlide

        val reenterSlide = Slide(Gravity.START)
        reenterSlide.duration = 700
        reenterSlide.addListener(reEnterTransitionListener)
        reenterSlide.interpolator = DecelerateInterpolator(2f)
        reenterSlide.addTarget(binding.layoutPassword)
        window.reenterTransition = reenterSlide
    }

    override fun onResume() {
        super.onResume()
        fabNext.setOnClickListener {
            val userPassword = binding.etOldPassword.text.toString()
            DisplayUtils.hideKeyboard(this, binding.etOldPassword)
            if (isValidPassword(binding.etOldPassword,binding.txtLayoutOldPassword))
                submitPassword(userPassword)
        }

        ivBack.setOnClickListener {
            DisplayUtils.hideKeyboard(this, binding.etOldPassword)
            super.onBackPressed()
        }
        binding.tvForgotPassword.setOnClickListener {
            DisplayUtils.hideKeyboard(this, binding.etOldPassword)
            val bottomSheetDialog = CustomBottomSheetDialogFragment.getInstance(true)
            bottomSheetDialog.show(supportFragmentManager, "CustomBottomSheetDialog")
        }
    }

    /**
     * Function to check whether password is valid or not
     */
    private fun isValidPassword(etPassword: EditText,txtLayoutPassword:TextInputLayout): Boolean {
        val userPassword = etPassword.text.toString()
        if (TextUtils.isEmpty(userPassword.trim())) {
            txtLayoutPassword.error = getString(R.string.error_valid_password)
            etPassword.requestFocus()
            return false
        } else if (userPassword.trim().length < resources.getInteger(R.integer.pwdLength)) {
            txtLayoutPassword.error = getString(R.string.error_valid_length_password)
            etPassword.requestFocus()
            return false
        } else return true
    }

    /**
     * Method to submit entered password
     * @param userPassword - Password to verify user
     */
    private fun submitPassword(userPassword: String) {
        val userId = Session.getSession(USER_ID, this)
        val userPhoneNumber = Session.getSession(USER_PHONE_NO, this)
        val userCountryCode = Session.getSession(COUNTRY_CODE, this)
        viewModel.showLoadingLayout.value = true
        viewModel.submitPassword(userId, userPhoneNumber, userCountryCode, userPassword).observe(this, Observer { signUpResponse ->
            viewModel.showLoadingLayout.value = false
            if (signUpResponse != null) {
                when (signUpResponse.responseCode) {
                    1 -> {
                        val signUpDetail = signUpResponse.data
                        Session.save(IS_PHONE_VERIFIED, true, this)
                        Session.save(USER_PHONE_NO, signUpDetail.phone, this)
                        Session.save(USER_NAME, signUpDetail.name, this)
                        Session.save(USER_EMAIL, signUpDetail.email, this)
                        Session.save(USER_ID, "${signUpDetail.userid}", this)
                        Session.save(COUNTRY_CODE, signUpDetail.countryCode ?: "", this)
                        CommonFunctions.checkUserIdAndRedirect(this)
                    }
                    else -> {
                        alertDialog = CommonFunctions.alertDialog(this, this@PasswordActivity, signUpResponse.Message, getString(R.string.ok), getString(R.string.cancel), true)
                    }
                }

            } else {
//                Toast.makeText(this, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                alertDialog = CommonFunctions.alertDialog(this, this@PasswordActivity, getString(R.string.server_error), getString(R.string.ok), getString(R.string.cancel), true)
            }
        })
    }

    override fun onPositiveButtonCLick(dialog: DialogInterface, alertType: Int) {
        when (alertType) {
            0 ->
                dialog.dismiss()
            else ->
                dialog.dismiss()
        }
    }

    override fun onNegativeButtonCLick(dialog: DialogInterface, alertType: Int) {
        when (alertType) {
            0 -> dialog.dismiss()
            else -> dialog.dismiss()
        }
    }

    private var enterTransitionListener: Transition.TransitionListener = object : Transition.TransitionListener {
        override fun onTransitionStart(transition: Transition) {


        }

        override fun onTransitionEnd(transition: Transition) {

            binding.layoutPassword.setBackgroundColor(ContextCompat.getColor(this@PasswordActivity, R.color.white))
            rootFrame.alpha = 1f
//            binding.etPassword.requestFocus()
//            Utils.showKeyboard(this@PasswordActivity)

        }

        override fun onTransitionCancel(transition: Transition) {

        }

        override fun onTransitionPause(transition: Transition) {

        }

        override fun onTransitionResume(transition: Transition) {

        }
    }

    private var reEnterTransitionListener: Transition.TransitionListener = object : Transition.TransitionListener {
        override fun onTransitionStart(transition: Transition) {


        }

        override fun onTransitionEnd(transition: Transition) {

            binding.layoutPassword.setBackgroundColor(ContextCompat.getColor(this@PasswordActivity, R.color.white))
            rootFrame.alpha = 1f
//            binding.etPassword.isCursorVisible = true
//            Utils.showKeyboard(this@PasswordActivity)
        }

        override fun onTransitionCancel(transition: Transition) {

        }

        override fun onTransitionPause(transition: Transition) {

        }

        override fun onTransitionResume(transition: Transition) {

        }
    }
}
