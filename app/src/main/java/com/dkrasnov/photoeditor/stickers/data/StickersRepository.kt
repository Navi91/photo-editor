package com.dkrasnov.photoeditor.stickers.data

import android.content.Context
import io.reactivex.Single

class StickersRepository(private val context: Context) {

    companion object {

        private const val STICKERS_ASSETS_PATH = "stickers"
    }

    fun getStickers(): Single<List<StickerData>> {


        return Single.fromCallable {
            val assetsManager = context.assets
            val stickerPaths = assetsManager.list(STICKERS_ASSETS_PATH) ?: return@fromCallable emptyList<StickerData>()

            stickerPaths.map { name ->
                StickerData("$STICKERS_ASSETS_PATH/$name")
            }
        }
    }
}