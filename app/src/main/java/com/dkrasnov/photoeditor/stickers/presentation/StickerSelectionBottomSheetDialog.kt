package com.dkrasnov.photoeditor.stickers.presentation

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dkrasnov.photoeditor.R
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.d_stickers_selection.*

class StickerSelectionBottomSheetDialog: BottomSheetDialogFragment() {

    companion object {
        const val TAG = "stickers_selections_bottom_sheet_dialog"

        fun newInstance() = StickerSelectionBottomSheetDialog()
    }

    private var srickersDisposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.d_stickers_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spanCount = resources.getInteger(R.integer.sticker_selection_grid_span_count)

        recyclerView.layoutManager = GridLayoutManager(context, spanCount)


    }
}