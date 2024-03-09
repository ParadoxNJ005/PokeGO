package com.example.ar.Adapter

import android.os.Handler
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import java.util.Timer
import java.util.TimerTask

class AutoScrollManager(private val recyclerView: RecyclerView) {
    private var timer: Timer? = null
    private var isAutoScrolling = false

    fun startAutoScroll(intervalMs: Long) {
        if (!isAutoScrolling) {
            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    val handler = Handler(recyclerView.context.mainLooper)
                    handler.post {
                        recyclerView.smoothScrollBy(500, 0) // Adjust the scroll amount as needed
                        Log.d("AutoScrollManager", "Auto-scrolling")
                    }
                }
            }, 0, intervalMs)
            isAutoScrolling = true
        }
    }

    fun stopAutoScroll() {
        timer?.cancel()
        isAutoScrolling = false
    }
}