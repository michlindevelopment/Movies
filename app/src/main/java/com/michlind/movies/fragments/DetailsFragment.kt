package com.michlind.movies.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.michlind.movies.R
import com.michlind.movies.model.ViewModel
import com.michlind.movies.model.Movie
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class DetailsFragment() : Fragment() {

    lateinit var movie: Movie

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val myViewModel = ViewModelProvider(activity!!).get(ViewModel::class.java)
        movie = myViewModel.movie


        val view = inflater.inflate(R.layout.details_fragment, container, false)

        val title = view.findViewById<TextView>(R.id.textMovieTitle)
        val rating = view.findViewById<TextView>(R.id.textViewRating)
        val year = view.findViewById<TextView>(R.id.textViewYear)
        val genres = view.findViewById<TextView>(R.id.textViewGenres)
        val poster = view.findViewById<ImageView>(R.id.imageViewPoster)

        //Load poster
        Picasso.with(context).load(movie.image)
            .placeholder(R.drawable.placeholder)
            .into(poster)


        title.text = (movie.title)
        rating.text = getString(R.string.rating) + (movie.rating.toString())
        year.text = getString(R.string.release) + (movie.releaseYear.toString())
        genres.text = getString(R.string.genres) + (movie.commaSeparatedGenres())

        return view
    }
}