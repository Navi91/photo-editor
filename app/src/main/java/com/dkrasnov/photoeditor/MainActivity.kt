package com.dkrasnov.photoeditor

import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.dkrasnov.photoeditor.background.BackgroundSourceRepository
import com.dkrasnov.photoeditor.editor.BackgroundSelectionAdapter
import com.dkrasnov.photoeditor.editor.BackgroundSelectionItem
import com.dkrasnov.photoeditor.editor.PlusBackgroundSelectionItem
import com.dkrasnov.photoeditor.editor.SourceBackgroundSelectionItem
import com.dkrasnov.photoeditor.fonts.data.Font
import com.dkrasnov.photoeditor.fonts.presentation.FontSelectionBottomSheetDialog
import com.dkrasnov.photoeditor.stickers.data.StickerData
import com.dkrasnov.photoeditor.stickers.presentation.StickerSelectionBottomSheetDialog
import com.dkrasnov.photoeditor.uploadphoto.UploadPhotoBottomSheetDialog
import kotlinx.android.synthetic.main.a_main.*

class MainActivity : AppCompatActivity(),
    StickerSelectionBottomSheetDialog.StickerSelectionListener,
    FontSelectionBottomSheetDialog.FontSelectionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)

        fontImageView.setOnClickListener { showFontSelectionDialog() }
        selectStickerImageView.setOnClickListener { showStickerSelectionDialog() }


        val items = mutableListOf<BackgroundSelectionItem>().apply {
            addAll(BackgroundSourceRepository().getBackgroundSourceList().map { SourceBackgroundSelectionItem(it) })
            add(PlusBackgroundSelectionItem)
        }
        items[0].selected = true

        val adapter = BackgroundSelectionAdapter { item ->
            if (item is PlusBackgroundSelectionItem) {
                showUploadPhotoDialog()
            }
        }.apply {
            setItems(items)
        }

        backgroundSelectionRecyclerView.run {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(object : RecyclerView.ItemDecoration() {

                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)

                    outRect.left = resources.getDimensionPixelSize(R.dimen.background_selection_item_padding_left)
                    outRect.right =
                        resources.getDimensionPixelSize(R.dimen.background_sticker_selection_item_padding_right)
                }
            })
            this.adapter = adapter
        }
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)

        if (fragment is StickerSelectionBottomSheetDialog) {
            fragment.setListener(this)
        } else if (fragment is FontSelectionBottomSheetDialog) {
            fragment.setListener(this)
        }
    }

    override fun onStickerSelected(stickerData: StickerData) {

    }

    override fun onFoneSelected(font: Font) {
        val typeface = ResourcesCompat.getFont(this, font.fontRes)
        helloWorldTextView.typeface = typeface
    }

    private fun showUploadPhotoDialog() {
        val dialog = UploadPhotoBottomSheetDialog.newInstance()
        dialog.show(supportFragmentManager, UploadPhotoBottomSheetDialog.TAG)
    }

    private fun showStickerSelectionDialog() {
        val dialog = StickerSelectionBottomSheetDialog.newInstance()
        dialog.show(supportFragmentManager, StickerSelectionBottomSheetDialog.TAG)
    }

    private fun showFontSelectionDialog() {
        val dialog = FontSelectionBottomSheetDialog.newInstance()
        dialog.show(supportFragmentManager, FontSelectionBottomSheetDialog.TAG)
    }
}
