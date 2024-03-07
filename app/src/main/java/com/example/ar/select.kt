package com.example.ar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ar.databinding.ActivitySelectBinding
import com.google.firebase.firestore.FirebaseFirestore

class select : AppCompatActivity() {

    private lateinit var adapter: SelectAdapter
    private var list: MutableList<InfoClass> = mutableListOf()
    private lateinit var binding: ActivitySelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        adapter = SelectAdapter(this, list)
        binding.rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.adapter = adapter

        fetchFromFirestore()
    }

    private fun fetchFromFirestore() {
        list.clear()
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("info")

        collectionRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val name = document.getString("name") ?: ""
                val image = document.getString("image") ?: ""
                val type1 = document.getString("type1") ?: ""
                val type2 = document.getString("type2") ?: ""
                val dis = document.getString("dis") ?: ""

                list.add(InfoClass(name, image, type1, type2,dis))
            }
            adapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error in fetching data: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}


//
