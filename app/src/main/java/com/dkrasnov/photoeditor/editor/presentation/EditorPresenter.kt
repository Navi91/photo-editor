package com.dkrasnov.photoeditor.editor.presentation

import android.content.Context
import android.graphics.Bitmap
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dkrasnov.photoeditor.background.BackgroundSourceRepository
import com.dkrasnov.photoeditor.editor.presentation.backgroundselection.BackgroundSelectionItem
import com.dkrasnov.photoeditor.editor.presentation.backgroundselection.PlusBackgroundSelectionItem
import com.dkrasnov.photoeditor.editor.presentation.backgroundselection.SourceBackgroundSelectionItem
import com.dkrasnov.photoeditor.utils.isDark

@InjectViewState
class EditorPresenter(private val context: Context) : MvpPresenter<EditorView>() {

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
        viewState.setBackground(backgroundSelectionItems.first { it.selected })
    }

    fun selectBackgroundItem(selectedItem: BackgroundSelectionItem) {
        if (selectedItem !is SourceBackgroundSelectionItem) {
            viewState.showUploadPhotoDialog()
            return
        }

        setSelectedItem(selectedItem)
        selectedItem.getSource(context)?.let { background ->
            viewState.setBackground(background)
            viewState.setMessageStyle(selectedItem.isDark(context))
        }
    }

    fun setBackgroundPhoto(bitmap: Bitmap?) {
        bitmap ?: return

        setSelectedItem(backgroundSelectionItems.last())
        viewState.setBackground(bitmap)

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, false)
        val pixelColor = scaledBitmap.getPixel(0, 0)

        viewState.setMessageStyle(pixelColor.isDark())
    }

    private fun setSelectedItem(selectedItem: BackgroundSelectionItem) {
        backgroundSelectionItems = backgroundSelectionItems.copy().apply {
            forEach { item ->
                if (selectedItem is SourceBackgroundSelectionItem) {
                    item.selected = item is SourceBackgroundSelectionItem && item.source == selectedItem.source
                } else {
                    item.selected = item is PlusBackgroundSelectionItem
                }
            }
        }

        viewState.setBackgroundSelectionItems(backgroundSelectionItems)
    }

    private fun List<BackgroundSelectionItem>.copy(): MutableList<BackgroundSelectionItem> {
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