package com.example.ar.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ar.DataClass.InfoClass
import com.example.ar.R
import com.example.ar.others.Poki

class PokiAdapter(
    val context: Context,
    val list: MutableList<InfoClass>,
    val poki: MutableSet<String>
): RecyclerView.Adapter<PokiAdapter.ViewHolder>()  {


    inner class ViewHolder(item: View):RecyclerView.ViewHolder(item){
        val name: TextView = itemView.findViewById(R.id.name)
        val image: ImageView = itemView.findViewById(R.id.view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.featuredpoke,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text = list[position].name

        Glide.with(context)
            .load(list[position].image)
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.image)

        poki.add("models/${list[position].name}.glb")

    }


}