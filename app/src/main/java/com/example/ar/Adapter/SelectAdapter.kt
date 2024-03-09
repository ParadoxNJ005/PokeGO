package com.example.ar.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ar.Activity.MainActivity
import com.example.ar.DataClass.InfoClass
import com.example.ar.R

class SelectAdapter(
    val context: Context,
    val list: MutableList<InfoClass>
): RecyclerView.Adapter<SelectAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.itemview,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text = list[position].name

        Glide.with(context)
            .load(list[position].image)
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            val intent = Intent(context , MainActivity::class.java)
            intent.putExtra("dis",list[position].dis)
            intent.putExtra("name",list[position].name)
            intent.putExtra("type1",list[position].type1)
            intent.putExtra("type2",list[position].type2)
            intent.putExtra("image",list[position].image)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(item: View):RecyclerView.ViewHolder(item){
        val name: TextView = itemView.findViewById(R.id.teamName)
        val image: ImageView = itemView.findViewById(R.id.teamLogo)
    }

}