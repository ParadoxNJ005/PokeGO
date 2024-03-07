package com.example.ar

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class SelectAdapter(
    val context: Context,
    val list: MutableList<InfoClass>
): RecyclerView.Adapter<SelectAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.itemview,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectAdapter.ViewHolder, position: Int) {

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