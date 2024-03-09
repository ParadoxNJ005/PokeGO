package com.example.ar.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.WindowManager
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.ar.databinding.ActivityMainBinding
import com.google.ar.core.Config
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener{

    private var tts: TextToSpeech? = null
    private lateinit var binding: ActivityMainBinding
    lateinit var modelNode:ArModelNode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding.ball.isVisible = false

        val dis = intent.getStringExtra("dis").toString()
        val name = intent.getStringExtra("name").toString()
        val image = intent.getStringExtra("image").toString()
        val type1 = intent.getStringExtra("type1").toString()
        val type2 = intent.getStringExtra("type2").toString()

        tts = TextToSpeech(this, this)

        binding.sceneview.apply {
            this.lightEstimationMode=Config.LightEstimationMode.DISABLED
        }


        binding.click.setOnClickListener {
            val textToSpeak = "$dis"
            tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "")

            place()
        }


        modelNode= ArModelNode(binding.sceneview.engine,PlacementMode.INSTANT).apply {
            loadModelGlbAsync(
                glbFileLocation = "models/$name.glb",
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

        binding.ball.setOnClickListener {

            val rnds = (0..1).random()

            if(rnds == 0){
                ballFall(rnds)
                Toast.makeText(this, "Oops! It looks like this Pokemon broke free.", Toast.LENGTH_SHORT).show()
            }else{

                ballFall(rnds)
                val requestData = hashMapOf(
                    "name" to name ,
                    "image" to image,
                    "type1" to type1,
                    "type2" to type2,
                    "dis" to dis
                )
                FirebaseFirestore.getInstance().collection("bag").add(requestData).addOnSuccessListener{
                    Toast.makeText(this, "Boom ! You've caught a new Pokemon! Keep on training and collecting more!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(this, "Check your Internet Connection", Toast.LENGTH_SHORT).show()
                }

            }
        }


    }
    private fun place(){
        modelNode.anchor()
        binding.sceneview.planeRenderer.isVisible=false

        binding.ball.isVisible = true
        val rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = 3000
        rotateAnimation.repeatCount = Animation.INFINITE
        binding.ball.startAnimation(rotateAnimation)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language not supported!")
            } else {

            }
        }
    }

    public override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        binding.ball.clearAnimation()
        super.onDestroy()
    }


    private fun ballFall(rnds: Int) {
        binding.ball.clearAnimation()

        val transAnim = if (rnds == 1) {
            TranslateAnimation(
                0f, 0f, 0f, -(displayHeight / 2).toFloat()
            )
        } else {
            TranslateAnimation(
                0f, (displayHeight / 2).toFloat(), 0f, -(displayHeight / 2).toFloat()
            )
        }

        transAnim.startOffset = 500
        transAnim.duration = 1500
        transAnim.fillAfter = true

        transAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {

                if(rnds == 1){
                    modelNode.isVisible = false
                    if (tts != null) {
                        tts!!.stop()
                        tts!!.shutdown()
                    }
                }


                binding.ball.clearAnimation()
                val left = binding.ball.left
                val top = binding.ball.top
                val right = binding.ball.right
                val bottom = binding.ball.bottom
                binding.ball.layout(left, top, right, bottom)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                binding.ball.startAnimation(transAnim)
            }
        })

        binding.ball.startAnimation(transAnim)
    }


        override fun onBackPressed() {
            super.onBackPressed()
            val intent = Intent(this, select::class.java)
            startActivity(intent)
            finish()
        }

    private val displayHeight: Int
        get() = this.resources.displayMetrics.heightPixels


}
















