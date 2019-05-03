package com.dkrasnov.photoeditor.stickers.data

import android.net.Uri
import com.dkrasnov.photoeditor.utils.convertToAssetsUriPath

data class StickerData(val path: String) {

    fun getUri(): Uri = Uri.parse(path.convertToAssetsUriPath())
}