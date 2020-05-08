package com.hillux.citispy

import android.app.ListActivity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.ListFragment
//import android.widget.SearchView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.*


/**
 * A simple [Fragment] subclass.
 */
class TagUserFragment : Fragment() {
//    lateinit var searchView: SearchView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.tag_user_fragment, container, false)
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.search_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }

}
