package com.example.drawapp

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.drawapp.base.showIf
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    companion object {
        private const val STORAGE_PERMISSION_REQUEST_CODE = 100
    }

    private val viewModel: ViewModel by viewModel()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Toast.makeText(this@MainActivity, "Service connected", Toast.LENGTH_SHORT).show()
            val service = (p1 as ImageSavingService.ImageSavingServiceBinder).getService()
            service.startService(drawView.getBitmap())
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Toast.makeText(this@MainActivity, "Service disconnected", Toast.LENGTH_SHORT).show()
        }

    }

    private lateinit var toolsLayouts: List<ToolsLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawView.setOnClickListener {
            viewModel.processUiEvent(UiEvent.OnCanvasClicked)
        }
        toolsLayouts = listOf(
            toolsBar as ToolsLayout,
            paletteBar as ToolsLayout,
            penBar as ToolsLayout
        )
        toolsLayouts[0].setOnClickListener {
            viewModel.processUiEvent(UiEvent.OnToolClicked(it))
        }
        toolsLayouts[1].setOnClickListener {
            viewModel.processUiEvent(UiEvent.OnColorClicked(it))
        }
        toolsLayouts[2].setOnClickListener {
            viewModel.processUiEvent(UiEvent.OnPenClicked(it))
        }
        openPalette.setOnClickListener {
            checkPermission()
        }
        viewModel.viewState.observe(this, Observer(::render))
    }

    private fun render(viewState: ViewState) {
        toolsLayouts[0].render(viewState.toolList)

        toolsLayouts[1].showIf(viewState.isPaletteVisible)
        toolsLayouts[1].render(viewState.colorList)

        toolsLayouts[2].showIf(viewState.isPenBarVisible)
        toolsLayouts[2].render(viewState.penList)

        if (viewState.clear) {
            drawView.clear()
        }
        drawView.render(viewState.canvasViewState)
    }

    private fun checkPermission() {
        when (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PackageManager.PERMISSION_GRANTED -> startSavingService()
            else -> ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startSavingService()
            } else {
                Toast
                    .makeText(
                        this,
                        "Storage permission required to save drawing",
                        Toast.LENGTH_LONG
                    )
                    .show()
            }
        }
    }

    private fun startSavingService() {
        val intent = Intent(this, ImageSavingService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }
}
