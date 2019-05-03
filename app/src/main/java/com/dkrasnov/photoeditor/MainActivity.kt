package com.dkrasnov.photoeditor

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.dkrasnov.photoeditor.editor.StickerTouchListener
import com.dkrasnov.photoeditor.editor.presentation.EditorPresenter
import com.dkrasnov.photoeditor.editor.presentation.EditorView
import com.dkrasnov.photoeditor.editor.presentation.backgroundselection.BackgroundSelectionAdapter
import com.dkrasnov.photoeditor.editor.presentation.backgroundselection.BackgroundSelectionItem
import com.dkrasnov.photoeditor.fonts.data.Font
import com.dkrasnov.photoeditor.fonts.presentation.FontSelectionBottomSheetDialog
import com.dkrasnov.photoeditor.glide.GlideApp
import com.dkrasnov.photoeditor.stickers.data.StickerData
import com.dkrasnov.photoeditor.stickers.presentation.StickerSelectionBottomSheetDialog
import com.dkrasnov.photoeditor.uploadphoto.UploadPhotoBottomSheetDialog
import com.dkrasnov.photoeditor.utils.background
import com.dkrasnov.photoeditor.utils.observeMain
import com.dkrasnov.photoeditor.utils.setVisible
import com.dkrasnov.photoeditor.utils.visible
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.a_main.*
import java.io.ByteArrayOutputStream

class MainActivity : MvpAppCompatActivity(),
    StickerSelectionBottomSheetDialog.StickerSelectionListener,
    FontSelectionBottomSheetDialog.FontSelectionListener,
    UploadPhotoBottomSheetDialog.UploadPhotoDialogListener,
    EditorView {

    companion object {

        private const val TAG = "MainActivity"

        private const val UPLOAD_PHOTO_FROM_GALLERY_REQUEST_CODE = 100
        private const val MAKE_PHOTO_REQUEST_CODE = 101
    }

    @InjectPresenter
    lateinit var presenter: EditorPresenter

    private val backgroundSelectionAdapter = BackgroundSelectionAdapter { item ->
        presenter.selectBackgroundItem(item)
    }

    @ProvidePresenter
    fun provideEditorPresenter() = EditorPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)

        fontImageView.setOnClickListener { showFontSelectionDialog() }
        selectStickerImageView.setOnClickListener { showStickerSelectionDialog() }
        saveButton.setOnClickListener { savePhoto() }

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
            adapter = backgroundSelectionAdapter
        }

        messageEditText.setOnTouchListener { v, event -> false }
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)

        when (fragment) {
            is StickerSelectionBottomSheetDialog -> fragment.setListener(this)
            is FontSelectionBottomSheetDialog -> fragment.setListener(this)
            is UploadPhotoBottomSheetDialog -> fragment.setListener(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_CANCELED) return

        when (requestCode) {
            MAKE_PHOTO_REQUEST_CODE -> {
                presenter.setBackgroundPhoto(data?.extras?.get("data") as? Bitmap)
            }
            UPLOAD_PHOTO_FROM_GALLERY_REQUEST_CODE -> {
                presenter.setBackgroundPhoto(MediaStore.Images.Media.getBitmap(contentResolver, data?.data))
            }
        }
    }

    override fun setBackground(source: Any) {
        Log.d(TAG, "set background $source")

        GlideApp.with(this)
            .load(source)
            .placeholder(backgroundImageView.drawable)
            .downsample(DownsampleStrategy.AT_MOST)
            .into(backgroundImageView)
    }

    override fun setMessageStyle(dark: Boolean) {
        val textColor = if (dark) {
            Color.WHITE
        } else {
            ContextCompat.getColor(this, R.color.primaryTextColor)
        }
        val hintTextColor = if (dark) {
            ContextCompat.getColor(this, R.color.hintTextColorDark)
        } else {
            ContextCompat.getColor(this, R.color.hintTextColor)
        }

        messageEditText.run {
            setTextColor(textColor)
            setHintTextColor(hintTextColor)
        }
    }

    override fun setBackgroundSelectionItems(items: List<BackgroundSelectionItem>) {
        backgroundSelectionAdapter.setItems(items)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStickerSelected(stickerData: StickerData) {
        val imageView = ImageView(this)
        val sizePx = resources.getDimensionPixelSize(R.dimen.sticker_image_view_default_size)
        val params = FrameLayout.LayoutParams(sizePx, sizePx)

        stickersLayout.addView(imageView, params)

        imageView.setOnTouchListener(StickerTouchListener(imageView))

        GlideApp.with(imageView).load(stickerData.getUri()).into(imageView)
    }

    override fun onRequestUploadPhotoFromGallery() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }

        val chooserIntent = Intent.createChooser(getIntent, getString(R.string.select_photo))
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, UPLOAD_PHOTO_FROM_GALLERY_REQUEST_CODE)
    }

    override fun onRequestUploadPhotoFromCamera() {
        startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), MAKE_PHOTO_REQUEST_CODE)
    }

    override fun onFontSelected(font: Font) {
        val typeface = ResourcesCompat.getFont(this, font.fontRes)
        messageEditText.typeface = typeface
    }

    override fun showUploadPhotoDialog() {
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

    private fun savePhoto() {
        saveButton.isEnabled = false

        messageEditText.setVisible(messageEditText.text.isNotEmpty())
        messageEditText.clearFocus()
        contentLayout.isDrawingCacheEnabled = true

        saveBitmapCompletable(contentLayout.drawingCache).background().subscribe {
            saveButton.isEnabled = true
            contentLayout.isDrawingCacheEnabled = false
            messageEditText.visible()
        }
    }

    private fun saveBitmapCompletable(bitmap: Bitmap): Completable {
        return Completable.fromCallable {
            Log.d(TAG, "save photo type")

            MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Photo" , "photo")

//            val outputStream = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.PNG, 95, outputStream)
//            val bitmapData = outputStream.toByteArray()
//
//            Log.d(TAG, "save photo bitmap compressed")
//
//            openFileOutput("temp.png", Context.MODE_PRIVATE).use {
//                it.write(bitmapData)
//                it.flush()
//                it.close()
//            }

            Log.d(TAG, "save photo finish")
        }
    }
}
