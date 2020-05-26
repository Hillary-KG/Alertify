package com.hillux.citispy

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hillux.citispy.models.UserModel
import kotlinx.android.synthetic.main.layout_user_list_item.tag_user
import java.util.*
import kotlin.collections.ArrayList


class UserRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable{
    private var usersList = ArrayList<UserModel>()
    private var usersFullList = ArrayList<UserModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_user_list_item, parent, false)
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val context = holder.itemView.context
        when(holder){
            is UserViewHolder -> {
                holder.bind(usersList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return usersList.size
    }
    fun submitList(userList: ArrayList<UserModel>){
        usersList = userList
    }

    class UserViewHolder constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){
        val userName: TextView = itemView.findViewById(R.id.userName)
        val tagUserBtn: Button = itemView.findViewById(R.id.tag_user)
        fun bind(user: UserModel){
            val name: String = user.first_name+ " " + user.last_name
            userName.text = name
        }
    }
    init {
        usersFullList = usersList
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<UserModel>()
                if (constraint != null) {
                    if (constraint.isEmpty()){
                        filteredList.addAll(usersFullList)
                    }else{
                        val charSearch = constraint.toString().toLowerCase(Locale.ROOT).trim()
                        Log.d("search pattern 22", usersFullList.size.toString())
                        for (item: UserModel in usersFullList){
//                            Log.d("search pattern 333", charSearch)
                            if (item.first_name.toLowerCase(Locale.ROOT).contains(charSearch)) {
                                Log.d("search pattern 333", item.first_name)
                                filteredList.add(item)
                            }
                        }
                    }
                }
                Log.d("collection", filteredList.size.toString())
                val results:FilterResults = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null) {
                    usersFullList = results.values as ArrayList<UserModel>
                }

                notifyDataSetChanged()
            }
        }
    }


}