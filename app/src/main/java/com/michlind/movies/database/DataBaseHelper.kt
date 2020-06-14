package com.michlind.movies.database

import android.content.Context
import com.michlind.movies.model.DefaultData
import com.michlind.movies.model.Movie
import kotlinx.coroutines.*
import org.jsoup.Jsoup


class DataBaseHelper() {

    companion object {

        //Check if DB empty
        suspend fun isDbEmpty(context: Context) = withContext(Dispatchers.IO) {
            val db = AppDatabase(context)
            db.movieDao().countElements() <= 0

        }

        //Adding all movies
        suspend fun addAllMovies(context: Context, movieList: List<Movie>) =
            withContext(Dispatchers.IO) {
                val db = AppDatabase(context)

                movieList.forEach {
                    val movieEntity =
                        generateEntity(it)
                    db.movieDao().insert(movieEntity)
                }
            }

        //Add single movie to DB
        suspend fun addMovieToDB(context: Context, movie: Movie) = withContext(Dispatchers.IO) {
            var exist: Boolean
            val db = AppDatabase(context)

            val count = db.movieDao().countSingle(movie.title, movie.releaseYear)
            if (count >= 1) {
                exist = true
            } else {
                var movieEntity = generateEntity(movie)
                db.movieDao().insert(movieEntity)
                exist = false
            }
            exist
        }

        //Get all movies from db
        suspend fun getAllMoviesFromDB(context: Context) = withContext(Dispatchers.IO) {

            val db = AppDatabase(context)
            val moviesArrayList: ArrayList<Movie>? = ArrayList()

            val movies = db.movieDao().getAll()
            movies.forEach {
                val genreList: List<String> = it.genre!!.split(DefaultData.DELIMITER).toList()
                val mov =
                    Movie(it.title!!, it.image!!, it.rating!!, it.releaseYear!!, genreList)
                moviesArrayList!!.add(mov)
            }
            moviesArrayList
        }

    }
}

private fun generateEntity(movie: Movie): MovieEntity {

    val posterUrl = if (movie.image.contains("imdb") && !movie.image.endsWith(".jpg")) {
        val document = Jsoup.connect(movie.image).get()
        val element = document.select("meta[itemprop=image]").first()
        val str = element.attr("content")
        str
    } else {
        movie.image
    }

    return MovieEntity(
        movie.title,
        posterUrl,
        movie.rating,
        movie.releaseYear,
        movie.commaSeparatedGenres()
    )

}

