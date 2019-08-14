package pasu.nakshatraDesigners.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.adapter.bindImageFromUrl
import pasu.nakshatraDesigners.data.LogoutRequestData
import pasu.nakshatraDesigners.data.ProfileRequestData
import pasu.nakshatraDesigners.data.ProfileResponseData
import pasu.nakshatraDesigners.utils.*
import pasu.nakshatraDesigners.viewModel.ProfileViewModel
import pasu.nakshatraDesigners.viewModel.ViewmodelFactory
import nakshatraDesigners.utils.CommonFunctions
import nakshatraDesigners.utils.CommonFunctions.isValidEmail
import pasu.nakshatraDesigners.signIn.data.SignUpDetail

class ProfileFrag : Fragment(), DialogOnClickInterface {
    val apiAlert = 1
    val logoutAlert = 2
    lateinit var profileData: SignUpDetail

    override fun onPositiveButtonCLick(dialog: DialogInterface, alertType: Int) {
        if (alertType == logoutAlert) {
            CommonFunctions.clearLoginSession(context!!)
            CommonFunctions.checkUserIdAndRedirect(context!!)
//            profileViewModel.showLoading.value = true
//            profileViewModel.callLogout(LogoutRequestData(Session.getSession(USER_ID, context)))
        }
    }


    override fun onNegativeButtonCLick(dialog: DialogInterface, alertType: Int) {
    }

    private lateinit var binding: pasu.nakshatraDesigners.databinding.FragProfileBinding


    private lateinit var profileViewModel: ProfileViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_profile, container, false)
        setUpViewModel()
        profileViewModel.getProfileData(ProfileRequestData(Session.getSession(USER_ID, context)))

        profileViewModel.profileResponseData.observe(this, Observer { response ->
            response?.let {
                if (response.responseCode == 400) {
                    if(response.data !=null && response.data.size>0) {
                        var data = response.data[0]
                        data?.let {
                            binding.data = data
                            profileData = data
                            updateProfileSession(profileData)
                        }
                    }else{

                    }
                } else {
                    CommonFunctions.alertDialog(
                        context!!,
                        this,
                        response.Message,
                        getString(R.string.ok),
                        "",
                        false, apiAlert, ""
                    )
                }
            }


            if (response == null) {

                CommonFunctions.alertDialog(
                    context!!,
                    this,
                    getString(R.string.server_error),
                    getString(R.string.ok),
                    "",
                    false, apiAlert, ""
                )
            }

            profileViewModel.showLoading.value = false
        })



        profileViewModel.updateprofileResponseData.observe(this, Observer { response ->
            response?.let {
                if (response.responseCode == 200) {
                    var data = response.data[0]
                    data?.let {
                        binding.data = data
                        profileData = data
                        updateProfileSession(profileData)
                        Toast.makeText(context, response.Message, Toast.LENGTH_SHORT).show()
                        activity?.onBackPressed()
                    }
                } else {
                    CommonFunctions.alertDialog(
                        context!!,
                        this,
                        response.Message,
                        getString(R.string.ok),
                        "",
                        false, apiAlert, ""
                    )
                }
            }


            if (response == null) {

                CommonFunctions.alertDialog(
                    context!!,
                    this,
                    getString(R.string.server_error),
                    getString(R.string.ok),
                    "",
                    false, apiAlert, ""
                )
            }

            profileViewModel.showLoading.value = false
        })



        binding.logOut.setOnClickListener {
            CommonFunctions.alertDialog(
                context!!,
                this,
                getString(R.string.logout_msg),
                getString(R.string.ok),
                getString(R.string.cancel),
                false, logoutAlert, getString(R.string.confirm_logout)
            )
        }

        binding.save.setOnClickListener {
            profileData.let {
                if (validateProfileField()) {
                    profileViewModel.editProfileData(
                        SignUpDetail(
                            Session.getSession(USER_ID, context),
                            binding.etPhone.text.toString(),
                            "",
                            "",
                            "",
                            binding.etEmail.text.toString(),
                            binding.etFirstName.text.toString(),
                            0,
                            Session.getAccessKey(requireContext()),

                            binding.etAddress.text.toString(),
                            binding.etCity.text.toString(),
                            binding.etState.text.toString()
                            ,
                            binding.etPostalLay.text.toString()


                        )
                    )
                }
            }
        }






        profileViewModel.logoutResponse.observe(this, Observer { response ->
            response?.let {
                if (response.status == 1) {

                    Toast.makeText(context!!, response.message, Toast.LENGTH_LONG).show()
                    CommonFunctions.clearLoginSession(context!!)
                    CommonFunctions.checkUserIdAndRedirect(context!!)

                } else {
                    CommonFunctions.alertDialog(
                        context!!,
                        this,
                        response.message,
                        getString(R.string.ok),
                        getString(R.string.cancel),
                        false, apiAlert, ""
                    )
                }

            }
            if (response == null) {

                CommonFunctions.alertDialog(
                    context!!,
                    this,
                    getString(R.string.server_error),
                    getString(R.string.ok),
                    getString(R.string.cancel),
                    false, apiAlert, ""
                )
            }

            profileViewModel.showLoading.value = false
        })


        binding.etEmail.setOnClickListener {
            Toast.makeText(
                context,
                getString(R.string.cant_edit_email),
                Toast.LENGTH_LONG
            ).show()
        }
        return binding.root
    }


    private fun updateProfileSession(data: SignUpDetail) {

        Session.save(USER_NAME, data.name, context)
//        Session.save(USER_LNAME, Detaildata.lName, context)
        Session.save(USER_EMAIL, data.email, context)
        Session.save(USER_PHONE_NO, data.phone, context)
        Session.save(USER_IMG, data.imageUrl, context)
        Session.save(USER_ID, data.userid, context)
//        bindImageFromUrl(binding.profilePic,Detaildata.image)
    }

    private fun validateProfileField(): Boolean {
        if (binding.etFirstName.text.toString().length < resources.getInteger(R.integer.fnameMinLength)) {
            CommonFunctions.alertDialog(
                context!!,
                this,
                getString(R.string.first_name_validataion),
                getString(R.string.ok),
                "",
                false, apiAlert, ""
            )
            return false
        } else if (!isValidEmail(binding.etEmail.text.toString())) {
            CommonFunctions.alertDialog(
                context!!,
                this,
                getString(R.string.email_validataion),
                getString(R.string.ok),
                "",
                false, apiAlert, ""
            )
            return false
        }


        return true
    }

    private fun setUpViewModel() {
        val factory = ViewmodelFactory(context!!)
        profileViewModel = ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java)
//        binding.profilePic.loadImage
        binding.apply {
            this.profileViewModel = this@ProfileFrag.profileViewModel
            lifecycleOwner = this@ProfileFrag
            executePendingBindings()
        }

    }
}