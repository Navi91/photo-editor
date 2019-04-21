package com.dkrasnov.photoeditor.fonts.data

import android.support.annotation.FontRes
import com.dkrasnov.photoeditor.R

enum class Font(@FontRes val fontRes: Int) {
    LOBSTER(R.font.lobster), ROBOTO(R.font.roboto_regular), AMATIC(R.font.amatic_regular)
}