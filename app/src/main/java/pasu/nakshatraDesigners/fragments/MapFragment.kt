package pasu.nakshatraDesigners.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import pasu.nakshatraDesigners.R
import nakshatraDesigners.utils.CommonFunctions.smallMarker


abstract class MapFragment : GpsFrag(),
        OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener {

    private var dragLatLng: LatLng? = null

    private val zoom = 16f
    lateinit var map: GoogleMap

    private var mapFragments: SupportMapFragment? = null

//    var locationImg: AppCompatImageView? = null

    lateinit var locationLay: FrameLayout

    var currLocClick: Boolean = false

    var cameraMove: Int = 0

//    lateinit var mapWrapperLayout: MapWrapperLayout

    private var callAddressDrag: Runnable? = null
    private var handlerServercall: Handler? = null

    private var currentLatLng: LatLng = LatLng(0.0, 0.0)


    fun initializeMap(view: View) {
        if (activity != null) {
//            mapWrapperLayout = view.findViewById(R.userid.map_relative_layout)

            handlerServercall = Handler(Looper.getMainLooper())

            mapFragments = childFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment?
//            if (mapFragments == null)
//                CToast.ShowToast(activity, "nullll")
//            else {
                mapFragments!!.getMapAsync(this)
//            }

        }
//        locationImg = view.findViewById(R.userid.location_img)
//        locationLay = view.findViewById(R.id.location_frame)
//
//        view.findViewById<View>(R.userid.mov_cur_loc).setOnClickListener {
//            //            MapWrapperLayout.setmMapIsTouched(true)
//            currLocClick = true
//            zoomToLastKnownLatLng()
//        }


        callAddressDrag = Runnable {

        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        println("onMapReady ${currentLatLng.latitude}____${currentLatLng.longitude}")
        if (map != null && currentLatLng.latitude != 0.0) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoom))
            MarkerOptions().position(currentLatLng)

            map.addMarker(
                MarkerOptions().position(currentLatLng).icon(BitmapDescriptorFactory.fromBitmap(smallMarker(context!!)))
            )
//            locationLay.visibility=View.GONE
        } else {
//            if (map == null)
//                locationLay.setBackgroundColor(resources.getColor(R.color.white))
        }
        getMap(map)
        if (map != null && activity != null) {
//            mapWrapperLayout.init(map, TaxiUtil.getPixelsFromDp(activity!!, (39 + 20).toFloat()), true, null)
            setMapStyle()

            if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            map.isMyLocationEnabled = true
            if (map != null) {
                map.uiSettings.isMapToolbarEnabled = false
                map.uiSettings.isCompassEnabled = false
            }
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = false

            map.setOnCameraMoveStartedListener(this)
            map.setOnCameraIdleListener(this)
//            Picasso.get().load(R.drawable.flag_green).into(locationImg)
        }
    }

    override fun onCameraMoveStarted(reason: Int) {
        println("Camera move started $reason")
        cameraMove = reason

    }

    override fun onCameraIdle() {
        Log.d("onCameraChange", "onCameraChange" + map.cameraPosition.target.latitude)
        dragLatLng = LatLng(map.cameraPosition.target.latitude, map.cameraPosition.target.longitude)

//        if (cameraMove == 1) {
//            if (address != null)
//                address!!.cancel(true)
//            Log.e("Location_address", "onCameraChange: ca" + dragLatLng!!)
//            if (address == null || address!!.responseCode != AsyncTask.Status.PENDING && address!!.responseCode != AsyncTask.Status.RUNNING) {
//                if (dragLatLng!!.latitude != 0.0 && dragLatLng!!.longitude != 0.0)
//                    address = AddressFromLatLng(activity, LatLng(dragLatLng!!.latitude, dragLatLng!!.longitude), this@MapFragment).execute()
//            }
//        } else if (currLocClick) {
//            currLocClick = false
//            if (address != null)
//                address!!.cancel(true)
//            Log.e("Location_address", "onCameraChange: ca" + getLastKnownLatLng()!!)
//            if (address == null || address!!.responseCode != AsyncTask.Status.PENDING && address!!.responseCode != AsyncTask.Status.RUNNING) {
//                if (getLastKnownLatLng()!!.latitude != 0.0 && getLastKnownLatLng()!!.longitude != 0.0)
//                    address = AddressFromLatLng(activity, LatLng(getLastKnownLatLng()!!.latitude, getLastKnownLatLng()!!.longitude), this@MapFragment).execute()
//            }
//        }
    }



    abstract fun setDraggedAddress(latitude: Double, longitude: Double, address: String)

    abstract fun getMap(map: GoogleMap)

    abstract fun updateLocFirstTime(currLatLng: LatLng)

    fun setMapStyle() {
//        try {
//            // Customise the styling of the base map using a JSON object defined in a raw resource file.
//            val success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity!!, R.raw.map_style))
//            if (!success) {
//                println("Style parsing failed.")
//            }
//        } catch (e: Resources.NotFoundException) {
//            println("Can't find style. Error: ")
//        }
    }

    fun zoomToLastKnownLatLng() {
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(getLastKnownLatLng(), zoom))
    }

    override fun getCurrentKnownLatLng(lastLatLng: LatLng) {
        println("getCurrentKnownLatLng ${lastLatLng.latitude}_____${lastLatLng.longitude}")
        if (map != null && currentLatLng.latitude == 0.0) {
//            locationLay.visibility=View.GONE
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, zoom))
            map.addMarker(
                MarkerOptions().position(lastLatLng).icon(BitmapDescriptorFactory.fromBitmap(smallMarker(context!!)))
            )
            updateLocFirstTime(lastLatLng)
        }
        currentLatLng = lastLatLng

    }
}