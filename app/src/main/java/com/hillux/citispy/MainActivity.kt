package com.hillux.citispy

//import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import com.google.firebase.iid.FirebaseInstanceId
import com.hillux.citispy.databinding.ActivityMainBinding
import com.hillux.citispy.models.UserModel as DBUser

import kotlinx.android.synthetic.main.nav_header.u_identity
import kotlinx.android.synthetic.main.nav_header.u_name


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var btnTagUser: Button? = null
    private var btnRaiseAlarm: Button? = null
    private var editProfile: Button? = null

    private var progressBar: ProgressBar? = null

    private lateinit var database: FirebaseDatabase

    private var authListener: FirebaseAuth.AuthStateListener? = null
    private var auth: FirebaseAuth? = null
    private var user: FirebaseUser? = null


    lateinit var mDrawer: DrawerLayout
    lateinit var toolbar: Toolbar
    lateinit var navDrawer: NavigationView
//    private var toggle: ActionBarDrawerToggle? = null

    //fragments declarations
    lateinit var homeFragment: HomeFragment
//    lateinit var alertFragment: AlertFragment
    lateinit var tagUserFragment: TagUserFragment
    lateinit var profileFragment: ProfileFragment

//    lateinit var usersRef


    val firebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //data biding
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
//        firebaseAuth!!.addAuthStateListener (this.authStateListener)
        auth = FirebaseAuth.getInstance()
        user = FirebaseAuth.getInstance().currentUser
        database = FirebaseDatabase.getInstance()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_LOW)
            )
        }
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                Log.d(TAG, "Key: $key Value: $value")
            }
        }
        // [END handle_data_extras]

        if (user == null){
//            Log.d("null null null", "NULL")
            val intent = Intent(this, LoginRegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        if(user != null){
            var usersRef = database.getReference("users").child(user!!.uid)
//            Log.d("user details one", user!!.uid)
            val userListener = object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()){
                        val u = dataSnapshot.getValue(DBUser::class.java)
                        u_identity!!.text = u!!.phone_number.toString()
                        u_name!!.text = u!!.first_name.toString() + " " + u!!.last_name.toString()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("LoadUser:onCancelled", databaseError.toException())
                }
            }
            usersRef!!.addValueEventListener(userListener)
        }

//        var user_details = usersRef.child()
//        var user_details = usersRef.orderByChild("phone_number").equalTo(user!!.phoneNumber)
//        Log.d("user dits", user_details.toString())

//        u_identity!!.text = usersRef.child("phone_number").toString()
//        u_name!!.text = usersRef.child("first_name").toString()




        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = "Alertify"

       // Find our drawer view
        mDrawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        navDrawer = findViewById<NavigationView>(R.id.navView)

        val toggle = ActionBarDrawerToggle(
            this, mDrawer, toolbar, 0, 0
        )

        toggle.isDrawerIndicatorEnabled = true

        this.mDrawer.addDrawerListener(toggle)
        toggle.syncState()
        this.navDrawer.setNavigationItemSelectedListener(this)

        //default fragment is raise alert
        homeFragment = HomeFragment() //fragment code
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_go_home -> {
                homeFragment = HomeFragment() //fragment code
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, homeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
//            R.id.action_raise_alert -> {
//                alertFragment = AlertFragment() //fragment code
//                supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.frame_layout, alertFragment)
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                    .commit()
//            }
            R.id.action_edit_profile -> {
                profileFragment = ProfileFragment() //fragment code
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, profileFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.action_tag_contact -> {
                tagUserFragment = TagUserFragment() //fragment code
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, tagUserFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
        }
        mDrawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawer(GravityCompat.START)
        }
        super.onBackPressed()
    }







////        toggle = ActionBarDrawerToggle(mDrawer)
//        // Setup drawer view
//        setupDrawerContent(navDrawer!!);
//
//        // Lookup navigation view
//        val navigationView = findViewById<NavigationView>(R.id.navView)
////        navigationView.setNavigationItemSelectedListener(this)
//
//        // There is usually only 1 header view.
//        // Multiple header views can technically be added at runtime.
//        // We can use navigationView.getHeaderCount() to determine the total number.
//        if (navigationView.headerCount > 0) {
//            // avoid NPE by first checking if there is at least one Header View available
//            val headerLayout = navigationView.getHeaderView(0)
//        }
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // The action bar home/up action should open or close the drawer.
//        when (item.itemId) {
//            android.R.id.home -> {
//                mDrawer!!.openDrawer(GravityCompat.START)
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    private fun setupDrawerContent(navigationView: NavigationView) {
//        navigationView.setNavigationItemSelectedListener { menuItem ->
//            selectDrawerItem(menuItem)
//            true
//        }
//    }
//
//    fun selectDrawerItem(menuItem: MenuItem) {
//        // Create a new fragment and specify the fragment to show based on nav item clicked
//        var fragment: Fragment? = null
//        val fragmentClass: Class<*> = when (menuItem.itemId) {
//            R.id.action_go_home -> RaiseAlarm::class.java
//            R.id.action_raise_alert -> RaiseAlarm::class.java
//            R.id.action_tag_contact -> SearchUserActivity::class.java
//            R.id.action_edit_profile -> EditProfile::class.java
//            else -> RaiseAlarm::class.java
//        }
//        try {
//            fragment = fragmentClass.newInstance() as Fragment
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        // Insert the fragment by replacing any existing fragment
//        val fragmentManager: FragmentManager = supportFragmentManager
//        if (fragment != null) {
//            fragmentManager.beginTransaction().replace(R.id.main_fragment, fragment).commit()
//        }
//
//        // Highlight the selected item has been done by NavigationView
//        menuItem.isChecked = true
//        // Set action bar title
//        title = menuItem.title
//        // Close the navigation drawer
//        mDrawer!!.closeDrawers()
//    }

    override fun onStart() {
        super.onStart()
        firebaseAuth!!.addAuthStateListener (this.authStateListener)
    }
    val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val currentUser = firebaseAuth.currentUser

        if (currentUser == null) {
            val intent = Intent(this, LoginRegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun raiseAlarm(user: FirebaseUser){


    }
    private fun tagUser(user: FirebaseUser){

    }
    companion object {

        private const val TAG = "MainActivity"
    }
}

