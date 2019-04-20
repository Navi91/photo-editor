package com.dkrasnov.photoeditor.stickers.data

import android.net.Uri
import com.dkrasnov.photoeditor.utils.covertToAssetsUriPath

data class StickerData(val path: String) {

    fun getUri() = Uri.parse(path.covertToAssetsUriPath())
}