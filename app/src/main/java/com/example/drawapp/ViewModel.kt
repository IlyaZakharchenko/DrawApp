package com.example.drawapp

import com.example.drawapp.base.BaseViewModel
import com.example.drawapp.base.Event
import org.koin.android.ext.android.inject

class ViewModel(private val imageSaver : ImageSaver) : BaseViewModel<ViewState>() {

    override fun initialViewState(): ViewState = ViewState(
        toolList = enumValues<TOOL>().map {
            ToolModel(
                it.value,
                when (it) {
                    TOOL.PALETTE -> R.color.colorPaintBlack
                    else -> null
                }
            )
        },
        colorList = enumValues<COLOR>().map { ColorModel(it.value) },
        penList = enumValues<PEN>().map { PenModel(it.value) },
        canvasViewState = CanvasViewState(COLOR.BLACK, PEN.SQUARE),
        isPaletteVisible = false,
        isPenBarVisible = false,
        clear = false,
        save = false
    )

    override fun reduce(event: Event, previousState: ViewState): ViewState? {
        val toolList = previousState.toolList
        val paletteIdx = toolList.indexOfFirst { TOOL.from(it.tool) == TOOL.PALETTE }
        when (event) {
            is UiEvent.OnColorClicked -> {
                toolList[paletteIdx].color = previousState.colorList[event.index].color
                return previousState.copy(
                    canvasViewState = previousState.canvasViewState.copy(
                        color = COLOR.from(previousState.colorList[event.index].color)
                    ),
                    clear = false,
                    save = false
                )
            }
            is UiEvent.OnToolClicked -> {
                return when (TOOL.from(toolList[event.index].tool)) {
                    TOOL.PALETTE -> {
                        toolList.forEachIndexed { index, toolModel ->
                            toolModel.color =
                                when (index) {
                                    paletteIdx -> toolModel.color
                                    else -> null
                                }
                        }
                        previousState.copy(
                            isPaletteVisible = !previousState.isPaletteVisible,
                            isPenBarVisible = false,
                            clear = false,
                            save = false
                        )
                    }
                    TOOL.PEN -> {
                        toolList.forEachIndexed { index, toolModel ->
                            toolModel.color =
                                when (index) {
                                    event.index -> R.color.colorAccent
                                    paletteIdx -> toolModel.color
                                    else -> null
                                }
                        }
                        previousState.copy(
                            isPenBarVisible = !previousState.isPenBarVisible,
                            isPaletteVisible = false,
                            clear = false,
                            save = false
                        )
                    }
                    TOOL.CLEAR -> {
                        toolList.forEachIndexed { index, toolModel ->
                            toolModel.color =
                                when (index) {
                                    paletteIdx -> toolModel.color
                                    else -> null
                                }
                        }
                        previousState.copy(
                            clear = true,
                            save = false
                        )
                    }
                }
            }
            is UiEvent.OnSaveClicked -> {
                return previousState.copy(
                    isPaletteVisible = false,
                    clear = false,
                    save = true
                )
            }
            is UiEvent.OnPenClicked -> {
                val penIndex = toolList.indexOfFirst { TOOL.from(it.tool) == TOOL.PEN }
                toolList[penIndex].tool = previousState.penList[event.index].pen
                return previousState.copy(
                    isPenBarVisible = false,
                    clear = false,
                    save = false,
                    canvasViewState = previousState.canvasViewState.copy(
                        pen = PEN.from(previousState.penList[event.index].pen)
                    )
                )
            }
            is UiEvent.OnCanvasClicked -> {
                return previousState.copy(
                    isPenBarVisible = false,
                    isPaletteVisible = false,
                    clear = false,
                    save = false
                )
            }
            is UiEvent.OnSaveDrawing -> {
                imageSaver.saveImage(event.bitmap)
            }
        }
        return null
    }

}