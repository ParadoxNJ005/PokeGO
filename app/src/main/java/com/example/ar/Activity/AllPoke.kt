package com.example.ar.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ar.Adapter.AutoScrollManager
import com.example.ar.Adapter.PokiAdapter
import com.example.ar.DataClass.InfoClass
import com.example.ar.databinding.ActivityAllPokeBinding
import com.google.ar.core.Config
import com.google.firebase.firestore.FirebaseFirestore
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position

class AllPoke : AppCompatActivity() {

    private lateinit var binding: ActivityAllPokeBinding
    lateinit var modelNode: ArModelNode
    private lateinit var adapter: PokiAdapter
    private var list: MutableList<InfoClass> = mutableListOf()
    private val db = FirebaseFirestore.getInstance()
    private var Poki: MutableSet<String> = mutableSetOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllPokeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.ball.isVisible = true
        binding.click.isVisible = false

        val rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = 3000
        rotateAnimation.repeatCount = Animation.INFINITE
        binding.ball.startAnimation(rotateAnimation)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding.sceneview.apply {
            this.lightEstimationMode= Config.LightEstimationMode.DISABLED
        }


        adapter = PokiAdapter(this, list,Poki)
        binding.rvv.layoutManager = CarouselLayoutManager(
            true, true, 0.5F, true, true, true, LinearLayoutManager.HORIZONTAL
        )

        binding.rvv.adapter = adapter
        (binding.rvv as CarouselRecyclerview).setInfinite(true)
        fetchFromFirestore()

        val autoScrollManager = AutoScrollManager(binding.rvv)
        autoScrollManager.startAutoScroll(2000)

        binding.ball.setOnClickListener {
            throwBall(Poki.toList().random())
        }

        binding.click.setOnClickListener{
            place()
        }

    }

    private fun fetchFromFirestore() {
        list.clear()
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("bag")

        collectionRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val name = document.getString("name") ?: ""
                val image = document.getString("image") ?: ""
                val type1 = document.getString("type1") ?: ""
                val type2 = document.getString("type2") ?: ""
                val dis = document.getString("dis") ?: ""

                list.add(InfoClass(name, image, type1, type2, dis))
            }
            adapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error in fetching data: ${exception.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun throwBall(name: String) {

        binding.ball.clearAnimation()

        val transAnim = TranslateAnimation(0f, 0f, 0f, -(displayHeight / 2).toFloat())

        transAnim.startOffset = 500
        transAnim.duration = 1500
        transAnim.fillAfter = true

        transAnim.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {

                binding.ball.clearAnimation()
                val left = binding.ball.left
                val top = binding.ball.top
                val right = binding.ball.right
                val bottom = binding.ball.bottom
                binding.ball.layout(left, top, right, bottom)

                binding.click.isVisible = true
                binding.ball.isVisible = false
                rndr(name)

            }

            override fun onAnimationRepeat(animation: Animation?) {
                binding.ball.startAnimation(transAnim)
            }

        })

        binding.ball.startAnimation(transAnim)

    }

    private val displayHeight: Int
        get() = this.resources.displayMetrics.heightPixels

    private fun rndr(name: String){

        modelNode= ArModelNode(binding.sceneview.engine, PlacementMode.INSTANT).apply {
            loadModelGlbAsync(
                glbFileLocation = name,
                scaleToUnits = 1f,
                centerOrigin = Position(-0.0f)
            )
            {
                binding.sceneview.planeRenderer.isVisible=true
            }
            onAnchorChanged ={
                binding.click.isGone=it!=null
            }
        }

        binding.sceneview.addChild(modelNode)

    }

        override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, select::class.java)
        startActivity(intent)
        finish()
    }

    private fun place() {
        modelNode.anchor()
        binding.sceneview.planeRenderer.isVisible = false
        binding.ball.isVisible = true
    }

}

