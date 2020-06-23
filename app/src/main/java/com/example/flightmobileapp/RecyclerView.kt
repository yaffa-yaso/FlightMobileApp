package com.example.flightmobileapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImagesAdapter(val images: List<Result>): RecyclerView.Adapter<ImagesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ImagesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        return holder.bind(images[position])
    }
}

class ImagesViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
    private val photo:ImageView = itemView.findViewById(R.id.photo)

    fun bind(image: Result) {
        Glide.with(itemView.context).load("http://image.tmdb.org/t/p/w500${image.screenshot}").into(photo)
    }

}
