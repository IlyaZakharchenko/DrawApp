package com.example.drawapp

import android.graphics.Bitmap
import com.example.drawapp.base.Event

data class ViewState(
    val toolList: List<ToolModel>,
    val colorList: List<ColorModel>,
    val penList: List<PenModel>,
    val canvasViewState: CanvasViewState,
    val isPenBarVisible: Boolean,
    val isPaletteVisible: Boolean,
    val clear: Boolean,
    val save: Boolean
)

sealed class UiEvent() : Event {
    data class OnColorClicked(val index: Int) : UiEvent()
    data class OnToolClicked(val index: Int) : UiEvent()
    data class OnPenClicked(val index: Int) : UiEvent()
    data class OnSaveDrawing(val bitmap: Bitmap) : UiEvent()
    object OnSaveClicked : UiEvent()
    object OnCanvasClicked : UiEvent()
}
