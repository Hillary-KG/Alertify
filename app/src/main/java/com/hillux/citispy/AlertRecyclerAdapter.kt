package com.hillux.citispy

import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hillux.citispy.models.AlertModel
import java.util.*
import kotlin.collections.ArrayList


class AlertRecyclerAdapter(
    private var alertList: ArrayList<AlertModel>,
    context: Context,
    alertsListingFragment: AlertListingFragment
): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    var alertFilterList = ArrayList<AlertModel>()
    lateinit var mcontext: Context

    //    class UsersHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    inner class UserViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView){
        val alertTime: TextView = itemView.findViewById(R.id.alert_tme)
        val alertLoc: TextView = itemView.findViewById(R.id.alert_location)
//        val alertRaiser: TextView = itemView.findViewById(R.id.alert_raiser)
        val alertType: TextView = itemView.findViewById(R.id.alert_type)

        fun bind(alert: AlertModel) {
//            val raiser = alert.raiser
            val location = alert.location
            val type = alert.type
            val time = alert.time

            alertTime.text = "Time in: $time"
            alertType.text = "Alert type: "+type
            alertLoc.text = "Location: "+location
//            alertRaiser.text = "Raised by: "+raiser
        }


    }

    init {
        alertFilterList = alertList
        mcontext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_alert_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return alertFilterList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AlertRecyclerAdapter.UserViewHolder -> {
                holder.bind(alertFilterList[position])
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    alertFilterList = alertList
                } else {
                    val resultList = ArrayList<AlertModel>()
                    for (row in alertList) {
                        val loc: String = ""

//                        val myLocation = Geocoder(yourContext, Locale.getDefault())
//                        val myList: List<Address> =
//                            myLocation.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1)
//                        val address: Address = myList[0] as Address
//                        var addressStr = ""
//                        addressStr += address.getAddressLine(0).toString() + ", "
//                        addressStr += address.getAddressLine(1).toString() + ", "
//                        addressStr += address.getAddressLine(2)



                        if (row.type.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT)) || row.location.toString().toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    alertFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = alertFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                alertFilterList = results?.values as ArrayList<AlertModel>
                notifyDataSetChanged()
            }
        }
    }
}
 