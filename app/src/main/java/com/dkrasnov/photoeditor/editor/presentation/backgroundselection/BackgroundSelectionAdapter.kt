package com.dkrasnov.photoeditor.editor.presentation.backgroundselection

import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.AdapterListUpdateCallback
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dkrasnov.photoeditor.R
import com.dkrasnov.photoeditor.utils.setVisible
import kotlinx.android.synthetic.main.v_background_selection_item.view.*

class BackgroundSelectionAdapter(
    private val selectCallback: (BackgroundSelectionItem) -> Unit
) : RecyclerView.Adapter<BackgroundSelectionAdapter.BackgroundSelectionViewHolder>() {

    private val itemsDiffer = AsyncListDiffer<BackgroundSelectionItem>(
        AdapterListUpdateCallback(this),
        AsyncDifferConfig.Builder<BackgroundSelectionItem>(BackgroundSelectionDiffCallback()).build()
    )

    fun setItems(items: List<BackgroundSelectionItem>) {
        itemsDiffer.submitList(mutableListOf<BackgroundSelectionItem>().apply { addAll(items) })
    }

    override fun onCreateViewHolder(container: ViewGroup, p1: Int): BackgroundSelectionViewHolder {
        return BackgroundSelectionViewHolder(
            LayoutInflater.from(container.context)
                .inflate(R.layout.v_background_selection_item, container, false)
        ).apply {
            itemView.setOnClickListener {
                selectCallback.invoke(getItem(adapterPosition))
            }
            itemView.imageView.clipToOutline = true
        }
    }

    override fun onBindViewHolder(viewHolder: BackgroundSelectionViewHolder, position: Int) {
        viewHolder.bind(position)
    }

    override fun getItemCount(): Int = itemsDiffer.currentList.size

    private fun getItem(position: Int): BackgroundSelectionItem = itemsDiffer.currentList[position]

    inner class BackgroundSelectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
            val item = getItem(position)

            itemView.selectionView.setVisible(item.selected)

            if (item is SourceBackgroundSelectionItem) {
                itemView.imageView.background = item.getThumb(itemView.context)
            } else {
                itemView.imageView.setBackgroundResource(R.drawable.ic_toolbar_new)
            }
        }
    }
}