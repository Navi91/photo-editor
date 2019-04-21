package com.dkrasnov.photoeditor

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import com.dkrasnov.photoeditor.fonts.data.Font
import com.dkrasnov.photoeditor.fonts.presentation.FontSelectionBottomSheetDialog
import com.dkrasnov.photoeditor.stickers.data.StickerData
import com.dkrasnov.photoeditor.stickers.presentation.StickerSelectionBottomSheetDialog
import kotlinx.android.synthetic.main.a_main.*

class MainActivity : AppCompatActivity(), StickerSelectionBottomSheetDialog.StickerSelectionListener,
    FontSelectionBottomSheetDialog.FontSelectionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)

        fontImageView.setOnClickListener { showFontSelectionDialog() }
        selectStickerImageView.setOnClickListener { showStickerSelectionDialog() }
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

    private fun showStickerSelectionDialog() {
        val dialog = StickerSelectionBottomSheetDialog.newInstance()
        dialog.show(supportFragmentManager, StickerSelectionBottomSheetDialog.TAG)
    }

    private fun showFontSelectionDialog() {
        val dialog = FontSelectionBottomSheetDialog.newInstance()
        dialog.show(supportFragmentManager, FontSelectionBottomSheetDialog.TAG)
    }
}
