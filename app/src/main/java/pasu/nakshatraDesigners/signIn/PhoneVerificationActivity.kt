package pasu.nakshatraDesigners.signIn

import android.annotation.TargetApi
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.transition.Slide
import android.util.Log
import android.view.Gravity.RIGHT
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import pasu.nakshatraDesigners.BuildConfig
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.databinding.ActivityPhoneVerificationBinding
import pasu.nakshatraDesigners.signIn.repository.CommonViewModelFactory
import pasu.nakshatraDesigners.signIn.smsVerifier.INTENT_ACTION
import pasu.nakshatraDesigners.signIn.smsVerifier.OnSmsCatchListener
import pasu.nakshatraDesigners.signIn.smsVerifier.SmsReceiver
import pasu.nakshatraDesigners.signIn.viewModel.PhoneVerificationViewModel
import pasu.nakshatraDesigners.utils.*
import nakshatraDesigners.utils.CommonFunctions
import java.util.regex.Pattern


class PhoneVerificationActivity : AppCompatActivity(), DialogOnClickInterface {
    private lateinit var binding: ActivityPhoneVerificationBinding
    private lateinit var viewModel: PhoneVerificationViewModel
    private var smsReceiver: SmsReceiver? = null
    private var smsRetrieverClient: SmsRetrieverClient? = null
    private var isReset: Boolean = false
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var phoneNumber: String
    private lateinit var countryCode: String
    private lateinit var userOtp: String
    private lateinit var userId: String
    private var alertDialog: AlertDialog? = null
    private var redirectToCart = false
    private val TAG = PhoneVerificationActivity::class.java.simpleName
    private lateinit var otpUnique: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, CommonViewModelFactory(this)).get(PhoneVerificationViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_verification)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setEnterAnimations()
        binding.apply {
            phoneVerificationViewModel = viewModel
            setLifecycleOwner(this@PhoneVerificationActivity)
            executePendingBindings()
        }
        intent?.extras?.run {
            redirectToCart = getBoolean(REDIRECT_TO_CART, false)
            phoneNumber = getString(USER_PHONE_NO) ?: ""
            userOtp = getString(OTP_PIN) ?: ""
            otpUnique = getString(OTP_UNIQUE) ?: ""

            countryCode = getString(COUNTRY_CODE) ?: ""
            userId = getString(USER_ID) ?: ""
        }
        initialize()
    }

    private fun initialize() {
        countDownTimer = MyCountDownTimer(20000, 1000)
    }

    override fun onStart() {
        super.onStart()
        resetTimer()
        smsReceiver = SmsReceiver()
        smsRetrieverClient = SmsRetriever.getClient(this)
        registerReceiver(smsReceiver, IntentFilter(INTENT_ACTION))
        buildSmsRetrieverApiClient()
    }

    private fun buildSmsRetrieverApiClient() {
        val task = smsRetrieverClient?.startSmsRetriever()
        task?.addOnSuccessListener {
            Log.v(TAG, "addOnSuccessListener")
            smsReceiver?.run {
                setCallback(object : OnSmsCatchListener<String> {
                    override fun onSmsCatch(message: String) {
                        val code = parseCode(message)
                        if (code.length >= 4) {
                            userOtp = code
                            binding.edPinOne.setText(code[0].toString())
                            binding.edPinTwo.setText(code[1].toString())
                            binding.edPinThree.setText(code[2].toString())
                            binding.edPinFour.setText(code[3].toString())
                        }
                    }
                })
            }
        }

        task?.addOnFailureListener {
            Log.v(TAG, "addOnFailureListener")
        }
    }

    override fun onResume() {
        super.onResume()
        if (BuildConfig.DEBUG) {
            binding.tvOtp.visibility = View.VISIBLE
            binding.tvOtp.text = "Your Otp : $userOtp"
        } else binding.tvOtp.visibility = View.GONE
        binding.tvResendOtp.visibility = View.GONE
        binding.imgRetry.visibility = View.GONE
        enableResend(false)
        binding.fabNext.setOnClickListener {
            submitOtp(phoneNumber, countryCode, userOtp)
        }
        binding.tvResendOtp.setOnClickListener {
            countDownTimer.cancel()
            countDownTimer.start()
            resendOtp()
        }
        binding.ivBack.setOnClickListener {
            super.onBackPressed()
        }
        binding.edPinOne.setOnTouchListener { v, event ->
            binding.edPinOne.text?.clear()
            return@setOnTouchListener false
        }
        binding.edPinThree.setOnTouchListener { v, event ->
            binding.edPinThree.text?.clear()
            return@setOnTouchListener false
        }
        binding.edPinTwo.setOnTouchListener { v, event ->
            binding.edPinTwo.text?.clear()
            return@setOnTouchListener false
        }
        binding.edPinFour.setOnTouchListener { v, event ->
            binding.edPinFour.text?.clear()
            return@setOnTouchListener false
        }
        binding.edPinOne.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.edPinOne.text.toString().length == 1) {
                    binding.edPinTwo.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}

        })
        binding.edPinTwo.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.edPinTwo.text.toString().length == 1) {
                    binding.edPinThree.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}

        })
        binding.edPinThree.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.edPinThree.text.toString().length == 1) {
                    binding.edPinFour.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}

        })
        binding.edPinFour.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.edPinFour.text.toString().length == 1) {
                    !binding.edPinFour.requestFocus()
                    userOtp =
                        "${binding.edPinOne.text.toString().trim()}${binding.edPinTwo.text.toString().trim()}${binding.edPinThree.text.toString().trim()}${binding.edPinFour.text.toString().trim()}"
                    submitOtp(phoneNumber, countryCode, userOtp)
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}

        })
    }

    override fun onStop() {
        super.onStop()
        alertDialog?.run {
            if (isShowing)
                dismiss()
        }
        try {
            if (smsReceiver != null)
                unregisterReceiver(smsReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun resendOtp() {
        viewModel.showLoadingLayout.value = true
        viewModel.resendOtp(phoneNumber, countryCode, userOtp)
            .observe(this, androidx.lifecycle.Observer { resendOtpResponse ->
                viewModel.showLoadingLayout.value = false
                if (resendOtpResponse != null) {
                    alertDialog = when (resendOtpResponse.status) {
                        1 -> {
                            enableResend(false)
                            resetTimer()
                            buildSmsRetrieverApiClient()
                            CommonFunctions.alertDialog(
                                this,
                                this@PhoneVerificationActivity,
                                resendOtpResponse.message,
                                getString(R.string.ok),
                                getString(R.string.cancel),
                                true
                            )
                        }
                        else -> {
                            CommonFunctions.alertDialog(
                                this,
                                this@PhoneVerificationActivity,
                                resendOtpResponse.message,
                                getString(R.string.ok),
                                getString(R.string.cancel),
                                true
                            )
                        }
                    }
                } else {
                    Toast.makeText(this, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun validateEnterCode(): Boolean {
        DisplayUtils.hideKeyboard(this, currentFocus)
        if (binding.edPinOne.text.toString().trim().isEmpty()
            || binding.edPinTwo.text.toString().trim().isEmpty()
            || binding.edPinThree.text.toString().trim().isEmpty()
            || binding.edPinFour.text.toString().trim().isEmpty()
        ) {
            when {
                binding.edPinOne.text.toString().trim().isEmpty() -> binding.edPinOne.requestFocus()
                binding.edPinTwo.text.toString().trim().isEmpty() -> binding.edPinTwo.requestFocus()
                binding.edPinThree.text.toString().trim().isEmpty() -> binding.edPinThree.requestFocus()
                else -> binding.edPinFour.requestFocus()
            }
            return false
        }
        return true
    }

    private fun enableResend(enable: Boolean) {
        if (enable) {
            binding.tvResendOtp.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
            binding.imgRetry.setColorFilter(
                ContextCompat.getColor(this, R.color.colorAccent),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        } else {
            binding.tvResendOtp.setTextColor(ContextCompat.getColor(this, R.color.light_gray))
            binding.imgRetry.setColorFilter(
                ContextCompat.getColor(this, R.color.light_gray),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
        binding.tvResendOtp.isEnabled = enable

    }

    private fun submitOtp(phoneNumber: String, countryCode: String, userOtp: String) {
        if (validateEnterCode()) {
            viewModel.showLoadingLayout.value = true
            viewModel.submitOtp(phoneNumber, countryCode, userOtp, otpUnique)
                .observe(this, androidx.lifecycle.Observer { signUpResponse ->
                    viewModel.showLoadingLayout.value = false
                    if (signUpResponse != null) {
                        if (signUpResponse.responseCode == 1) {
//                        val Detaildata = signUpResponse.Detaildata
//                        Session.save(USER_ID, "${Detaildata.userid}", this)
//                        Session.save(USER_EMAIL, Detaildata.email, this)
//                        Session.save(USER_NAME, Detaildata.name, this)
//                        Session.save(USER_PHONE_NO, Detaildata.phone, this)
//                        Session.save(COUNTRY_CODE, Detaildata.countryCode, this)
//                        Session.save(IS_PHONE_VERIFIED, true, this)
                            Toast.makeText(this@PhoneVerificationActivity, signUpResponse.Message, Toast.LENGTH_SHORT)
                                .show()
//                        CommonFunctions.checkUserIdAndRedirect(this)
                            startActivity(Intent(this@PhoneVerificationActivity, PasswordActivity::class.java))
                        } else if (signUpResponse.responseCode == 0 || signUpResponse.responseCode == 2 || signUpResponse.responseCode == 3) {
                            alertDialog = CommonFunctions.alertDialog(
                                this,
                                this@PhoneVerificationActivity,
                                signUpResponse.Message,
                                getString(R.string.ok),
                                getString(R.string.cancel),
                                true
                            )
                        } else
                            Toast.makeText(
                                this@PhoneVerificationActivity,
                                signUpResponse.Message,
                                Toast.LENGTH_SHORT
                            ).show()
                    } else {
                        Toast.makeText(this, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                    }
                })
        }

    }

    private fun resetTimer() {
        countDownTimer.cancel()
        countDownTimer.start()
    }

    internal inner class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            enableResend(true)
        }

        override fun onTick(millisUntilFinished: Long) {
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setEnterAnimations() {
        binding.llphone.transitionName = resources.getString(R.string.transition_llPhone)
        val enterSlide = Slide(RIGHT)
        enterSlide.duration = 700
        enterSlide.addTarget(R.id.llphone)
        enterSlide.interpolator = DecelerateInterpolator(2f)
        window.enterTransition = enterSlide

        val returnSlide = Slide(RIGHT)
        returnSlide.duration = 700
        returnSlide.addTarget(R.id.llphone)
        returnSlide.interpolator = DecelerateInterpolator()
        window.returnTransition = returnSlide
    }

    /**
     * Parse verification code
     *
     * @param message sms Message
     * @return only four numbers from massage string
     */
    private fun parseCode(message: String): String {
        val p = Pattern.compile("\\b\\d{4}\\b")
        val m = p.matcher(message)
        var code = ""
        while (m.find()) {
            code = m.group(0)
        }
        return code
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
}
