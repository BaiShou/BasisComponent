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
    return this.subtype.toLowerCase().contains("plain")
}

fun MediaType.isJson(): Boolean {
    return this.subtype.toLowerCase().contains("json")
}

fun MediaType.isXml(): Boolean {
    return this.subtype.toLowerCase().contains("xml")
}

fun MediaType.isHtml(): Boolean {
    return this.subtype.toLowerCase().contains("html")
}

fun MediaType.isForm(): Boolean {
    return this.subtype.toLowerCase().contains("x-www-form-urlencoded")
}