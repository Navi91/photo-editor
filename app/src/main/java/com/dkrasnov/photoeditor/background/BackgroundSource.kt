package com.dkrasnov.photoeditor.background

import android.support.annotation.ColorRes
import com.dkrasnov.photoeditor.R

sealed class BackgroundSource

class ColorBackgroundSource(@ColorRes val colorFromRes: Int, @ColorRes val colorToRes: Int) : BackgroundSource() {

    fun isWhite() = colorFromRes == R.color.background_source_white

    override fun hashCode(): Int {
        var hashCode = 17

        hashCode = 37 * hashCode + colorFromRes
        hashCode = 37 * hashCode + colorToRes

        return hashCode
    }

    override fun equals(other: Any?): Boolean {
        if (other !is ColorBackgroundSource) return false

        return colorFromRes == other.colorFromRes && colorToRes == other.colorToRes
    }
}

object StartBackgroundSource : BackgroundSource() {

    const val assetsPath = "backgrounds/bg_stars_center.png"

    override fun equals(other: Any?): Boolean {
        return other is StartBackgroundSource
    }
}

object BeachBackgroundSource : BackgroundSource() {

    const val bottomAssetsPath = "backgrounds/bg_beach_bottom.png"
    const val centerAssetsPath = "backgrounds/bg_beach_center.png"
    const val topAssetsPath = "backgrounds/bg_beach_top.png"

    override fun equals(other: Any?): Boolean {
        return other is BeachBackgroundSource
    }
}