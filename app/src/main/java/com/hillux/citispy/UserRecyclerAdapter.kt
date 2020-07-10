package com.hillux.citispy

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.hillux.citispy.models.UserModel
import kotlinx.android.synthetic.main.layout_user_list_item.tag_user
import kotlinx.android.synthetic.main.layout_user_list_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class UsersRecyclerViewAdapter(
    private  var userList: ArrayList<UserModel>,
    context: Context,
    private  var listener: OnItemClickListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    var usersFilterList = ArrayList<UserModel>()
    lateinit var mcontext: Context

    //    class UsersHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    inner class UserViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val userName: TextView = itemView.findViewById(R.id.userName)
        val tagUserBtn: Button = itemView.findViewById(R.id.tag_user)
        fun bind(user: UserModel) {
            val name: String = user.first_name + " " + user.last_name
            userName.text = name
        }

        init {
            tagUserBtn.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface  OnItemClickListener{
        fun  onItemClick(position: Int)
    }

    init {
        usersFilterList = userList
        mcontext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_user_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return usersFilterList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UsersRecyclerViewAdapter.UserViewHolder -> {
                holder.bind(usersFilterList[position])
            }
        }
//        holder.itemView.tag_user.setOnClickListener {
//            val successText = "User Tagged!!"
//            val duration = Toast.LENGTH_SHORT
//            val toast = Toast.makeText(mcontext, successText, duration)
//            toast.show()
//        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    usersFilterList = userList
                } else {
                    val resultList = ArrayList<UserModel>()
                    for (row in userList) {
                        if (row.first_name.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT)) || row.last_name.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    usersFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = usersFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                usersFilterList = results?.values as ArrayList<UserModel>
                notifyDataSetChanged()
            }
        }
    }
}

//class UserRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable{
//    private var usersList = ArrayList<UserModel>()
//    private var usersFullList = ArrayList<UserModel>()
//
//    //initializing the user list
////    init {
////        usersFullList = this.usersList
////    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return UserViewHolder(
//            LayoutInflater.from(parent.context).inflate(R.layout.layout_user_list_item, parent, false)
//        )
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
////        val context = holder.itemView.context
//        when(holder){
//            is UserViewHolder -> {
//                holder.bind(usersList.get(position))
//            }
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return usersList.size
//    }
//    fun submitList(userList: ArrayList<UserModel>){
//        usersList = userList
//        usersFullList = this.usersList
//    }
//
//    class UserViewHolder constructor(
//        itemView: View
//    ): RecyclerView.ViewHolder(itemView){
//        val userName: TextView = itemView.findViewById(R.id.userName)
//        val tagUserBtn: Button = itemView.findViewById(R.id.tag_user)
//        fun bind(user: UserModel){
//            val name: String = user.first_name+ " " + user.last_name
//            userName.text = name
//        }
//    }
//
//
//    override fun getFilter(): Filter {
//        return object: Filter(){
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                val charSearch = constraint.toString().toLowerCase(Locale.ROOT).trim()
//                val filteredList = ArrayList<UserModel>()
//                if (constraint != null) {
////                    Log.d("search pattern 22", "List is not null")
//                    if (constraint.isEmpty()){
////                        Log.d("search pattern 22", "List is empty")
////                        filteredList.addAll(usersFullList)
//                    }else{
////                        Log.d("search pattern 22", charSearch)
//                        Log.d("full list length--->", usersFullList.size.toString())
//                        for (item: UserModel in usersFullList){
////                            Log.d("search pattern 333", charSearch)
//                            if (item.first_name.toLowerCase(Locale.ROOT).contains(charSearch) ||
//                                item.last_name.toLowerCase(Locale.ROOT).contains(charSearch)) {
//                                Log.d("Contains string", item.first_name)
//                                filteredList.add(item)
//                            }
//                        }
//                    }
//                }
//                Log.d("filtered list size", filteredList.size.toString())
//                val results:FilterResults = FilterResults()
//                results.values = filteredList
//                return results
//            }
//
//            @Suppress("UNCHECKED_CAST")
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                if (results != null) {
//                    usersFullList = results.values as ArrayList<UserModel>
//                }
//
//                notifyDataSetChanged()
//            }
//        }
//    }
//}