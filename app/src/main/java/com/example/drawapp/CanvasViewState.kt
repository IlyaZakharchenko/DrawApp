package com.example.drawapp

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

data class CanvasViewState(val color: COLOR, val pen: PEN)

enum class COLOR(
    @ColorRes
    val value: Int
) {
    BLACK(R.color.colorPaintBlack),
    RED(R.color.colorPaintRed),
    BLUE(R.color.colorPaintBlue);

    companion object {
        private val map = values().associateBy(COLOR::value)
        fun from(color: Int) = map[color] ?: BLACK
    }
}

enum class TOOL(
    @DrawableRes
    val value: Int
) {
    PEN(R.drawable.ic_cap_square),
    PALETTE(R.drawable.ic_tool_palette),
    CLEAR(R.drawable.ic_tool_clear);

    companion object {
        private val map = values().associateBy(TOOL::value)
        fun from(tool: Int) = map[tool] ?: PEN
    }
}

enum class PEN(
    @DrawableRes
    val value: Int
) {
    ROUND(R.drawable.ic_cap_round),
    SQUARE(R.drawable.ic_cap_square);

    companion object {
        private val map = PEN.values().associateBy(PEN::value)
        fun from(tool: Int) = map[tool] ?: SQUARE
    }
}