package com.arnold.common.network.utils

import android.text.TextUtils
import java.nio.charset.Charset

fun Charset.convertCharset(): String {
    val s = this.toString()
    val indexOf = s.indexOf("[")
    if (indexOf == -1) {
        return s
    }
    return s.substring(indexOf + 1, s.length - 1)
}

fun String.formatJson(): String {
    if (TextUtils.isEmpty(this)) {
        return ""
    }
    val sb = StringBuilder()
    var last = '\u0000'
    var current = '\u0000'
    var indent = 0
    for (i in 0 until length) {
        last = current
        current = get(i)
        //遇到{ [换行，且下一行缩进
        when (current) {
            '{', '[' -> {
                sb.append(current)
                sb.append('\n')
                indent++
                addIndentBlank(sb, indent)
            }
            //遇到} ]换行，当前行缩进
            '}', ']' -> {
                sb.append('\n')
                indent--
                addIndentBlank(sb, indent)
                sb.append(current)
            }
            //遇到,换行
            ',' -> {
                sb.append(current)
                if (last != '\\') {
                    sb.append('\n')
                    addIndentBlank(sb, indent)
                }
            }
            else -> sb.append(current)
        }
    }
    return sb.toString()

}

/**
 * 添加space
 *
 * @param sb
 * @param indent
 */
private fun addIndentBlank(sb: StringBuilder, indent: Int) {
    for (i in 0 until indent) {
        sb.append('\t')
    }
}


/**
 * http 请求数据返回 json 中中文字符为 unicode 编码转汉字转码
 *
 * @param theString
 * @return 转化后的结果.
 */
fun String.decodeUnicode(): String {
    var aChar: Char

    val outBuffer = StringBuffer(length)
    var x = 0
    while (x < length) {
        aChar = this[x++]
        if (aChar == '\\') {
            aChar = this[x++]
            if (aChar == 'u') {
                var value = 0
                for (i in 0..3) {
                    aChar = this[x++]
                    value = when (aChar) {
                        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> (value shl 4) + aChar.toInt() - '0'.toInt()
                        'a', 'b', 'c', 'd', 'e', 'f' -> (value shl 4) + 10 + aChar.toInt() - 'a'.toInt()
                        'A', 'B', 'C', 'D', 'E', 'F' -> (value shl 4) + 10 + aChar.toInt() - 'A'.toInt()
                        else -> throw IllegalArgumentException(
                            "Malformed   \\uxxxx   encoding."
                        )
                    }

                }
                outBuffer.append(value.toChar())
            } else {
                if (aChar == 't') {
                    aChar = '\t'
                } else if (aChar == 'r') {
                    aChar = '\r'
                } else if (aChar == 'n') {
                    aChar = '\n'
                } else if (aChar == 'f') {
                    aChar = '\u000C' //'\f'
                }
                outBuffer.append(aChar)
            }
        } else {
            outBuffer.append(aChar)
        }
    }
    return outBuffer.toString()
}