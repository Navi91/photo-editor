package com.dkrasnov.photoeditor.editor.presentation.backgroundselection

import android.support.v7.util.DiffUtil

class BackgroundSelectionDiffCallback : DiffUtil.ItemCallback<BackgroundSelectionItem>() {

    override fun areItemsTheSame(p0: BackgroundSelectionItem, p1: BackgroundSelectionItem): Boolean {
        if (p0::class != p1::class) return false

        if (p0 is PlusBackgroundSelectionItem) return true

        if (p0 !is SourceBackgroundSelectionItem || p1 !is SourceBackgroundSelectionItem) {
            throw IllegalStateException("Unknown background items classes $p0 $p1")
        }

        return p0.source == p1.source
    }

    override fun areContentsTheSame(p0: BackgroundSelectionItem, p1: BackgroundSelectionItem): Boolean {
        return p0.selected == p1.selected
    }
}