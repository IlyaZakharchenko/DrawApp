package com.example.drawapp

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.*
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.widget.Toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.FileDescriptor


class ImageSavingService: Service() {
    
    private val imageSaver: ImageSaver by inject()

    private lateinit var bitmap: Bitmap

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // We don't provide binding, so return null
        return ImageSavingServiceBinder()
    }

    fun startService(bitmap: Bitmap) {

            GlobalScope.launch {
                imageSaver.saveImage(bitmap)
            }
    }

    override fun onDestroy() {
        Toast.makeText(this, "Image saved!", Toast.LENGTH_SHORT).show()
    }

    inner class ImageSavingServiceBinder: Binder() {
        fun getService() = this@ImageSavingService
    }
}
