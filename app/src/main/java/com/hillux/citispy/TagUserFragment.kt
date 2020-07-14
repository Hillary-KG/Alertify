package com.hillux.citispy

//import android.widget.SearchView
import android.app.SearchManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.hillux.citispy.R.menu.options_menu
import com.hillux.citispy.models.UserModel
import kotlinx.android.synthetic.main.fragment_tag_user.*
import kotlinx.android.synthetic.main.layout_user_list_item.*
import kotlinx.android.synthetic.main.layout_user_list_item.view.*


/**
 * A simple [Fragment] subclass.
 */
class TagUserFragment : Fragment(), UsersRecyclerViewAdapter.OnItemClickListener {
//    lateinit var searchView: SearchView
    private lateinit var auth: FirebaseAuth
    private  lateinit var usersAdapter: UsersRecyclerViewAdapter
    private lateinit var usersView: RecyclerView
    private val usersList: ArrayList<UserModel> = DataSource.createDataSet()

//    init {
//        usersList = DataSource.createDataSet()
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //get authenticatd user
        auth = FirebaseAuth.getInstance()
        var authUser = auth.currentUser
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.tag_user_fragment, container, false)
//        val recyclerView: RecyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        usersView = view.findViewById<RecyclerView>(R.id.recycler_view)
        usersView.layoutManager = LinearLayoutManager(usersView.context)
        usersView.hasFixedSize()
        setHasOptionsMenu(true)

        getUsersList()

//        var itemView = usersView.tag_user
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
//        usersAdapter.submitList(data)
//    }
//    private fun initRecyclerView(recyclerView: RecyclerView){
//        recyclerView.apply {
////            hasFixedSize()
//            layoutManager = LinearLayoutManager(requireContext())
//            setHasFixedSize(true)
//            val topSpacingItemDecoration = TopSpacingItemDecoration(20)
//            addItemDecoration(topSpacingItemDecoration)
//            usersAdapter = UserRecyclerAdapter()
//            adapter = usersAdapter
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(options_menu, menu)
        val user_search: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = user_search.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                usersAdapter.filter.filter(newText)
                return false
            }
        })
//        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getUsersList(){
//        val users = DataSource.createDataSet()
        usersAdapter = UsersRecyclerViewAdapter(usersList, this.requireContext(), this)
        usersView.adapter = usersAdapter
    }

    override fun onItemClick(position: Int) {
//        val successText = "Success"
        val clickeduser = usersList[position]
        val user_topic: String = clickeduser.userTopic
        FirebaseMessaging.getInstance().subscribeToTopic(user_topic)
            .addOnCompleteListener { task ->
                var msg = "User tagged successfully"
                if (!task.isSuccessful) {
                    msg = "User tagging failed"
                }
                Log.d(TAG, msg)
                Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show()
            }


//        val duration = Toast.LENGTH_SHORT
//        val toast = Toast.makeText(this.requireContext(), clickeduser.userToken, duration)
//        toast.show()
    }

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

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        initRecyclerView()
//        addDataSet()
//    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.search_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }

}
