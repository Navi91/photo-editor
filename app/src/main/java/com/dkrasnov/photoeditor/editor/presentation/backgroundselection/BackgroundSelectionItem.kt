package com.dkrasnov.photoeditor.editor.presentation.backgroundselection

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import com.dkrasnov.photoeditor.R
import com.dkrasnov.photoeditor.background.BackgroundSource
import com.dkrasnov.photoeditor.background.BeachBackgroundSource
import com.dkrasnov.photoeditor.background.ColorBackgroundSource
import com.dkrasnov.photoeditor.background.StartBackgroundSource
import com.dkrasnov.photoeditor.utils.convertToAssetsUriPath

sealed class BackgroundSelectionItem(var selected: Boolean = false) {

    abstract fun getSource(context: Context): Any?
}

class SourceBackgroundSelectionItem(
    val source: BackgroundSource, selected: Boolean
) : BackgroundSelectionItem(selected) {

    override fun getSource(context: Context): Any? {
        return when (source) {
            is ColorBackgroundSource -> GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(
                    ContextCompat.getColor(context, source.colorFromRes),
                    ContextCompat.getColor(context, source.colorToRes)
                )
            )
            is StartBackgroundSource -> source.assetsPath.convertToAssetsUriPath()
            is BeachBackgroundSource -> {
                val assetManager = context.assets

                val options = BitmapFactory.Options().apply {
                    inMutable = true
                }

                val centerBitmap = BitmapFactory.decodeStream(assetManager.open(source.centerAssetsPath),
                    null,
                    options) ?: return null

                val width = centerBitmap.width

                val topBitmap = BitmapFactory.decodeStream(assetManager.open(source.topAssetsPath)).let { bitmap ->
                    Bitmap.createScaledBitmap(bitmap, width, bitmap.height, false)
                }
                val bottomBitmap = BitmapFactory.decodeStream(assetManager.open(source.bottomAssetsPath)).let { bitmap ->
                    Bitmap.createScaledBitmap(bitmap, width, bitmap.height, false)
                }

               Canvas(centerBitmap).apply {
                    drawBitmap(topBitmap, 0f, 0f, null)
                    drawBitmap(
                        bottomBitmap,
                        0f,
                        (centerBitmap.height - bottomBitmap.height).toFloat(),
                        null
                    )
                }

                centerBitmap
            }
        }
    }

    fun getThumb(context: Context): Drawable? {
        return when (source) {
            is StartBackgroundSource -> ContextCompat.getDrawable(context, R.drawable.thumb_stars)
            is BeachBackgroundSource -> ContextCompat.getDrawable(context, R.drawable.thumb_beach)
            is ColorBackgroundSource -> {

                if (source.isWhite()) {
                    return ColorDrawable(ContextCompat.getColor(context, R.color.background_source_white_thumb))
                }

                return GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    intArrayOf(
                        ContextCompat.getColor(context, source.colorFromRes),
                        ContextCompat.getColor(context, source.colorToRes)
                    )
                )
            }
        }
    }
}

class PlusBackgroundSelectionItem(selected: Boolean = false) : BackgroundSelectionItem(selected) {

    override fun getSource(context: Context): Any? {
        return null
    }
}