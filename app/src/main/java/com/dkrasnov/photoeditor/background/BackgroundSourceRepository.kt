package com.dkrasnov.photoeditor.background

import com.dkrasnov.photoeditor.R

class BackgroundSourceRepository {

    fun getBackgroundSourceList(): List<BackgroundSource> {
        return listOf(
            ColorBackgroundSource(R.color.background_source_white, R.color.background_source_white),
            ColorBackgroundSource(R.color.background_source_blue_from, R.color.background_source_blue_to),
            ColorBackgroundSource(R.color.background_source_green_from, R.color.background_source_green_to),
            ColorBackgroundSource(R.color.background_source_orange_from, R.color.background_source_orange_to),
            ColorBackgroundSource(R.color.background_source_crimson_from, R.color.background_source_crimson_to),
            ColorBackgroundSource(R.color.background_source_purple_from, R.color.background_source_purple_to),
            StartBackgroundSource,
            BeachBackgroundSource
        )
    }
}