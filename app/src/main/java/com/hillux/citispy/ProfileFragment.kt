package com.hillux.citispy

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.fragment_profile.edit_first_name
import kotlinx.android.synthetic.main.fragment_profile.edit_last_name
//import kotlinx.android.synthetic.main.fragment_profile.edit_phone_no
import kotlinx.android.synthetic.main.fragment_profile.save_profile
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.nav_header.*
import java.util.concurrent.FutureTask


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    public lateinit var auth: FirebaseAuth
    public  lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        view.save_profile!!.setOnClickListener{view ->
            Log.d("UpdateBtn", "update button clicked")
            var msg = "Profile updated"

            if(this.updateUser()){
                val HomeFragment = HomeFragment()
                val FragmentTransaction = requireFragmentManager().beginTransaction()

                val myToast: Toast = Toast.makeText(requireView().context, msg, Toast.LENGTH_SHORT)
                myToast.show()
                FragmentTransaction.replace(R.id.frame_layout, HomeFragment)
                FragmentTransaction.addToBackStack(null)
                FragmentTransaction.commit()
            }else{
                msg = "Profile could not be updated"
                val myToast: Toast = Toast.makeText(requireView().context, msg, Toast.LENGTH_SHORT)
                myToast.show()
            }
        }
        // Inflate the layout for this fragment
        return view
    }

    fun updateUser(): Boolean{
        var user = auth.currentUser
        database = FirebaseDatabase.getInstance()
        var usersRef = database.getReference("users").child(user!!.uid)
        val f_name = edit_first_name!!.text.toString()
        val l_name = edit_last_name!!.text.toString()

        var updateInfo: Map <String, String> = mapOf(
            "first_name" to f_name,
            "last_name" to l_name
//                    "phone_number" to phone_no
        )
        //update the record
        usersRef.updateChildren(updateInfo)

        return true
    }
}

