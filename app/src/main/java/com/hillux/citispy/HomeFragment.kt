package com.hillux.citispy

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.maps.OnMapReadyCallback

import kotlinx.android.synthetic.main.fragment_app_home.raise_alert_btn

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), OnMapReadyCallback {
    var location: Location? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_app_home, container, false)

        val mapFragment = supportFragmentManager.

//        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        raise_alert_btn.setOnClickListener { view->
//            try {
//                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0F, LocationListener)
//            }catch(ex: SecurityException) {
//                Log.d("myTag", "Security Exception, no location available")
//            }
//
//        }
        return view
    }

//    private var LocationListener: LocationListener = object : LocationListener {
//        override fun onLocationChanged(location: Location) {
//            thetext.text = ("" + location.longitude + ":" + location.latitude)
//        }
//        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
//        override fun onProviderEnabled(provider: String) {}
//        override fun onProviderDisabled(provider: String) {}
//    }

}