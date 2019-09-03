package com.arnold.common.network.utils

import okhttp3.MediaType

fun MediaType.isParseable(): Boolean {
    return isText() || isPlain() || isJson()
            || isXml() || isHtml() || isForm()
}

fun MediaType.isText(): Boolean {
    return this.type == "text"
}

fun MediaType.isPlain(): Boolean {
    return this.type.toLowerCase() == "plain"
}

fun MediaType.isJson(): Boolean {
    return this.type.toLowerCase() == "json"
}

fun MediaType.isXml(): Boolean {
    return this.type.toLowerCase() == "isXml"
}

fun MediaType.isHtml(): Boolean {
    return this.type.toLowerCase() == "html"
}

fun MediaType.isForm(): Boolean {
    return this.type.toLowerCase() == "x-www-form-urlencoded"
}