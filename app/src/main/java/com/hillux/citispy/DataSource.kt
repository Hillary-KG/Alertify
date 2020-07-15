package com.hillux.citispy

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hillux.citispy.models.AlertModel
import com.hillux.citispy.models.UserModel


class DataSource {
    companion object{
        fun createDataSet(): ArrayList<UserModel>{
            val list = ArrayList<UserModel>()

            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val usersRef = database.getReference("users")
//            Log.d("user details one", user!!.uid)
//            usersRef.addValueEventListener(ValueEventListener)
            val userListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    list.clear()

                    for (userSnapshot: DataSnapshot in dataSnapshot.children){
                        val user: UserModel? = userSnapshot.getValue(UserModel::class.java)
                        if (user != null) {
                            list.add(user)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("LoadUser:onCancelled", databaseError.toException())
                }
            }
            usersRef.addValueEventListener(userListener)
//            list.add()
            return list
        }

        fun createAlertDataSet(): ArrayList<AlertModel>{
            val alertList = ArrayList<AlertModel>()

            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val alertsRef = database.getReference("alerts")
//            Log.d("user details one", user!!.uid)
//            usersRef.addValueEventListener(ValueEventListener)
            val userListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    alertList.clear()

                    for (userSnapshot: DataSnapshot in dataSnapshot.children){
                        val alert: AlertModel? = userSnapshot.getValue(AlertModel::class.java)
                        if (alert != null) {
                            alertList.add(alert)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("LoadUser:onCancelled", databaseError.toException())
                }
            }
            alertsRef.addValueEventListener(userListener)
//            list.add()
            return alertList
        }
    }
}