package com.dkrasnov.photoeditor.stickers.presentation

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dkrasnov.photoeditor.R
import com.dkrasnov.photoeditor.stickers.data.StickerData

class StickersSelectionAdapter : RecyclerView.Adapter<StickersSelectionAdapter.StickerSelectionItemViewHolder>() {

    private val items = mutableListOf<StickerData>()

    fun setItems(items: List<StickerData>) {
        this.items.apply {
            clear()
            addAll(items)
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): StickerSelectionItemViewHolder {
        return StickerSelectionItemViewHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.v_sticker_selection_item, p0, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(viewHolder: StickerSelectionItemViewHolder, p1: Int) {

    }

    inner class StickerSelectionItemViewHolder(view: View) : RecyclerView.ViewHolder(view)
}