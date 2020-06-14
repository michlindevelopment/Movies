package com.michlind.movies.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.michlind.movies.R
import com.michlind.movies.model.ViewModel
import com.michlind.movies.model.Movie

class ListFragment : Fragment() {

    companion object {
        const val FRAGMENT_TAG = "LIST_FRAGMENT"
    }

    private lateinit var moviesArrayList: ArrayList<Movie>
    private var mListener: OnFragmentInteractionListener? = null
    private lateinit var listView: RecyclerView

    //On pressing FAB notify MainActivity
    interface OnFragmentInteractionListener {
        fun onFloatingActionButtonClick()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) mListener = context else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        listView = view.findViewById(R.id.recyclerViewList)

        val fab: View = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            mListener!!.onFloatingActionButtonClick()
        }

        //ViewModel
        ViewModelProvider(activity!!).get(ViewModel::class.java).also {
            moviesArrayList = it.movies
        }

        listView.adapter =
            MyMovieRecyclerViewAdapter(moviesArrayList, object : OnItemClickListener {
                override fun onItemClicked(item: Int) {

                    val myViewModel = ViewModelProvider(activity!!).get(ViewModel::class.java)
                    myViewModel.movie = moviesArrayList[item]

                    //Start details fragment
                    val fragmentTransaction =
                        requireActivity().supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.container, DetailsFragment())
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
            })
        return view
    }

    //Refresh adapter with new data
    fun refreshAdapter(moviesArrayList: ArrayList<Movie>?) {
        this.moviesArrayList.clear()
        if (moviesArrayList != null) {
            this.moviesArrayList.addAll(moviesArrayList)
        }

        listView.adapter!!.notifyDataSetChanged()
    }

}

