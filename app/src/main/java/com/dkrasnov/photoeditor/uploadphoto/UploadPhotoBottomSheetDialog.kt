package com.dkrasnov.photoeditor.uploadphoto

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dkrasnov.photoeditor.R
import kotlinx.android.synthetic.main.d_upload_photo.*

class UploadPhotoBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {

        const val TAG = "upload_photo_bottom_sheet_dialog_tag"

        fun newInstance() = UploadPhotoBottomSheetDialog()
    }

    private var listener: UploadPhotoDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.d_upload_photo, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryTextView.setOnClickListener {
            listener?.onRequestUploadPhotoFromGallery()
            dismiss()
        }
        cameraTextView.setOnClickListener {
            listener?.onReuqestUploadPhotoFromCamera()
            dismiss()
        }
    }

    fun setListener(listener: UploadPhotoDialogListener) {
        this.listener = listener
    }

    interface UploadPhotoDialogListener {

        fun onRequestUploadPhotoFromGallery()

        fun onReuqestUploadPhotoFromCamera()
    }
}