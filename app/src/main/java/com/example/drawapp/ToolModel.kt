package com.example.drawapp

import androidx.annotation.DrawableRes
import com.example.drawapp.base.Item

data class ToolModel(@DrawableRes var tool: Int, var color: Int? = null) : Item