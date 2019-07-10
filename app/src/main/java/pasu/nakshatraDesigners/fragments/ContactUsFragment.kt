package pasu.nakshatraDesigners.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.contact_us.*
import nakshatraDesigners.utils.CommonFunctions
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.data.FeedBackData
import pasu.nakshatraDesigners.utils.*
import pasu.nakshatraDesigners.viewModel.ContactUsViewModel
import pasu.nakshatraDesigners.viewModel.DashboardViewModel
import pasu.nakshatraDesigners.viewModel.ViewmodelFactory

class ContactUsFragment : MapFragment(), DialogOnClickInterface {
    override fun setDraggedAddress(latitude: Double, longitude: Double, address: String) {

    }

    override fun getMap(map: GoogleMap) {
        SetMarkerLocation(LatLng(Session.getSplashDATA(context!!).address1_latitude.toDouble(),
            Session.getSplashDATA(context!!).address1_longitude.toDouble()),
            LatLng(Session.getSplashDATA(context!!).address2_latitude.toDouble(),
                Session.getSplashDATA(context!!).address2_longitude.toDouble()))
    }

    override fun updateLocFirstTime(currLatLng: LatLng) {
    }

    override fun onPositiveButtonCLick(dialog: DialogInterface, alertType: Int) {
        binding.phoneEdt.setText("")
        binding.emailEdt.setText("")
        binding.messageEdt.setText("")
        binding.nameEdt.setText("")

        dialog.dismiss()
    }

    override fun onNegativeButtonCLick(dialog: DialogInterface, alertType: Int) {
        binding.phoneEdt.setText("")
        binding.emailEdt.setText("")
        binding.messageEdt.setText("")
        binding.nameEdt.setText("")
        dialog.dismiss()
    }

    private lateinit var binding: pasu.nakshatraDesigners.databinding.ContactUsBinding
    private lateinit var contactUsViewModel: ContactUsViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.contact_us, container, false)
        contactUsViewModel =
            ViewModelProviders.of(this, ViewmodelFactory(context!!)).get(ContactUsViewModel::class.java)
        binding.apply {
            data = Session.getSplashDATA(context!!)
            executePendingBindings()
        }

        initializeMap(binding.root)
        feedBackRes()
        binding.phoneEdt.setText(Session.getSession(USER_PHONE_NO,context))
        binding.emailEdt.setText(Session.getSession(USER_EMAIL,context))
        binding.messageEdt.setText("")
        binding.nameEdt.setText(Session.getSession(USER_NAME,context))
        binding.submit.setOnClickListener {
            if (isFeedBackValid()) {
                binding.layoutLoading.visibility = View.VISIBLE
                binding.submit.visibility = View.GONE
                contactUsViewModel.sendFeedBack(
                    FeedBackData(
                        binding.nameEdt.text.toString(),
                        binding.messageEdt.text.toString(),
                        binding.emailEdt.text.toString(),
                        binding.phoneEdt.text.toString()
                    )
                )
            }
        }
        return binding.root

    }


    fun feedBackRes() {
        contactUsViewModel.responseData.observe(this, Observer { response ->

            binding.layoutLoading.visibility = View.GONE
            binding.submit.visibility = View.VISIBLE
            response?.let {

                CommonFunctions.alertDialog(
                    context!!,
                    this,
                    response.Message,
                    getString(R.string.ok),
                    "",
                    false, 1, ""
                )
            }


            if (response == null) {

                CommonFunctions.alertDialog(
                    context!!,
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
                nameEdt,
                nameLay,
                context!!,
                getString(R.string.error_please_enter_name)
            )
        )
        if (CommonFunctions.isValidEmail(emailEdt, emailLay, context!!))
            if (CommonFunctions.isValidPhone(phoneEdt, phoneEdtLay, context!!))
                if (CommonFunctions.isEmpty(
                        messageEdt,
                        messageEdtLay,
                        context!!,
                        getString(R.string.error_please_enter_description)
                    )
                )

                        return true

        return false
    }

    private fun SetMarkerLocation(start: LatLng, end: LatLng) {
        val options = MarkerOptions()
        options.position(start)
        val options_end = MarkerOptions()
        options_end.position(end)
//        options.icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_shop))
//        options_end.icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_location))
        options.icon(BitmapDescriptorFactory.defaultMarker(0.0f))
        options_end.icon(BitmapDescriptorFactory.defaultMarker(120.0f))
        this.map.addMarker(options)
        this.map.addMarker(options_end)


        val builder = LatLngBounds.Builder()
        builder.include(start)
        builder.include(end)

        val bounds = builder.build()

        val padding = 100 // offset from edges of the map in pixels
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        map.moveCamera(cu)

    }

}