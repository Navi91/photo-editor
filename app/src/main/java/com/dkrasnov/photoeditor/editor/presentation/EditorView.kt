package com.dkrasnov.photoeditor.editor.presentation

import com.arellomobile.mvp.MvpView
import com.dkrasnov.photoeditor.editor.presentation.backgroundselection.BackgroundSelectionItem

interface EditorView : MvpView {

    fun setBackgroundSelectionItems(items: List<BackgroundSelectionItem>)

    fun showUploadPhotoDialog()
}