package com.hillux.citispy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hillux.citispy.models.UserModel
import kotlinx.android.synthetic.main.fragment_tag_user.view.*

class UserRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private  var items: List<UserModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_tag_user, parent, false)
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is UserViewHolder -> {
                holder.bind(items.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class UserViewHolder constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){
        val userName: TextView = itemView.userName
        val tagUserBtn: Button = itemView.tag_user
        fun bind(user: UserModel){
            val name: String = user.first_name+ " " + user.last_name
            userName.text = name
        }
    }



}