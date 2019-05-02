package com.dkrasnov.photoeditor.utils

fun String.convertToAssetsUriPath() : String {
    return "file:///android_asset/$this"
}