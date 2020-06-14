package com.michlind.movies.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.reflect.Constructor

@Entity
data class MovieEntity(
    //@PrimaryKey val uid: Int,
    @PrimaryKey(autoGenerate = true) var uid: Int?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "rating") val rating: Double?,
    @ColumnInfo(name = "release_year") val releaseYear: Int?,
    @ColumnInfo(name = "genre") val genre: String?
) {
    constructor(
        title: String?,
        image: String?,
        rating: Double?,
        releaseYear: Int?,
        genre: String?
    ) : this(null, title, image, rating, releaseYear, genre)
}
