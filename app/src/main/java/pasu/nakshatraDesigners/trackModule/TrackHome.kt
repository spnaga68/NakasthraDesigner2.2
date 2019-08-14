package pasu.nakshatraDesigners.trackModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.fragments.MapFragment


class TrackHome : MapFragment() {
    override fun setDraggedAddress(latitude: Double, longitude: Double, address: String) {

    }

    override fun getMap(map: GoogleMap) {
    }

    override fun updateLocFirstTime(currLatLng: LatLng) {
    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(pasu.nakshatraDesigners.R.layout.frag_track, container, false)
//        val mapFragment = childFragmentManager
//            ?.findFragmentById(pasu.nakshatraDesigners.R.userid.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//        initializeMap(v)
//        v.findViewById<View>(R.id.currentLocation).setOnClickListener { zoomToLastKnownLatLng() }
//
//        val llBottomSheet = v.findViewById<View>(pasu.nakshatraDesigners.R.id.bottom_sheet)
//
//// init the bottom sheet behavior
//        val bottomSheetBehavior = BottomSheetBehavior.from<View>(llBottomSheet)
//
//        bottomSheetBehavior.isHideable = false
//
//        val btmSheetArraow=v.findViewById<ImageView>(R.id.upArrow)
//        btmSheetArraow.setOnClickListener {
//            if(bottomSheetBehavior.state==BottomSheetBehavior.STATE_COLLAPSED){
//                bottomSheetBehavior.state=BottomSheetBehavior.STATE_EXPANDED
//                btmSheetArraow.setImageResource(R.drawable.ic_arrow_downward_black_24dp)
//            }else{
//                bottomSheetBehavior.state=BottomSheetBehavior.STATE_COLLAPSED
//                btmSheetArraow.setImageResource(R.drawable.ic_arrow_upward_black_24dp)
//            }
//        }
        return v;
    }
}