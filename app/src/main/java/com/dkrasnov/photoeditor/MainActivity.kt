package com.dkrasnov.photoeditor

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dkrasnov.photoeditor.stickers.presentation.StickerSelectionBottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fontImageView.setOnClickListener { showStickersSelectionDialog() }
        selectStickerImageView.setOnClickListener { showStickersSelectionDialog() }
    }

    private fun showStickersSelectionDialog() {
        val dialog = StickerSelectionBottomSheetDialog.newInstance()
        dialog.show(supportFragmentManager, StickerSelectionBottomSheetDialog.TAG)
    }
}
