package com.dkrasnov.photoeditor.editor.presentation

import com.arellomobile.mvp.MvpView
import com.dkrasnov.photoeditor.background.BackgroundSource
import com.dkrasnov.photoeditor.editor.presentation.backgroundselection.BackgroundSelectionItem

interface EditorView : MvpView {

    fun setBackgroundSelectionItems(items: List<BackgroundSelectionItem>)

    fun showUploadPhotoDialog()

    fun setBackground(source: Any)

    fun setMessageStyle(dark: Boolean)
}