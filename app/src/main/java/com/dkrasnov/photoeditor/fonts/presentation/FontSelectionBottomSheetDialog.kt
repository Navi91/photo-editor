package com.dkrasnov.photoeditor.fonts.presentation

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dkrasnov.photoeditor.R
import com.dkrasnov.photoeditor.fonts.data.Font
import kotlinx.android.synthetic.main.d_font_selection_bottom_sheet.*

class FontSelectionBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "font_selection_bottom_sheet_dialog_tag"

        fun newInstance() = FontSelectionBottomSheetDialog()
    }

    private var listener: FontSelectionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.d_font_selection_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        robotoTextView.setOnClickListener {
            listener?.onFontSelected(Font.ROBOTO)
            dismiss()
        }
        amaticTextView.setOnClickListener {
            listener?.onFontSelected(Font.AMATIC)
            dismiss()
        }
        lobsterTextView.setOnClickListener {
            listener?.onFontSelected(Font.LOBSTER)
            dismiss()
        }
    }

    fun setListener(listener: FontSelectionListener) {
        this.listener = listener
    }

    interface FontSelectionListener {
        fun onFontSelected(font: Font)
    }
}