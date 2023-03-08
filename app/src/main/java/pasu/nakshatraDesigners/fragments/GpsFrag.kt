package pasu.nakshatraDesigners.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import android.widget.Toast
import android.location.LocationManager
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import androidx.core.content.ContextCompat.getSystemService
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.utils.DialogOnClickInterface
import nakshatraDesigners.utils.CommonFunctions
import android.content.Intent


abstract class GpsFrag : Fragment(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    DialogOnClickInterface {

    var mGoogleApiClient: GoogleApiClient? = null

    private var location: Location? = null

    private var lastKnownLatLng: LatLng = LatLng(0.0, 0.0)


    private lateinit var locationCallback: LocationCallback

    private lateinit var fusedLocationClient: FusedLocationProviderClient;

    fun createLocationRequest(): LocationRequest? {
        return LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }


    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            fusedLocationClient.requestLocationUpdates(
                createLocationRequest()!!,
                locationCallback,
                null /* Looper */
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager?

        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show()
        } else {
//            CommonFunctions.alertDialog(
//                context!!,
//                this@GpsFrag,
//                getString(R.string.gps_enable),
//                getString(R.string.go_to_settings),
//                getString(R.string.cancel),
//                false
//            ).show()
        }


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    println("hellllllllo $location")
                    lastKnownLatLng = LatLng(location.latitude, location.longitude)
                    getCurrentKnownLatLng(lastKnownLatLng)
                }
            }
        }
    }

    private val requestingLocationUpdates: Boolean = true

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }


    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    init {
        try {
            if (mGoogleApiClient != null) {
                mGoogleApiClient = GoogleApiClient.Builder(requireActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build()
                if (!mGoogleApiClient!!.isConnected) {
                    mGoogleApiClient!!.connect()
                } else {
                    mGoogleApiClient!!.reconnect()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnected(p0: Bundle?) {

    }


    abstract fun getCurrentKnownLatLng(lastLatLng: LatLng)

    fun getLastKnownLatLng(): LatLng {
        println("NAN___2 $lastKnownLatLng ___ ${lastKnownLatLng.latitude}")
        println("getLastKnownLatLng ${lastKnownLatLng.latitude} ____ ${lastKnownLatLng.longitude}")
        return lastKnownLatLng
    }

    fun getLastLocation(): Location? {
        return location
    }

    override fun onPositiveButtonCLick(dialog: DialogInterface, alertType: Int) {
        val callGPSSettingIntent = Intent(
            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
        )
        startActivity(callGPSSettingIntent)
        dialog.dismiss()
    }

    override fun onNegativeButtonCLick(dialog: DialogInterface, alertType: Int) {
        dialog.dismiss()
    }

}