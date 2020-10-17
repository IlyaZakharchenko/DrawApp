package com.example.drawapp

import android.graphics.PorterDuff
import com.example.drawapp.base.Item
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_cap.view.*
import kotlinx.android.synthetic.main.item_palette.view.*

fun paletteAdapterDelegate(
    onClick: (Int) -> Unit
): AdapterDelegate<List<Item>> =
    adapterDelegateLayoutContainer<ColorModel, Item>(
        R.layout.item_palette
    ) {
        bind {
            itemView.color.setColorFilter(
                context.resources.getColor(item.color),
                PorterDuff.Mode.SRC_IN
            )
            itemView.setOnClickListener { onClick(adapterPosition) }
        }
    }

fun toolAdapterDelegate(
    onClick: (Int) -> Unit
): AdapterDelegate<List<Item>> = adapterDelegateLayoutContainer<ToolModel, Item>(
    R.layout.item_cap
) {

    itemView.setOnClickListener { onClick(adapterPosition) }
    bind {
        itemView.cap.setImageResource(item.tool)
        item.color?.let {
            itemView.cap.setColorFilter(
                context.resources.getColor(it),
                PorterDuff.Mode.SRC_IN
            )
        } ?: itemView.cap.clearColorFilter()
    }
}

fun penAdapterDelegate(
    onClick: (Int) -> Unit
): AdapterDelegate<List<Item>> = adapterDelegateLayoutContainer<PenModel, Item>(
    R.layout.item_cap
) {

    itemView.setOnClickListener { onClick(adapterPosition) }
    bind {
        itemView.cap.setImageResource(item.pen)
    }
}