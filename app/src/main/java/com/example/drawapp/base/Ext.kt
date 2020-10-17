package com.example.drawapp.base

import android.view.View
import com.example.drawapp.ViewState

fun View.showIf(show: Boolean) {
    if (show) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}