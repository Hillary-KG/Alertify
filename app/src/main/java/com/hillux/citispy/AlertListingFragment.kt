package com.hillux.citispy

//import android.widget.SearchView
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessaging
import com.hillux.citispy.R.menu.options_menu
import com.hillux.citispy.models.AlertModel
import com.hillux.citispy.models.UserModel
import kotlinx.android.synthetic.main.fragment_tag_user.*
import kotlinx.android.synthetic.main.layout_user_list_item.*
import kotlinx.android.synthetic.main.layout_user_list_item.view.*


/**
 * A simple [Fragment] subclass.
 */
class AlertListingFragment : Fragment() {
 
    private  lateinit var alertsAdapter: AlertRecyclerAdapter
    private lateinit var alertsView: RecyclerView
    private val alertsList: ArrayList<AlertModel> = DataSource.createAlertDataSet()

//    init {
//        usersList = DataSource.createDataSet()
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_alerts, container, false)
//        val recyclerView: RecyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        alertsView = view.findViewById<RecyclerView>(R.id.alerts_recycler_view)
        alertsView.layoutManager = LinearLayoutManager(alertsView.context)
        val topSpacingDecoration = TopSpacingDecoration(25)
        alertsView.addItemDecoration(topSpacingDecoration)
        alertsView.hasFixedSize()
        setHasOptionsMenu(true)

        getAlertList()

//        var itemView = alertsView.tag_user
//        itemView.setOnClickListener {
//            val successText = itemView.
//            val duration = Toast.LENGTH_SHORT
//            val toast = Toast.makeText(this.requireContext(), successText, duration)
//            toast.show()
//        }

//        initRecyclerView(recyclerView)
//        addDataSet()

        return view
    }
//    private fun addDataSet(){
//        val data = DataSource.createDataSet()
//        alertsAdapter.submitList(data)
//    }
//    private fun initRecyclerView(recyclerView: RecyclerView){
//        recyclerView.apply {
////            hasFixedSize()
//            layoutManager = LinearLayoutManager(requireContext())
//            setHasFixedSize(true)
//            val topSpacingItemDecoration = TopSpacingItemDecoration(20)
//            addItemDecoration(topSpacingItemDecoration)
//            alertsAdapter = UserRecyclerAdapter()
//            adapter = alertsAdapter
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(options_menu, menu)
        val alert_search: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = alert_search.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                alertsAdapter.filter.filter(newText)
                return false
            }
        })
//        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getAlertList(){
//        val users = DataSource.createDataSet()
        alertsAdapter = AlertRecyclerAdapter(alertsList, this.requireContext(), this)
        alertsView.adapter = alertsAdapter
    }

//    override fun onItemClick(position: Int) {
////        val successText = "Success"
//        val clickeduser = usersList[position]
//        val user_topic: String = clickeduser.userTopic
//        FirebaseMessaging.getInstance().subscribeToTopic(user_topic)
//            .addOnCompleteListener { task ->
//                var msg = "User tagged successfully"
//                if (!task.isSuccessful) {
//                    msg = "User tagging failed"
//                }
//                Log.d(TAG, msg)
//                Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show()
//            }
//
//
////        val duration = Toast.LENGTH_SHORT
////        val toast = Toast.makeText(this.requireContext(), clickeduser.userToken, duration)
////        toast.show()
//    }

    private fun checkGooglePlayServices(): Boolean {
        // 1
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this.requireContext())
        // 2
        return if (status != ConnectionResult.SUCCESS) {
            Log.e(TAG, "Error")
            // ask user to update google play services and manage the error.
            false
        } else {
            // 3
            Log.i(TAG, "Google play services updated")
            true
        }
    }
    
}
