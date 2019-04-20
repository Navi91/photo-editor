package com.dkrasnov.photoeditor.utils

fun String.covertToAssetsUriPath() : String {
    return "file:///android_asset/$this"
}