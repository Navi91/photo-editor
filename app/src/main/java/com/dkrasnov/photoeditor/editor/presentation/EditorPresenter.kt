package com.dkrasnov.photoeditor.editor.presentation

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dkrasnov.photoeditor.background.BackgroundSourceRepository
import com.dkrasnov.photoeditor.editor.presentation.backgroundselection.BackgroundSelectionItem
import com.dkrasnov.photoeditor.editor.presentation.backgroundselection.PlusBackgroundSelectionItem
import com.dkrasnov.photoeditor.editor.presentation.backgroundselection.SourceBackgroundSelectionItem

@InjectViewState
class EditorPresenter : MvpPresenter<EditorView>() {

    private val backgroundSourceRepository = BackgroundSourceRepository()
    private var backgroundSelectionItems = mutableListOf<BackgroundSelectionItem>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        backgroundSelectionItems.run {
            addAll(backgroundSourceRepository.getBackgroundSourceList().mapIndexed { index, backgroundSource ->
                SourceBackgroundSelectionItem(
                    backgroundSource,
                    index == 0
                )
            })
            add(PlusBackgroundSelectionItem())
        }

        viewState.setBackgroundSelectionItems(backgroundSelectionItems)
    }

    fun selectBackgroundItem(selectedItem: BackgroundSelectionItem) {
        if (selectedItem !is SourceBackgroundSelectionItem) {
            viewState.showUploadPhotoDialog()
            return
        }

        backgroundSelectionItems = backgroundSelectionItems.copy().apply {
            forEach { item ->
                item.selected = item is SourceBackgroundSelectionItem && item.source == selectedItem.source
            }
        }

        viewState.setBackgroundSelectionItems(backgroundSelectionItems)
    }

    private fun List<BackgroundSelectionItem>.copy() : MutableList<BackgroundSelectionItem> {
        val items = mutableListOf<BackgroundSelectionItem>()

        this.forEach { item ->
            if (item is SourceBackgroundSelectionItem) {
                items.add(SourceBackgroundSelectionItem(item.source, item.selected))
            } else {
                items.add(PlusBackgroundSelectionItem(item.selected))
            }
        }

        return items
    }
}