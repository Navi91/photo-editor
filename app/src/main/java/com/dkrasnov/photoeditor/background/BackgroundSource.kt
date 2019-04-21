package com.dkrasnov.photoeditor.background

import android.support.annotation.ColorRes
import com.dkrasnov.photoeditor.R

sealed class BackgroundSource

class ColorBackgroundSource(@ColorRes val colorFromRes: Int, @ColorRes val colorToRes: Int) : BackgroundSource() {

    fun isWhite() = colorFromRes == R.color.background_source_white
}

object StartBackgroundSource : BackgroundSource()

object BeachBackgroundSource : BackgroundSource()