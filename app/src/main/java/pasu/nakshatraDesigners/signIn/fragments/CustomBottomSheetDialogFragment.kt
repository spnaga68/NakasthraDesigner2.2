package pasu.nakshatraDesigners.signIn.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.databinding.FragmentForgotPasswordBinding
import pasu.nakshatraDesigners.signIn.PhoneVerificationActivity
import pasu.nakshatraDesigners.signIn.repository.CommonViewModelFactory
import pasu.nakshatraDesigners.signIn.viewModel.PasswordViewModel
import pasu.nakshatraDesigners.utils.*
import nakshatraDesigners.utils.CommonFunctions
import nakshatraDesigners.utils.CommonFunctions.isValidPhone

private const val ARG_PARAM1 = "showGetPasswordLayout"


class CustomBottomSheetDialogFragment : BottomSheetDialogFragment(), DialogOnClickInterface {
    private var alertDialog: AlertDialog? = null
    override fun onPositiveButtonCLick(dialog: DialogInterface, alertType: Int) {
        when (alertType) {
            0 -> {
                dialog.dismiss()
                this@CustomBottomSheetDialogFragment.dismiss()
            }
            else -> {
                dialog.dismiss()
                this@CustomBottomSheetDialogFragment.dismiss()
            }
        }
    }

    override fun onNegativeButtonCLick(dialog: DialogInterface, alertType: Int) {
        when (alertType) {
            0 -> dialog.dismiss()
            else -> dialog.dismiss()
        }
    }

    private var showGetPasswordLayout: Boolean = false
    private lateinit var viewModel: PasswordViewModel
    private lateinit var binding: FragmentForgotPasswordBinding

    companion object {
        @JvmStatic
        fun getInstance(showGetPasswordLayout: Boolean) =
            CustomBottomSheetDialogFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_PARAM1, showGetPasswordLayout)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            showGetPasswordLayout = it.getBoolean(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false)
        viewModel =
            ViewModelProviders.of(this, context?.let { CommonViewModelFactory(it) }).get(PasswordViewModel::class.java)
        binding.apply {
            passwordViewModel = viewModel
            setLifecycleOwner(this@CustomBottomSheetDialogFragment)
            executePendingBindings()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showGetPasswordLayout.value = showGetPasswordLayout
    }

    override fun onResume() {
        super.onResume()
        binding.imgCancelDialog.setOnClickListener {
            this.dismiss()
        }

        binding.btnDone.setOnClickListener {
            DisplayUtils.hideKeyboard(binding.root.context, binding.etPhoneNumber)
            if (isValidPhone(
                    binding.etPhoneNumber, binding.edMobileLayout
                    , context!!
                )
            ) {
                verifyNumberAndSendOtp()
            }
        }
    }

    private fun verifyNumberAndSendOtp() {
        context?.run {
            val countryCode = DEFAULT_COUNTRY_CODE
            val phoneNo = binding.etPhoneNumber.text.toString()

            viewModel.forgotPassword(phoneNo, countryCode).observe(viewLifecycleOwner, Observer { forgotResponse ->
                if (forgotResponse != null) {
                    when (forgotResponse.status) {
                        1 -> {
                            var intent = Intent(context, PhoneVerificationActivity::class.java)
                            if (forgotResponse.otp_reference != null && !forgotResponse.otp_reference.equals("")) {
                                intent.putExtra(OTP_PIN, forgotResponse.otp_reference)
                                intent.putExtra(OTP_UNIQUE, forgotResponse.otp_unique)
                                Toast.makeText(this, forgotResponse.otp_reference, Toast.LENGTH_LONG).show()
                            }

                            startActivity(intent)
                            dismiss()
                        }
                        else -> {
                            alertDialog = CommonFunctions.alertDialog(
                                this,
                                this@CustomBottomSheetDialogFragment,
                                forgotResponse.message,
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
    }


    override fun onDismiss(dialog: DialogInterface) {
        alertDialog?.run {
            if (isShowing)
                dismiss()
        }
        super.onDismiss(dialog)
    }
}