package com.example.drawapp

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drawapp.base.Item
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.view_tools.view.*

class ToolsLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private var onClick: (Int) -> Unit = {}

    private val adapterDelegate = ListDelegationAdapter(
        toolAdapterDelegate {
            onClick(it)
        },
        paletteAdapterDelegate {
            onClick(it)
        },
        penAdapterDelegate {
            onClick(it)
        }
    )

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        toolsList.adapter = adapterDelegate
        toolsList.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    fun render(toolList: List<Item>) {
        adapterDelegate.items = toolList
        adapterDelegate.notifyDataSetChanged()
    }

    fun setOnClickListener(onClick: (Int) -> Unit) {
        this.onClick = onClick
    }
}