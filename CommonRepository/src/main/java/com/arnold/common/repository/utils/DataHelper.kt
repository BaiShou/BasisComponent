package com.arnold.common.repository.utils

import android.content.Context
import android.os.Environment
import android.os.Parcelable
import com.tencent.mmkv.MMKV
import java.io.*
import java.util.*

/**
 * 处理数据或本地文件的工具类
 */
object DataHelper {

    private var mmkv: MMKV = MMKV.defaultMMKV()

    /**
     * 存储数据
     */
    fun encode(key: String, value: Any) {
        when (value) {
            is String -> mmkv.encode(key, value)
            is Float -> mmkv.encode(key, value)
            is Boolean -> mmkv.encode(key, value)
            is Int -> mmkv.encode(key, value)
            is Long -> mmkv.encode(key, value)
            is Double -> mmkv.encode(key, value)
            is ByteArray -> mmkv.encode(key, value)
            is Nothing -> return
        }
    }

    fun <T : Parcelable> encode(key: String, t: T) {
        mmkv.encode(key, t)
    }

    fun encode(key: String, sets: Set<String>) {
        mmkv.encode(key, sets)
    }

    fun decodeInt(key: String): Int {
        return mmkv.decodeInt(key, 0)
    }

    fun decodeDouble(key: String): Double {
        return mmkv.decodeDouble(key, 0.00)
    }

    fun decodeLong(key: String): Long {
        return mmkv.decodeLong(key, 0L)
    }

    fun decodeBoolean(key: String): Boolean {
        return mmkv.decodeBool(key, false)
    }

    fun decodeFloat(key: String): Float {
        return mmkv.decodeFloat(key, 0F)
    }

    fun decodeByteArray(key: String): ByteArray {
        return mmkv.decodeBytes(key)
    }

    fun decodeString(key: String): String {
        return mmkv.decodeString(key, "")
    }

    fun <T : Parcelable> decodeParcelable(key: String, tClass: Class<T>): T {
        return mmkv.decodeParcelable(key, tClass)
    }

    fun decodeStringSet(key: String): Set<String> {
        return mmkv.decodeStringSet(key, Collections.emptySet())
    }

    fun removeKey(key: String) {
        mmkv.removeValueForKey(key)
    }

    fun clearAll() {
        mmkv.clearAll()
    }

    /**
     * 返回缓存文件夹
     */
    fun getCacheFile(context: Context): File {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            var file: File?
            file = context.externalCacheDir//获取系统管理的sd卡缓存文件
            if (file == null) {//如果获取的文件为空,就使用自己定义的缓存文件夹做缓存路径
                file = File(getCacheFilePath(context))
                makeDirs(file)
            }
            return file
        } else {
            return context.cacheDir
        }
    }

    /**
     * 获取自定义缓存文件地址
     *
     * @param context
     * @return
     */
    fun getCacheFilePath(context: Context): String {
        val packageName = context.packageName
        return "/mnt/sdcard/$packageName"
    }

    /**
     * 创建未存在的文件夹
     *
     * @param file
     * @return
     */
    fun makeDirs(file: File): File {
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    /**
     * 使用递归获取目录文件大小
     *
     * @param dir
     * @return
     */
    fun getDirSize(dir: File?): Long {
        if (dir == null) {
            return 0
        }
        if (!dir.isDirectory) {
            return 0
        }
        var dirSize: Long = 0
        val files = dir.listFiles()
        for (file in files!!) {
            if (file.isFile) {
                dirSize += file.length()
            } else if (file.isDirectory) {
                dirSize += file.length()
                dirSize += getDirSize(file) // 递归调用继续统计
            }
        }
        return dirSize
    }

    /**
     * 使用递归删除文件夹
     *
     * @param dir
     * @return
     */
    fun deleteDir(dir: File?): Boolean {
        if (dir == null) {
            return false
        }
        if (!dir.isDirectory) {
            return false
        }
        val files = dir.listFiles()
        for (file in files!!) {
            if (file.isFile) {
                file.delete()
            } else if (file.isDirectory) {
                deleteDir(file) // 递归调用继续删除
            }
        }
        return true
    }

    @Throws(IOException::class)
    fun bytyToString(`in`: InputStream): String {
        val out = ByteArrayOutputStream()
        var read: Int
        `in`.use { input ->
            out.use {
                while (input.read().also { read = it } != -1) {
                    it.write(read)
                }
            }
        }
        val result = out.toString()
        out.close()
        return result
    }

}