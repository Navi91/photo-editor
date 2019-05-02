package com.dkrasnov.photoeditor.utils

import android.support.v4.graphics.ColorUtils

fun Int.isDark() : Boolean {
    return ColorUtils.calculateLuminance(this) < 0.5
}