package com.dkrasnov.photoeditor.stickers.data

import io.reactivex.Single

class StickersRepository {

    fun getStickers(): Single<List<StickerData>> {

        return Single.fromCallable {
            emptyList<StickerData>()
        }
    }
}