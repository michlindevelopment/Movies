package com.michlind.movies.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.michlind.movies.R
import com.michlind.movies.model.Movie
import com.squareup.picasso.Picasso


class MyMovieRecyclerViewAdapter(
    private val values: ArrayList<Movie>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<MyMovieRecyclerViewAdapter.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.fragment_item, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = values[position]

        //Add item click listener
        holder.bind(position, itemClickListener)
        holder.contentView.text = item.title

        //Load Image
        Picasso.with(context).load(item.image)
           .placeholder(R.drawable.placeholder)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val contentView: TextView = view.findViewById(R.id.textViewMovieName)
        val imageView: ImageView = view.findViewById(R.id.imageViewMovieIcon)


        fun bind(position: Int, clickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                clickListener.onItemClicked(position)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}

interface OnItemClickListener {
    fun onItemClicked(item: Int)
}


