package com.dkrasnov.photoeditor.editor

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dkrasnov.photoeditor.R
import kotlinx.android.synthetic.main.v_background_selection_item.view.*

class BackgroundSelectionAdapter(
    private val selectCallback: (BackgroundSelectionItem) -> Unit
) : RecyclerView.Adapter<BackgroundSelectionAdapter.BackgroundSelectionViewHolder>() {

    private val items = mutableListOf<BackgroundSelectionItem>()

    fun setItems(items: List<BackgroundSelectionItem>) {
        this.items.run {
            clear()
            addAll(items)
        }
    }

    override fun onCreateViewHolder(container: ViewGroup, p1: Int): BackgroundSelectionViewHolder {
        return BackgroundSelectionViewHolder(
            LayoutInflater.from(container.context)
                .inflate(R.layout.v_background_selection_item, container, false)
        ).apply {
            itemView.setOnClickListener {
                selectCallback.invoke(items[adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(viewHolder: BackgroundSelectionViewHolder, position: Int) {
        viewHolder.bind(position)
    }

    override fun getItemCount(): Int = items.size

    inner class BackgroundSelectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
            val item = items[position]

            if (item is SourceBackgroundSelectionItem) {
                itemView.imageView.background = item.getThumb(itemView.context)
            } else {
                itemView.imageView.setBackgroundResource(R.drawable.ic_toolbar_new)
            }
        }
    }
}