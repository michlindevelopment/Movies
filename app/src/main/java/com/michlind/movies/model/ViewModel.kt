package com.michlind.movies.model


import androidx.lifecycle.ViewModel

class ViewModel : ViewModel() {
    lateinit var movies : ArrayList<Movie>
    lateinit var movie : Movie

}