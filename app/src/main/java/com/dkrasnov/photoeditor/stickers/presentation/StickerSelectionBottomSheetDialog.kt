package com.dkrasnov.photoeditor.stickers.presentation

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.GridLayoutManager
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dkrasnov.photoeditor.R
import com.dkrasnov.photoeditor.stickers.data.StickerData
import com.dkrasnov.photoeditor.stickers.data.StickersRepository
import com.dkrasnov.photoeditor.utils.safeDispose
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.d_stickers_selection.*

class StickerSelectionBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "stickers_selections_bottom_sheet_dialog"

        fun newInstance() = StickerSelectionBottomSheetDialog()
    }

    private var stickersDisposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.BottomSheetDialog)
        return inflater.cloneInContext(contextThemeWrapper).inflate(R.layout.d_stickers_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val spanCount = resources.getInteger(R.integer.sticker_selection_grid_span_count)

        recyclerView.layoutManager = GridLayoutManager(context, spanCount)

        stickersDisposable = StickersRepository(requireContext()).getStickers().subscribe({
            setStickers(it)
        }, {

        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        view?.post {
//            val parent = view?.parent as View
//            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
//            val behavior = params.behavior as BottomSheetBehavior
//            behavior.peekHeight = 5000
////            behavior.state = BottomSheetBehavior.STATE_EXPANDED
//        }

//            requireContext().resources.getDimensionPixelSize(R.dimen.sticker_selection_bottom_dialog_peek_height)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        stickersDisposable.safeDispose()
    }

    private fun setStickers(stickers: List<StickerData>) {
        val adapter = StickersSelectionAdapter().apply {
            setItems(stickers)
        }

        recyclerView.adapter = adapter
    }
}