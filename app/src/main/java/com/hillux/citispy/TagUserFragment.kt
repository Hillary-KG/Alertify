package com.hillux.citispy

import android.app.ListActivity
import android.os.Bundle
import androidx.fragment.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import kotlinx.android.synthetic.*


/**
 * A simple [Fragment] subclass.
 */
class TagUserFragment : ListFragment() {
    lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        searchView =
        return inflater.inflate(R.layout.fragment_tag_user, container, false)
    }

}
