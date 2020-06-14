package com.michlind.movies.api

import com.michlind.movies.model.DefaultData
import com.michlind.movies.model.Movie
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiService {
    @GET(DefaultData.JSON_FILE)
    fun getMovies(): Observable<List<Movie>>
}