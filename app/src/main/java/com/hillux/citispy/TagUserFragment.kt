package com.hillux.citispy

//import android.widget.SearchView
import android.app.SearchManager
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hillux.citispy.R.menu.options_menu
import kotlinx.android.synthetic.main.fragment_tag_user.*


/**
 * A simple [Fragment] subclass.
 */
class TagUserFragment : Fragment() {
//    lateinit var searchView: SearchView
    private  lateinit var usersAdapter: UserRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.tag_user_fragment, container, false)
        val recyclerView: RecyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        setHasOptionsMenu(true)

        initRecyclerView(recyclerView)
        addDataSet()

        return view
    }
    private fun addDataSet(){
        val data = DataSource.createDataSet()
        usersAdapter.submitList(data)
    }
    private fun initRecyclerView(recyclerView: RecyclerView){
        recyclerView.apply {
//            hasFixedSize()
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            val topSpacingItemDecoration = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingItemDecoration)
            usersAdapter = UserRecyclerAdapter()
            adapter = usersAdapter
        }
    }

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
