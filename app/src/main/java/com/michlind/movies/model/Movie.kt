package com.michlind.movies.model

data class Movie(val title:String, val image: String, val rating: Double, val releaseYear: Int, val genre: List<String>) {
      fun commaSeparatedGenres(): String? {
          return genre.joinToString (separator = DefaultData.DELIMITER)

    }
}

