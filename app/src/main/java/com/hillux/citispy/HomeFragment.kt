package com.hillux.citispy

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TimeUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import com.google.android.gms.location.*
import com.google.android.gms.maps.OnMapReadyCallback

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.hillux.citispy.models.UserModel as DBUser
import kotlinx.android.synthetic.main.fragment_app_home.raise_alert_btn
import kotlinx.android.synthetic.main.nav_header.*
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.location.*
import android.location.Location
import android.location.LocationListener
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import org.json.JSONObject
import com.android.volley.Response
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.hillux.citispy.databinding.FragmentAppHomeBinding
import org.json.JSONException
import kotlin.collections.ArrayList
import com.hillux.citispy.utils.PermissionUtils


import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.*
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAAkpRwkEI:APA91bEzAfE-eijC8K2Du6s3NmdW4BFOfncj9jsdO4Nyb4y3PD311Qqcuf3v" +
                "JnYIrOvtxtAHh1yKkhY5NP7eo7xjehK93GL0ZU9pVgXxAG9NYH_USpMyfo4vcYRiXLGcjfw-qh5S9jug"

    private val contentType = "application/json"
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(activity?.applicationContext)
    }
    private var user: FirebaseUser? = null
    private var auth: FirebaseAuth? = null
    private var alertRaiser: DBUser? = null
    private lateinit var database: DatabaseReference

//    lateinit var loc: Location
    private lateinit var fusedLoationClient: FusedLocationProviderClient
    private lateinit var raiseAlertBtn:Button

    private lateinit var accidentAlert: CheckBox
    private lateinit var crimeAlert: CheckBox
    private lateinit var healthAlert: CheckBox
    private lateinit var otherAlert: CheckBox
    private lateinit var fireAlert: CheckBox

    private lateinit var alertType: String

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_app_home, container, false)
        val binding = DataBindingUtil.inflate<FragmentAppHomeBinding>(inflater, R.layout.fragment_app_home, container, false)
        accidentAlert = binding.accidentAlert
        crimeAlert = binding.crimeAlert
        healthAlert = binding.healthAlert
        fireAlert = binding.fireAlert
        otherAlert = binding.otherAlert

        auth = FirebaseAuth.getInstance()
        user = auth!!.currentUser
        database = FirebaseDatabase.getInstance().reference

//        val mapFragment = supportFragmentManager.
        fusedLoationClient = LocationServices.getFusedLocationProviderClient(requireContext())
//        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        raiseAlertBtn = binding.raiseAlertBtn
//            .findViewById(R.id.raise_alert_btn)
        raiseAlertBtn.setOnClickListener { view->
//            Log.d("Location button clicked", "Locccc")

            try {
               val loc: ArrayList<Double> = ArrayList()
                fusedLoationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
                fusedLoationClient.lastLocation.addOnSuccessListener {
                    val locArray = listOf<Double>(it.latitude, it.longitude)
                    Log.d("Location","" + it.latitude + "," + it.longitude)
                    user?.let { raiseAlarm(locArray, it, binding.checkboxTagContacts) }
                }
//                Log.d("Location button clicked", .toString())

//                    context?.let {
//                        LocationHelper().startListeningUserLocation(it, object : LocationHelper.MyLocationListener {
//                            override fun onLocationChanged(location: Location) {
//                                // Here you got user location :)
//                                loc.addAll(listOf(location.latitude, location.longitude))
//                                Log.d("Location","" + location.latitude + "," + location.longitude)
//                            }
//                        })
//                    }

            }catch(ex: SecurityException) {
                Log.d("myTag", "Security Exception, no location available")
            }

        }

        return binding.root
    }

    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
//                        latTextView.text = location.latitude.toString()
//                        lngTextView.text = location.longitude.toString()

                    }
                    // Few more things we can do here:
                    // For example: Update the location of user on server
                }
            },
            Looper.myLooper()
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this.requireContext()) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this.requireContext())
                        }
                    }
                } else {
                    Toast.makeText(
                        this.requireContext(),
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(this.requireContext()) -> {
                when {
                    PermissionUtils.isLocationEnabled(this.requireContext()) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this.requireContext())
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this.requireActivity() as AppCompatActivity,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun raiseAlarm(location: List<Double>, user: FirebaseUser, view: View): Boolean {
        var alert: Alert? = null
        val loc = location
        Log.d("Locationnnananana", loc.toString())
        val time = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.of("Africa/Nairobi"))
            .format(Instant.now())
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.

//        val time = "2020-07-14 15:04:50"
        val userRef = database.child("users").child(user!!.uid)
        Log.d("user details one", user!!.uid)
        val userListener = object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    alertRaiser = dataSnapshot.getValue(DBUser::class.java)
                    when {
                        accidentAlert.isChecked -> {
                            alertType = accidentAlert.text.toString()
                        }
                        crimeAlert.isChecked -> {
                            alertType = crimeAlert.text.toString()
                        }
                        healthAlert.isChecked -> {
                            alertType = healthAlert.text.toString()
                        }
                        fireAlert.isChecked -> {
                            alertType = fireAlert.text.toString()
                        }
                        otherAlert.isChecked -> {
                            alertType = otherAlert.text.toString()
                        }else ->{
                            alertType = "Other"
                        }
                    }
                    Log.d("LOCCCC", loc.toString())
//                    val myLocation = Geocoder(context, Locale.getDefault())
//                    val myList: List<Address> =
//                        myLocation.getFromLocation(loc[0], loc[1], 1)
//                    val address: Address = myList[0] as Address
//                    var addressStr = ""
//                    addressStr += address.getAddressLine(0).toString() + ", "
//                    addressStr += address.getAddressLine(1).toString() + ", "
//                    addressStr += address.getAddressLine(2)
                    val lat = loc[0].toDouble()
                    val lng = loc[1].toDouble()

                    val address = getAddress(lat, lng)
                    Log.d("Adderere", address)

                    alert = Alert(address, time.toString(), alertRaiser!!.taggedUsers, alertRaiser, alertType)

                    if(view is CheckBox){
                        val checked: Boolean = view.isChecked
                        when(view.id){
                            R.id.checkboxTagContacts ->{
                                if (checked){
                                    tagUsers(alertRaiser!!, loc.toString())
                                }
                            }
                        }
                    }
                    saveAlert(alert!!)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("LoadUser:onCancelled", databaseError.toException())
            }
        }
        userRef!!.addValueEventListener(userListener)
        //        var userRef = database.child("users").child(user!!.uid)
//
//        val userListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()){
//                    val  u = dataSnapshot.getValue(DBUser::class.java)!!
//                    Log.d("User Phone", alertRaiser!!.phone_number)
//                }else{
//                    Log.d("User does not exist", "NONE")
//                }
//                alertRaiser = u
//
//            }
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.w("LoadUser:onCancelled", databaseError.toException())
//            }
//        }
//        userRef!!.addValueEventListener(userListener)



        //raise alarm



        return true
    }

    fun saveAlert(alert: Alert): Boolean{

        val alertRef = database.child("alerts")
        val retValue = alertRef.push().setValue(alert)
        return true
    }

    fun tagUsers(user: DBUser, location: String): Boolean{
        val notification = JSONObject()
        val notificationBody = JSONObject()
        val topic = "/topics/"+ user.userTopic
        Log.d("user Topic", topic)

        var msg: String = "${user.last_name} ${user.last_name} raised an emergency alert " +
                "that needs your urgent attention. Their location is: $location. " +
                "Kindly respond asap to save a life."
        try {
            notificationBody.put("title", "Alert Notification!")
            notificationBody.put("message", msg)
            notification.put("to", topic)
            notification.put("data", notificationBody)
            Log.e("TAG", "try")
        } catch (e: JSONException) {
            Log.e("TAG", "onCreate: " + e.message)
        }
        sendNotification(notification)
        return true
    }

    private fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener<JSONObject> { response ->
                Log.i("TAG", "onResponse: $response")
//                msg.setText("")
            },
            Response.ErrorListener {
                Toast.makeText(this.activity, "Request error", Toast.LENGTH_LONG).show()
                Log.i("TAG", "onErrorResponse: Didn't work")
            }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

    private fun getAddress(lat: Double, lng: Double): String {
        val geocoder = Geocoder(this.context)
        val list = geocoder.getFromLocation(lat, lng, 1)
        return list[0].getAddressLine(0)
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

data class Alert(
    var location: String = "",
    var time:String = "",
    var taggedUsers: List<String> = listOf(),
    var user: DBUser?,
    var type:String = "",
    var status:String = "pending"

)